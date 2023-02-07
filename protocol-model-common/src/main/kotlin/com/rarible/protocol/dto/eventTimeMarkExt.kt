package com.rarible.protocol.dto

import java.time.Instant

fun blockchainEventMark(markName: String, seconds: Long): EventTimeMarksDto {
    return blockchainEventMark(markName, Instant.ofEpochSecond(seconds))
}

fun blockchainEventMark(markName: String, date: Instant): EventTimeMarksDto {
    return EventTimeMarksDto("blockchain")
        .add("source", date)
        .add(markName, Instant.now())
}

fun integrationEventMark(markName: String, date: Instant): EventTimeMarksDto {
    return EventTimeMarksDto("integration")
        .add("source", date)
        .add(markName, Instant.now())
}

fun offchainEventMark(markName: String): EventTimeMarksDto {
    return EventTimeMarksDto("offchain").add(markName, null)
}

fun EventTimeMarksDto.add(name: String, date: Instant? = null): EventTimeMarksDto {
    val mark = EventTimeMarkDto(name, date ?: Instant.now())
    val marks = this.marks.toMutableList()
    marks.add(mark)
    return this.copy(marks = marks)
}