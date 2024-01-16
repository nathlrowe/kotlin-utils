package io.github.nathlrowe.utils

import java.net.URI

/**
 * Builder class for [URI] objects.
 */
class URIBuilder(
    var scheme: String? = null,
    var authority: String? = null,
    var userInfo: String? = null,
    var username: String? = null,
    var password: String? = null,
    var host: String? = null,
    var port: Int = -1,
    var path: String? = null,
    var query: String? = null,
    val queryParams: MutableMap<String, Any> = mutableMapOf(),
    var queryDelimiter: String = "&",
    var fragment: String? = null,
) {
    fun build(): URI {
        val query = (query ?: queryParams.toList().joinToString(queryDelimiter) { "${it.first}=${it.second}" })
            .takeIf { it.isNotBlank() }
        val authority = authority
        return if (authority != null) {
            URI(scheme, authority, path, query, fragment)
        } else {
            val userInfo = userInfo ?: run {
                val username = username
                if (username != null) {
                    buildString {
                        append(username)
                        password?.let { append(":$it") }
                    }
                } else null
            }
            URI(scheme, userInfo, host, port, path, query, fragment)
        }
    }
}

fun URI(builderAction: URIBuilder.() -> Unit): URI {
    val builder = URIBuilder()
    builder.builderAction()
    return builder.build()
}
