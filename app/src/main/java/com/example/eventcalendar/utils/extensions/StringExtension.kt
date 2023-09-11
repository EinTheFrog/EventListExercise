package com.example.eventcalendar.utils.extensions

import java.util.Calendar

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

fun String.toCalendar(): Calendar {
    val dashIndices = allIndicesOf('-')
    val yearStart = 0
    val yearEnd = dashIndices[0]
    val year = substring(yearStart, yearEnd).toInt()
    val monthStart = dashIndices[0] + 1
    val monthEnd = dashIndices[1]
    val month = substring(monthStart, monthEnd).toInt()
    val dateStart = dashIndices[1] + 1
    val dateEnd = length
    val date = substring(dateStart, dateEnd).toInt()
    val calendar = Calendar.getInstance()
    calendar.set(year, month, date)
    return calendar
}