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
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun Instant.getCalendarFormattedDate(): String {
    val localDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    val month = localDateTime.month.name.take(3)
    return "${localDateTime.dayOfMonth}th ${month}, ${localDateTime.year}"
}

fun getFormattedDate(timeStampMilliSeconds: Long, format: String = "EEE, MMM d, ''yy"): String {
    return SimpleDateFormat(format, Locale.ENGLISH).format(Date(timeStampMilliSeconds))
}

fun getFormattedTime(timeStampMilliSeconds: Long): String {
    return SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(Date(timeStampMilliSeconds))
}


/*
returns time left in [Int] and its unit in [String] (seconds, minutes, hours, days, months, years)
positive int implies future time and negative implies time in past
this function has a zero error of 30 seconds (i.e future and past is differentiated by a diff of 30 seconds)
*/
fun calculateTimeLeft(timeStampMilliSeconds: Long): Pair<Int, DateTimeDifferenceUnit> {

    val now = Clock.System.now()
    val difference = timeStampMilliSeconds - now.toEpochMilliseconds()

    if (difference < 0) {
        // time is in past
        return Pair(-1, PAST)
    } else {

        val days = TimeUnit.MILLISECONDS.toDays(difference).toInt()

        val years = days.div(365)

        val months = days.div(30)

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

// returns updated timestamp in milliseconds
fun updateDate(timeStampMilliSeconds: Long, dateStamp: Long): Long {

    val newDate = Instant.fromEpochMilliseconds(dateStamp)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date

    val calendar = Calendar.getInstance(java.util.TimeZone.getDefault())
    calendar.time = Date(timeStampMilliSeconds)
    calendar.set(Calendar.DAY_OF_MONTH, newDate.dayOfMonth)
    calendar.set(Calendar.MONTH, newDate.monthNumber - 1)
    calendar.set(Calendar.YEAR, newDate.year)

    return calendar.timeInMillis
}

// returns updated timestamp in milliseconds
fun updateTime(timeStampMilliSeconds: Long, hour: Int, minute: Int): Long {

    val calendar = Calendar.getInstance(java.util.TimeZone.getDefault())
    calendar.time = Date(timeStampMilliSeconds)
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)

    return calendar.timeInMillis
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