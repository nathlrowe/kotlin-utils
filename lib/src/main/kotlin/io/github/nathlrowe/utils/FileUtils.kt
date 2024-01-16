package io.github.nathlrowe.utils

import java.io.File
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.PathWalkOption
import kotlin.io.path.walk

// region TODO update these, and migrate to java.nio.file.Path

/**
 * Returns the first path which corresponds to an existing file.
 *
 * @throws NoSuchElementException if no such file is found
 */
fun firstExistingFilePath(paths: Iterable<String?>): String {
    return paths.filterNotNull().first { File(it).exists() }
}

/**
 * Returns the first path which corresponds to an existing file.
 *
 * @throws NoSuchElementException if no such file is found
 */
fun firstExistingFilePath(vararg paths: String?): String = firstExistingFilePath(paths.asIterable())

/**
 * Returns the first path which corresponds to an existing file, or `null` if no such file is found.
 */
fun firstExistingFilePathOrNull(paths: Iterable<String?>): String? {
    return paths.filterNotNull().firstOrNull { File(it).exists() }
}

/**
 * Returns the first path which corresponds to an existing file, or `null` if no such file is found.
 */
fun firstExistingFilePathOrNull(vararg paths: String?): String? = firstExistingFilePathOrNull(paths.asIterable())

// endregion

/**
 * Returns a sequence over the results from walking each file in this sequence.
 */
@OptIn(ExperimentalPathApi::class)
fun Sequence<Path>.walk(vararg options: PathWalkOption): Sequence<Path> = flatMap { it.walk(*options) }

/**
 * Returns a sequence over the results from walking each file in this iterable.
 */
@OptIn(ExperimentalPathApi::class)
fun Iterable<Path>.walk(vararg options: PathWalkOption): Sequence<Path> = asSequence().walk(*options)

/**
 * Returns the most-specific directory containing all the files in this sequence.
 */
fun Sequence<Path>.findCommonDirectory(): Path {
    val iterator = iterator()
    check(iterator.hasNext()) { "Sequence was empty." }
    var curDirectory = iterator.next().parent
    for (path in iterator) {
        while (!path.startsWith(curDirectory)) {
            curDirectory = curDirectory.parent
        }
    }
    return curDirectory
}

/**
 * Returns the most-specific directory containing all the files in this iterable.
 */
fun Iterable<Path>.findCommonDirectory() = asSequence().findCommonDirectory()
