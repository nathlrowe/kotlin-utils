package io.github.nathlrowe.utils

import kotlinx.coroutines.*
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

fun runProcess(
    command: List<String>,
    timeout: Duration? = null,
    directory: Path? = null,
    environment: Map<String, String>? = null,
    stdin: Redirect.In? = null,
    stdout: Redirect.Out? = null,
    stderr: Redirect.Out? = null,
    destroyForcibly: Boolean = false,
    redirectErrorStream: Boolean = false,
): Deferred<ProcessResult> {
    val pb = ProcessBuilder(command).apply {
        directory?.let { directory(it.toFile()) }
        environment?.let { environment().putAll(it) }

        stdin?.javaRedirect?.let { redirectInput(it) }
        stdout?.javaRedirect?.let { redirectOutput(it) }
        stderr?.javaRedirect?.let {
            redirectError(it)
            if (it.type() == ProcessBuilder.Redirect.Type.PIPE && redirectErrorStream) {
                redirectErrorStream(true)
            }
        }
    }

    return runBlocking {
        async(Dispatchers.IO) {
            val process = pb.start()
            try {
                launch {
                    stdin?.handle(process.outputStream)
                }

                val capturedOutputDeferred = async {
                    stdout?.handle(process.inputStream)
                }
                val capturedErrorDeferred = async {
                    stderr?.handle(process.errorStream)
                }

                val exitValue = if (timeout != null) {
                    val completed = process.waitFor(timeout.inWholeMilliseconds, TimeUnit.MILLISECONDS)
                    if (!completed) throw RuntimeException("Process timed out.") // TODO use different exception type
                    process.exitValue()
                } else {
                    process.waitFor()
                }

                ProcessResult(
                    exitValue = exitValue,
                    output = capturedOutputDeferred.await(),
                    error = capturedErrorDeferred.await()
                )
            } catch (e: CancellationException) {
                if (destroyForcibly) process.destroyForcibly() else process.destroy()
                throw e
            }
        }
    }
}

fun runProcess(
    vararg command: String,
    timeout: Duration? = null,
    directory: Path? = null,
    environment: Map<String, String>? = null,
    stdin: Redirect.In? = null,
    stdout: Redirect.Out? = null,
    stderr: Redirect.Out? = null,
    destroyForcibly: Boolean = false,
): Deferred<ProcessResult> = runProcess(
    command = command.toList(),
    timeout = timeout,
    directory = directory,
    environment = environment,
    stdin = stdin,
    stdout = stdout,
    stderr = stderr,
    destroyForcibly = destroyForcibly,
)

sealed interface Redirect {
    val javaRedirect: ProcessBuilder.Redirect get() = ProcessBuilder.Redirect.PIPE

    /**
     * Redirection directive for a process's input stream.
     */
    sealed interface In : Redirect {
        suspend fun handle(outputStream: OutputStream) = Unit

        /**
         * Redirection directive to handle a process's input stream with the given [handler].
         */
        class Handler(val handler: suspend (OutputStream) -> Unit) : In {
            override suspend fun handle(outputStream: OutputStream) { handler(outputStream) }
        }
    }

    /**
     * Redirection directive for a process's output stream.
     */
    sealed interface Out : Redirect {
        suspend fun handle(inputStream: InputStream): String? = null

        /**
         * Redirection directive to handle a process's output stream with the given [handler].
         *
         * The result from the given [handler] will be included in the [process result][ProcessResult].
         */
        class Handler(val handler: suspend (InputStream) -> String?) : Out {
            override suspend fun handle(inputStream: InputStream): String? = handler(inputStream)
        }
    }

    /**
     * Redirection directive to redirect standard I/O streams to inherit from the caller.
     */
    data object Inherit : In, Out {
        override val javaRedirect: ProcessBuilder.Redirect = ProcessBuilder.Redirect.INHERIT
    }

    /**
     * Redirection directive to redirect a process's input to read from the given [file].
     */
    class From(val file: Path) : In {
        override val javaRedirect: ProcessBuilder.Redirect get() = ProcessBuilder.Redirect.from(file.toFile())
    }

    /**
     * Redirection directive to redirect a process's input to the given [byteArray].
     */
    class ByteArray(val byteArray: kotlin.ByteArray) : In {
        override suspend fun handle(outputStream: OutputStream) {
            outputStream.use { it.write(byteArray) }
        }
    }

    /**
     * Redirection directive to redirect a process's input to the given [charArray].
     */
    class CharArray(val charArray: kotlin.CharArray) : In {
        override suspend fun handle(outputStream: OutputStream) {
            outputStream.bufferedWriter().use { it.write(charArray) }
        }
    }

    /**
     * Redirection directive to redirect a process's input to the given [charSequence].
     */
    class CharSequence(val charSequence: kotlin.CharSequence) : In {
        override suspend fun handle(outputStream: OutputStream) {
            outputStream.bufferedWriter().use { it.write(charSequence.toString()) }
        }
    }

    /**
     * Redirection directive to capture a process's output as a string variable.
     *
     * The captured output will be included in the [process result][ProcessResult].
     */
    data object Capture : Out {
        override suspend fun handle(inputStream: InputStream): String {
            return inputStream.bufferedReader().use { it.readText() }
        }
    }

    /**
     * Redirection directive to redirect a process's output to write to the given [file].
     */
    class To(val file: Path) : Out {
        override val javaRedirect: ProcessBuilder.Redirect get() = ProcessBuilder.Redirect.to(file.toFile())
    }

    /**
     * Redirection directive to redirect a process's output to append to the given [file].
     */
    class AppendTo(val file: Path) : Out {
        override val javaRedirect: ProcessBuilder.Redirect get() = ProcessBuilder.Redirect.appendTo(file.toFile())
    }
}

data class ProcessResult(
    val exitValue: Int,
    val output: String?,
    val error: String?,
)
