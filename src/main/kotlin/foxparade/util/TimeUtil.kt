package foxparade.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TimeUtil {

    companion object {

        private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss")

        fun convertJstStringToUtcZonedDateTime(time: String): ZonedDateTime {
            val timeJst: ZonedDateTime = ZonedDateTime.of(LocalDateTime.parse(time, formatter), ZoneId.of("Asia/Tokyo"))
            return timeJst.toInstant().atZone(ZoneId.of("UTC"))
        }

    }
}