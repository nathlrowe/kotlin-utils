package io.github.nathlrowe.utils

import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.ArchiveOutputStream
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import java.nio.file.Path
import kotlin.io.path.*

interface Archiver {
    fun archiveEntries(entries: Sequence<Entry>, targetFile: Path)

    fun archiveFile(file: Path, targetFile: Path) {
        archiveEntries(sequenceOf(Entry(file, file.name)), targetFile)
    }

    @OptIn(ExperimentalPathApi::class)
    fun archiveDirectory(dir: Path, targetFile: Path) {
        val entries = dir.walk().map { Entry(it, it.relativeTo(dir).toString()) }
        archiveEntries(entries, targetFile)
    }

    @OptIn(ExperimentalPathApi::class)
    fun archive(sources: Sequence<Path>, targetFile: Path) {
        val files = sources.walk().distinct()
        val commonDir = files.findCommonDirectory()
        val entries = files.map { Entry(it, it.relativeTo(commonDir).toString()) }
        return archiveEntries(entries, targetFile)
    }

    fun archive(sources: Iterable<Path>, targetFile: Path) = archive(sources.asSequence(), targetFile)

    data class Entry(
        val source: Path,
        val name: String
    )

    enum class Format(
        val extension: String,
        private val archiver: () -> Archiver
    ) {
        ZIP("zip", { ZipArchiver() }),
        TAR("tar.gz", { TarArchiver() }),
        SEVEN_Z("7z", { SevenZArchiver() });

        fun getArchiver() = archiver()

        fun archiveFile(file: Path, targetFile: Path) = getArchiver().archiveFile(file, targetFile)

        fun archiveDirectory(dir: Path, targetFile: Path) = getArchiver().archiveDirectory(dir, targetFile)

        fun archive(sources: Sequence<Path>, targetFile: Path) = getArchiver().archive(sources, targetFile)
    }
}

interface StandardArchiver<E : ArchiveEntry> : Archiver {
    fun getArchiveOutputStream(targetFile: Path): ArchiveOutputStream<E>

    override fun archiveEntries(entries: Sequence<Archiver.Entry>, targetFile: Path) {
        getArchiveOutputStream(targetFile).use { outputStream ->
            entries.forEach { outputStream.writeEntry(it.source, it.name) }
            outputStream.finish()
        }
    }
}

class ZipArchiver : StandardArchiver<ZipArchiveEntry> {
    override fun getArchiveOutputStream(targetFile: Path): ArchiveOutputStream<ZipArchiveEntry> {
        return ZipArchiveOutputStream(targetFile)
    }
}

class TarArchiver : StandardArchiver<TarArchiveEntry> {
    override fun getArchiveOutputStream(targetFile: Path): ArchiveOutputStream<TarArchiveEntry> {
        return TarArchiveOutputStream(targetFile.outputStream())
    }
}

class SevenZArchiver : Archiver {
    override fun archiveEntries(entries: Sequence<Archiver.Entry>, targetFile: Path) {
        SevenZOutputFile(targetFile.toFile()).use { outputFile ->
            entries.forEach { entry ->
                val archiveEntry = outputFile.createArchiveEntry(entry.source, entry.name)
                outputFile.putArchiveEntry(archiveEntry)
                if (entry.source.isRegularFile()) {
                    entry.source.inputStream().use { outputFile.write(it) }
                }
                outputFile.closeArchiveEntry()
            }
            outputFile.finish()
        }
    }
}

private fun <E : ArchiveEntry> ArchiveOutputStream<E>.writeEntry(source: Path, entryName: String) {
    val entry = createArchiveEntry(source, entryName)
    putArchiveEntry(entry)
    if (source.isRegularFile()) {
        source.inputStream().use { it.copyTo(this) }
    }
    closeArchiveEntry()
}
