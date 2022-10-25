package com.rarible.protocol.erc20.api.client.autoconfigure

import com.rarible.core.application.ApplicationEnvironmentInfo
import com.rarible.protocol.client.CompositeWebClientCustomizer
import com.rarible.protocol.client.DefaultProtocolWebClientCustomizer
import com.rarible.protocol.client.NoopWebClientCustomizer
import com.rarible.protocol.erc20.api.client.Erc20IndexerApiClientFactory
import com.rarible.protocol.erc20.api.client.Erc20IndexerApiServiceUriProvider
import com.rarible.protocol.erc20.api.client.K8sErc20IndexerApiServiceUriProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean

const val ERC20_INDEXER_WEB_CLIENT_CUSTOMIZER = "ERC20_INDEXER_WEB_CLIENT_CUSTOMIZER"

class Erc20IndexerApiClientAutoConfiguration(
    private val applicationEnvironmentInfo: ApplicationEnvironmentInfo
) {

    @Autowired(required = false)
    @Qualifier(ERC20_INDEXER_WEB_CLIENT_CUSTOMIZER)
    private var webClientCustomizer: WebClientCustomizer = NoopWebClientCustomizer()

    @Bean
    @ConditionalOnMissingBean(Erc20IndexerApiServiceUriProvider::class)
    fun erc20IndexerApiServiceUriProvider(): Erc20IndexerApiServiceUriProvider {
        return K8sErc20IndexerApiServiceUriProvider()
    }

    @Bean
    @ConditionalOnMissingBean(Erc20IndexerApiClientFactory::class)
    fun erc20IndexerApiClientFactory(
        @Value("\${rarible.core.client.name:}") clientName: String,
        erc20IndexerApiServiceUriProvider: Erc20IndexerApiServiceUriProvider
    ): Erc20IndexerApiClientFactory {
        val customizers = listOf(DefaultProtocolWebClientCustomizer(clientName), webClientCustomizer)
        val compositeWebClientCustomizer = CompositeWebClientCustomizer(customizers)
        return Erc20IndexerApiClientFactory(erc20IndexerApiServiceUriProvider, compositeWebClientCustomizer)
    }
}
