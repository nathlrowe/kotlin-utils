package io.github.nathlrowe.utils

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.nio.file.Path
import java.sql.ResultSet
import kotlin.io.path.bufferedWriter

fun ResultSet.printToCsv(
    file: Path,
    csvFormat: CSVFormat = CSVFormat.EXCEL,
    printHeader: Boolean = true
) {
    file.bufferedWriter().use { writer ->
        CSVPrinter(writer, csvFormat).use { it.printRecords(this, printHeader) }
    }
}
