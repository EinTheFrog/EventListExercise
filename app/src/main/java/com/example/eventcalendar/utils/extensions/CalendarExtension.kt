package com.example.eventcalendar.utils.extensions

import java.util.Calendar

fun Calendar.toShortString() =
    "${get(Calendar.YEAR)}-${get(Calendar.MONTH)}-${get(Calendar.DATE)}"

fun Calendar.toReadableString() =
    "${get(Calendar.DATE)}/${get(Calendar.MONTH)}/${get(Calendar.YEAR)}"