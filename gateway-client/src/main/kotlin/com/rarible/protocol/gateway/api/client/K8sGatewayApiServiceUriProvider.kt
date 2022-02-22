package com.rarible.protocol.gateway.client

import java.net.URI

class K8sGatewayApiServiceUriProvider : GatewayApiServiceUriProvider {

    override fun getUri(blockchain: String): URI {
        return URI.create(String.format("http://%s-gateway:8080", blockchain))
    }
}
