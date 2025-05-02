package dev.asodesu.islandutils.api

private val TIME_REGEX = "(\\d+)([dhms])".toRegex()

/**
 * Gets the amount of seconds in Island's time strings, formatted:
 *
 * "3h 34m 19s"
 */
fun String.getTimeSeconds(): Long? {
    var time = 0L

    val findResult = TIME_REGEX.findAll(this)

    val iterator = findResult.iterator()
    if (!iterator.hasNext()) return null
    iterator.forEach {
        val value = it.groupValues[1].toIntOrNull() ?: return null

        when (it.groupValues[2]) {
            "d" -> time += value * 86400 // seconds in a day
            "h" -> time += value * 3600 // seconds in an hour
            "m" -> time += value * 60 // seconds in a minute
            "s" -> time += value
        }
    }

    return time
}