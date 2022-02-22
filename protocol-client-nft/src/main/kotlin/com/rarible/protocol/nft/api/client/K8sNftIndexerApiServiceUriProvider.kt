package com.rarible.protocol.nft.api.client

import java.net.URI

class K8sNftIndexerApiServiceUriProvider : NftIndexerApiServiceUriProvider {

    override fun getUri(blockchain: String): URI {
        return URI.create(String.format("http://%s-nft-api:8080", blockchain))
    }

}
