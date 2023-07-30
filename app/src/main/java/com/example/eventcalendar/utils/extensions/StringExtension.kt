package com.example.eventcalendar.utils.extensions

fun String.allIndicesOf(char: Char): List<Int> {
    val result = mutableListOf<Int>()
    var lastIndex = 0
    while (lastIndex < length) {
        lastIndex = indexOf(char = char, startIndex = lastIndex)
        if (lastIndex == -1) break
        result.add(lastIndex)
    }
    return result
}