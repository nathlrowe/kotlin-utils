package io.github.nathlrowe.utils

import org.apache.commons.cli.*
import org.apache.logging.log4j.LogManager
import kotlin.system.exitProcess

/**
 * Runs the given [block] with a [CommandLine] receiver built according to the provided parser configuration.
 *
 * This function injects a help-displaying option, if configured. The help display option uses the keys `-h` and
 * `--help`, meaning if [helpSyntax] is set, those options are reserved. Attempting to include options with reserved
 * keys will throw an exception.
 *
 * @param args The CLI arguments to parse.
 * @param options The CLI options to parse arguments against.
 * @param helpSyntax The syntax to be used when displaying command help, or `null` to not include help displaying.
 */
fun <T> withCommand(
    args: Array<String>,
    options: Options,
    helpSyntax: String? = null,
    block: CommandLine.() -> T
): T {
    require(helpSyntax == null || !options.hasLongOption("help")) { "Long option 'help' is reserved." }
    require(helpSyntax == null || !options.hasShortOption("h")) { "Short option 'h' is reserved." }

    val logger by lazy { LogManager.getLogger() }
    val parser = DefaultParser()

    if (helpSyntax != null) {
        options.apply {
            addOption("h", "help", false, "show this help display")
        }
    }

    return try {
        parser.parse(options, args).block()
    } catch (e: ParseException) {
        logger.error(e.stackTraceToString())
        helpSyntax?.let {
            val helpFormatter = HelpFormatter()
            helpFormatter.printHelp(it, options, true)
        }
        exitProcess(1)
    } catch (e: Throwable) {
        logger.error(e.stackTraceToString())
        exitProcess(1)
    }
}
