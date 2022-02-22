package com.rarible.protocol.nftorder.api.client

import java.net.URI

class K8sNftOrderApiServiceUriProvider : NftOrderApiServiceUriProvider {

    override fun getUri(blockchain: String): URI {
        return URI.create(String.format("http://%s-nft-order-api:8080", blockchain))
    }
}