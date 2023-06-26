package com.rarible.protocol.order.api.client

import com.rarible.protocol.dto.OrderDto
import com.rarible.protocol.dto.OrderStateDto
import com.rarible.protocol.order.api.ApiClient
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.util.LinkedMultiValueMap

/**
 * This API is only accessible inside cloud cluster, and can't be call directly by external clients.
 */
class OrderAdminControllerApi(
    private val apiClient: ApiClient
) {
    suspend fun changeState(hash: String, state: OrderStateDto): OrderDto {
        val postBody: Any = state
        val pathParams = hashMapOf("hash" to hash)

        val localVarAccepts = arrayOf(
            "application/json"
        )
        val localVarAccept = apiClient.selectHeaderAccept(localVarAccepts)
        val localVarContentTypes = arrayOf<String>()
        val localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes)
        val localVarReturnType: ParameterizedTypeReference<OrderDto> = object : ParameterizedTypeReference<OrderDto>() {}

        return apiClient.invokeAPI(
            "/admin/order/orders/{hash}/state",
            HttpMethod.POST,
            pathParams.toMap(),
            LinkedMultiValueMap(),
            postBody,
            HttpHeaders(),
            LinkedMultiValueMap(),
            LinkedMultiValueMap(),
            localVarAccept,
            localVarContentType,
            arrayOf<String>(),
            localVarReturnType
        ).awaitSingle()
    }
}