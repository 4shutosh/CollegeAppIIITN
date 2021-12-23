package com.college.app.utils.extensions

import com.college.app.utils.extensions.DateTimeDifferenceUnit.DAYS
import com.college.app.utils.extensions.DateTimeDifferenceUnit.HOURS
import com.college.app.utils.extensions.DateTimeDifferenceUnit.MINUTES
import com.college.app.utils.extensions.DateTimeDifferenceUnit.MONTHS
import com.college.app.utils.extensions.DateTimeDifferenceUnit.PAST
import com.college.app.utils.extensions.DateTimeDifferenceUnit.SECONDS
import com.college.app.utils.extensions.DateTimeDifferenceUnit.YEARS
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.concurrent.TimeUnit


fun Instant.getCalendarFormattedDate(): String {
    val localDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDateTime.dayOfMonth}th ${localDateTime.month}, ${localDateTime.year}"
}

fun Instant.getFormattedTime(): String {
    val localDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDateTime.hour}:${localDateTime.minute}"
}


/*
returns time left in [Int] and its unit in [String] (seconds, minutes, hours, days, months, years)
positive int implies future time and negative implies time in past
this function has a zero error of 30 seconds (i.e future and past is differentiated by a diff of 30 seconds)
*/
fun calculateTimeLeft(timeStampMilliSeconds : Long): Pair<Int, DateTimeDifferenceUnit> {

    val now = Clock.System.now()
    val difference = timeStampMilliSeconds - now.toEpochMilliseconds()

    if (difference < 0) {
        // time is in past
        return Pair(-1, PAST)
    } else {

        val days = TimeUnit.MILLISECONDS.toDays(difference).toInt()

        val years = days.div(365).toInt()

        val months = days.div(30).toInt()

        val hours = TimeUnit.MILLISECONDS.toHours(difference).toInt()
        val minutes = TimeUnit.MILLISECONDS.toMinutes(difference).toInt()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(difference).toInt() // todo reconsider this

        when {
            years > 0 -> {
                return Pair(years, YEARS)
            }
            months > 0 -> {
                return Pair(months, MONTHS)
            }
            days > 0 -> {
                return Pair(days, DAYS)
            }
            hours > 0 -> {
                return Pair(hours, HOURS)
            }
            minutes > 0 -> {
                return Pair(minutes, MINUTES)
            }
            seconds > 0 -> {
                return Pair(seconds, SECONDS)
            }
            else -> throw IllegalStateException("Something left to catch!")
        }
    }
}

enum class DateTimeDifferenceUnit(val unit: String) {
    PAST("past"),

    SECONDS("seconds"),
    MINUTES("minutes"),
    HOURS("hours"),
    DAYS("days"),
    MONTHS("months"),
    YEARS("years")
}