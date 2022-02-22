package com.rarible.protocol.unlockable.api.client

import java.net.URI

class K8sUnlockableApiServiceUriProvider : UnlockableApiServiceUriProvider {

    override fun getUri(blockchain: String): URI {
        return URI.create(String.format("http://%s-unlockable-api:8080", blockchain))
    }

}
