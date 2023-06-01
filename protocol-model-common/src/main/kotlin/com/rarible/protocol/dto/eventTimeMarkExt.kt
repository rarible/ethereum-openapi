package com.rarible.protocol.dto

import com.rarible.core.common.EventTimeMark
import com.rarible.core.common.EventTimeMarks

fun EventTimeMarksDto.toModel(): EventTimeMarks {
    return EventTimeMarks(this.source, this.marks.map { EventTimeMark(it.name, it.date) })
}