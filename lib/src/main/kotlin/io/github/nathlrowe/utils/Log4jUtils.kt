package io.github.nathlrowe.utils

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.config.Configurator

fun setLog4jLevel(level: Level) {
    Configurator.setRootLevel(level)
}

fun setLog4jLevel(loggerName: String, level: Level) {
    Configurator.setLevel(loggerName, level)
}

fun setLog4jLevel(logger: Logger, level: Level) {
    Configurator.setLevel(logger, level)
}
