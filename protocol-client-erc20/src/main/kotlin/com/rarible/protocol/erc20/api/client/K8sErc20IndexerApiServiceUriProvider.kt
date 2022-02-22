package com.rarible.protocol.erc20.api.client

import java.net.URI

class K8sErc20IndexerApiServiceUriProvider : Erc20IndexerApiServiceUriProvider {

    override fun getUri(blockchain: String): URI {
        return URI.create(String.format("http://%s-erc20-api:8080", blockchain))
    }
}
