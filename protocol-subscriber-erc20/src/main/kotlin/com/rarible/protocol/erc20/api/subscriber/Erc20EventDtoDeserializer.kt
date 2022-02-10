package com.rarible.protocol.erc20.api.subscriber

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.rarible.protocol.dto.Erc20BalanceEventDto
import org.apache.kafka.common.serialization.Deserializer

class Erc20EventDtoDeserializer : Deserializer<Erc20BalanceEventDto> {

    private val mapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())

    override fun deserialize(topic: String, data: ByteArray): Erc20BalanceEventDto {
        return mapper.readValue(data)
    }
}

