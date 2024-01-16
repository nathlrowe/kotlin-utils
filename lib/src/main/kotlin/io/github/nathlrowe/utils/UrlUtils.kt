package io.github.nathlrowe.utils

import java.net.URL
import java.net.URLStreamHandler

/**
 * Builder class for [URL] objects.
 */
class URLBuilder(
    var spec: String? = null,
    var protocol: String? = null,
    var host: String? = null,
    var port: Int = -1,
    var file: String? = null,
    var context: URL? = null,
    var handler: URLStreamHandler? = null,
) {
    fun build(): URL {
        val spec = spec
        return if (spec != null) {
            URL(context, spec, handler)
        } else {
            val protocol = checkNotNull(protocol) { "Protocol cannot be null" }
            val file = checkNotNull(file) { "File cannot be null" }
            URL(protocol, host, port, file, handler)
        }
    }
}

fun URL(builderAction: URLBuilder.() -> Unit): URL {
    val builder = URLBuilder()
    builder.builderAction()
    return builder.build()
}
