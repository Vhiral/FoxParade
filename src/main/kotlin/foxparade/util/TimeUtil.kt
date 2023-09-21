package foxparade.util

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class TimeUtil {

    companion object {

        private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss")

        fun convertJstStringToUtcZonedDateTime(time: String): ZonedDateTime {
            val timeJst: ZonedDateTime = ZonedDateTime.of(LocalDateTime.parse(time, formatter), ZoneId.of("Asia/Tokyo"))
            return timeJst.toInstant().atZone(ZoneId.of("UTC"))
        }

        fun isOnCooldown(lastPull: LocalDateTime, now: LocalDateTime, cooldownMs: Long): Boolean {
            return lastPull.isAfter(now.minus(cooldownMs, ChronoUnit.MILLIS))
        }

        fun getCooldownString(lastPull: LocalDateTime, now: LocalDateTime, cooldown: Long): String {
            val cooldownDifferenceMs: Long = cooldown - Duration.between(lastPull, now).toMillis()
            val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(cooldownDifferenceMs)
            val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(cooldownDifferenceMs) - TimeUnit.MINUTES.toSeconds(minutes)

            return if (minutes == 0L) "$seconds seconds." else "$minutes minutes $seconds seconds."
        }

    }
}