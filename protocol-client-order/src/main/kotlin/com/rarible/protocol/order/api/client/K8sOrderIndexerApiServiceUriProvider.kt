package com.rarible.protocol.order.api.client

import java.net.URI

class K8sOrderIndexerApiServiceUriProvider : OrderIndexerApiServiceUriProvider {

    override fun getUri(blockchain: String): URI {
        return URI.create(String.format("http://%s-order-api:8080", blockchain))
    }

}
