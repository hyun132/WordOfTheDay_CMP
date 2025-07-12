package org.hyun.projectkmp.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getFormattedCurrentDate() = with(
    (Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date)
) {
    "${year}-${monthNumber.toString().padStart(2, '0')}-${
        dayOfMonth.toString().padStart(2, '0')
    }"
}