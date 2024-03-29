package com.rarible.protocol.nft.api.client.autoconfigure

import com.rarible.core.application.ApplicationEnvironmentInfo
import com.rarible.protocol.client.CompositeWebClientCustomizer
import com.rarible.protocol.client.DefaultProtocolWebClientCustomizer
import com.rarible.protocol.client.NoopWebClientCustomizer
import com.rarible.protocol.nft.api.client.K8sNftIndexerApiServiceUriProvider
import com.rarible.protocol.nft.api.client.NftIndexerApiClientFactory
import com.rarible.protocol.nft.api.client.NftIndexerApiServiceUriProvider
import com.rarible.protocol.nft.api.client.SwarmNftIndexerApiServiceUriProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean

const val NFT_INDEXER_WEB_CLIENT_CUSTOMIZER = "NFT_INDEXER_WEB_CLIENT_CUSTOMIZER"

class NftIndexerApiClientAutoConfiguration(
    private val applicationEnvironmentInfo: ApplicationEnvironmentInfo
) {

    @Autowired(required = false)
    @Qualifier(NFT_INDEXER_WEB_CLIENT_CUSTOMIZER)
    private var webClientCustomizer: WebClientCustomizer = NoopWebClientCustomizer()

    @Bean
    @ConditionalOnMissingBean(NftIndexerApiServiceUriProvider::class)
    fun nftIndexerApiServiceUriProvider(
        @Value("\${rarible.core.client.k8s:true}") k8s: Boolean
    ): NftIndexerApiServiceUriProvider {
        return if (k8s) {
            K8sNftIndexerApiServiceUriProvider()
        } else {
            SwarmNftIndexerApiServiceUriProvider(applicationEnvironmentInfo.name)
        }
    }

    @Bean
    @ConditionalOnMissingBean(NftIndexerApiClientFactory::class)
    fun nftIndexerApiClientFactory(
        @Value("\${rarible.core.client.name:}") clientName: String,
        nftIndexerApiServiceUriProvider: NftIndexerApiServiceUriProvider
    ): NftIndexerApiClientFactory {
        val customizers = listOf(DefaultProtocolWebClientCustomizer(clientName), webClientCustomizer)
        val compositeWebClientCustomizer = CompositeWebClientCustomizer(customizers)
        return NftIndexerApiClientFactory(nftIndexerApiServiceUriProvider, compositeWebClientCustomizer)
    }
}