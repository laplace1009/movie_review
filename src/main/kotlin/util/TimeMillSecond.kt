package util

enum class TimeMillSecond(val millis: Long) {
    SECOND(1000L),
    MINUTE(60L * 1000L),
    HOUR(60L * 60L * 1000L),
    DAY(24L * 60L * 60L * 1000L)
}