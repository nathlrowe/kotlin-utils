package io.github.nathlrowe.utils

/**
 * Returns the enum entry with the specified [name], or `null` if the enum value does not exist.
 *
 * @see enumValues
 */
inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String): T? {
    return enumValues<T>().firstOrNull { it.name == name }
}

/**
 * Returns the enum entry matching the given [name] after applying the given [transform].
 *
 * @see indexOf
 */
inline fun <reified T : Enum<T>> enumValueOf(
    name: String,
    tryAsIndex: Boolean = true,
    noinline transform: ((String) -> String)? = String::clean
): T {
    return enumValues<T>()[indexOf<T>(name, tryAsIndex, transform)]
}

/**
 * Returns the enum entry matching the given [name] after applying the given [transform], or `null` if the enum value
 * does not exist.
 *
 * @see indexOf
 */
inline fun <reified T : Enum<T>> enumValueOfOrNull(
    name: String,
    tryAsIndex: Boolean = true,
    noinline transform: ((String) -> String)? = String::clean
): T? {
    return enumValues<T>().getOrNull(indexOf<T>(name, tryAsIndex, transform))
}

/**
 * Returns the index of the enum entry matching the given [name] after applying the given [transform].
 *
 * @param tryAsIndex If true and [name] is an integer, will return the value denoted by [name], or -1 if the value
 * is not a valid index.
 */
inline fun <reified T : Enum<T>> indexOf(
    name: String,
    tryAsIndex: Boolean = true,
    noinline transform: ((String) -> String)? = String::clean
): Int {
    val enumValues = enumValues<T>()

    if (tryAsIndex) name.toIntOrNull()?.let { index ->
        return if (index in enumValues.indices) index else -1
    }

    return if (transform != null) {
        val argClean = transform(name)
        enumValues.indexOfFirst { transform(it.name) == argClean }
    } else {
        enumValues.indexOfFirst { it.name == name }
    }
}

@PublishedApi
internal fun String.clean() = this.trim().lowercase()
