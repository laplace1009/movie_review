package util

enum class TimeUnit(val unit: String) {
    SECONDS("sec"),
    MINUTES("min"),
    HOURS("hour"),
    DAYS("day");

    companion object {
        fun fromString(value: String): TimeUnit? {
            return entries.firstOrNull { value.endsWith(it.unit) }
        }
    }
}