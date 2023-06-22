package com.rarible.protocol.order.api.client

import com.rarible.protocol.dto.OrderDto
import com.rarible.protocol.dto.OrderStateDto
import com.rarible.protocol.order.api.ApiClient
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import reactor.core.publisher.Mono

class AdminControllerApiTest {
    private val apiClient = mockk<ApiClient>()
    private val adminControllerApi = AdminControllerApi(apiClient)

    @Test
    @Suppress("ReactiveStreamsUnusedPublisher")
    fun `change state - ok`() = runBlocking<Unit> {
        val hash = "hash"
        val state = OrderStateDto(true)
        val orderDto = mockk<OrderDto>()

        every { apiClient.selectHeaderAccept(any()) } returns listOf(MediaType.APPLICATION_JSON)
        every { apiClient.selectHeaderContentType(any()) } returns MediaType.APPLICATION_JSON

        every {
            apiClient.invokeAPI<OrderDto>(
                 "/admin/order/orders/{hash}/state",
                HttpMethod.POST,
                any(),
                any(),
                state,
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Mono.just(orderDto)

        val result = adminControllerApi.changeState(hash, state)
        assertThat(result).isEqualTo(orderDto)
    }
}