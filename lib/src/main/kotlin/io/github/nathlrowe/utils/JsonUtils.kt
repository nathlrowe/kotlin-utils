package io.github.nathlrowe.utils

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

fun mergeJsonElements(vararg elements: JsonElement) = mergeJsonElements(elements.asIterable())

fun mergeJsonElements(elements: Iterable<JsonElement>): JsonElement {
    return elements.reduce { acc, jsonElement -> mergeJsonElements(acc, jsonElement) }
}

fun mergeJsonElements(first: JsonElement, second: JsonElement): JsonElement {
    return when {
        first is JsonObject && second is JsonObject -> mergeJsonObjects(first, second)
        first is JsonArray && second is JsonArray -> JsonArray(first + second)
        first is JsonArray -> JsonArray(first + second)
        second is JsonArray -> JsonArray(listOf(first) + second)
        else -> JsonArray(listOf(first, second))
    }
}

fun mergeJsonObjects(first: JsonObject, second: JsonObject): JsonObject {
    return buildJsonObject {
        val keys = first.keys + second.keys
        for (key in keys) {
            val value1 = first[key]
            val value2 = second[key]
            if (value1 is JsonObject && value2 is JsonObject) {
                put(key, mergeJsonObjects(value1, value2))
            } else {
                put(key, (value2 ?: value1)!!)
            }
        }
    }
}
