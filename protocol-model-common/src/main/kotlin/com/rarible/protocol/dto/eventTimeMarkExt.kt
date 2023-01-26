package com.rarible.protocol.dto

import java.time.Instant

fun indexerEventMark(): EventTimeMarksDto {
    return EventTimeMarksDto(null, Instant.now())
}

fun transitiveEventMark(source: SourceEventTimeMarkDto?): EventTimeMarksDto {
    return EventTimeMarksDto(source, Instant.now())
}

fun blockchainEventMark(seconds: Long?): EventTimeMarksDto {
    return blockchainEventMark(seconds?.let { Instant.ofEpochSecond(seconds) })
}

fun blockchainEventMark(date: Instant?): EventTimeMarksDto {
    return EventTimeMarksDto(
        source = date?.let { SourceEventTimeMarkDto(SourceEventTimeMarkDto.Type.BLOCKCHAIN, date) },
        indexer = Instant.now()
    )
}

fun integrationEventMark(date: Instant?): EventTimeMarksDto {
    return EventTimeMarksDto(
        source = date?.let { SourceEventTimeMarkDto(SourceEventTimeMarkDto.Type.INTEGRATION, date) },
        indexer = Instant.now()
    )
}