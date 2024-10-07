package utils

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

fun truncateToMinute(instant: Instant): Instant = instant.truncatedTo(ChronoUnit.MINUTES)

fun plusOneMinute(instant: Instant) : Instant = instant.plus(1, ChronoUnit.MINUTES)

fun minusMinutes(instant: Instant, minutes: Long) : Instant = instant.minus(minutes, ChronoUnit.MINUTES)
