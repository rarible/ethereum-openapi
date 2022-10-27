package com.rarible.protocol.client

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.util.unit.DataSize
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration

class DefaultProtocolWebClientCustomizer(
    private val clientName: String?
) : WebClientCustomizer {

    override fun customize(webClientBuilder: WebClient.Builder) {
        webClientBuilder.codecs { clientCodecConfigurer ->
            clientCodecConfigurer.defaultCodecs().maxInMemorySize(DEFAULT_MAX_BODY_SIZE)
        }
        val provider = ConnectionProvider.builder("protocol-default-connection-provider")
            .maxConnections(500)
            .pendingAcquireMaxCount(-1)
            .maxIdleTime(DEFAULT_TIMEOUT)
            .maxLifeTime(DEFAULT_TIMEOUT)
            .lifo()
            .build()

        val client = HttpClient
            .create(provider)
            .responseTimeout(DEFAULT_TIMEOUT)
            .followRedirect(true)

        val connector = ReactorClientHttpConnector(client)

        webClientBuilder.clientConnector(connector)
        if (!clientName.isNullOrBlank()) {
            webClientBuilder.defaultHeader("x-rarible-client", clientName)
        }
    }

    companion object {
        val DEFAULT_MAX_BODY_SIZE = DataSize.ofMegabytes(10).toBytes().toInt()
        val DEFAULT_TIMEOUT: Duration = Duration.ofSeconds(30)
    }
}
