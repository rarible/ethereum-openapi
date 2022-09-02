package com.rarible.protocol.order.api.client

import java.net.URI

class K8sOrderIndexerApiServiceUriProvider(
    private val environment: String
) : OrderIndexerApiServiceUriProvider {

    override fun getUri(blockchain: String): URI {
        return URI.create(String.format("http://%s-order-api.%s-protocol:8080", blockchain, environment))
    }

}
