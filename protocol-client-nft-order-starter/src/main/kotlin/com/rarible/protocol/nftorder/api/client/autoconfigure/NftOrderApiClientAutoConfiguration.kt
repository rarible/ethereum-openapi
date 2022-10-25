package com.rarible.protocol.nftorder.api.client.autoconfigure

import com.rarible.core.application.ApplicationEnvironmentInfo
import com.rarible.protocol.client.CompositeWebClientCustomizer
import com.rarible.protocol.client.DefaultProtocolWebClientCustomizer
import com.rarible.protocol.client.NoopWebClientCustomizer
import com.rarible.protocol.nftorder.api.client.K8sNftOrderApiServiceUriProvider
import com.rarible.protocol.nftorder.api.client.NftOrderApiClientFactory
import com.rarible.protocol.nftorder.api.client.NftOrderApiServiceUriProvider
import com.rarible.protocol.nftorder.api.client.SwarmNftOrderApiServiceUriProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean

const val NFT_ORDER_WEB_CLIENT_CUSTOMIZER = "NFT_ORDER_WEB_CLIENT_CUSTOMIZER"

class NftOrderApiClientAutoConfiguration(
    private val applicationEnvironmentInfo: ApplicationEnvironmentInfo
) {

    @Autowired(required = false)
    @Qualifier(NFT_ORDER_WEB_CLIENT_CUSTOMIZER)
    private var webClientCustomizer: WebClientCustomizer = NoopWebClientCustomizer()

    @Bean
    @ConditionalOnMissingBean(NftOrderApiServiceUriProvider::class)
    fun nftOrderApiServiceUriProvider(
        @Value("\${rarible.core.client.k8s:false}") k8s: Boolean
    ): NftOrderApiServiceUriProvider {
        return if (k8s)
            K8sNftOrderApiServiceUriProvider()
        else
            SwarmNftOrderApiServiceUriProvider(applicationEnvironmentInfo.name)
    }

    @Bean
    @ConditionalOnMissingBean(NftOrderApiClientFactory::class)
    fun nftOrderApiClientFactory(
        @Value("\${rarible.core.client.name:}") clientName: String,
        nftOrderApiServiceUriProvider: NftOrderApiServiceUriProvider
    ): NftOrderApiClientFactory {
        val customizers = listOf(DefaultProtocolWebClientCustomizer(clientName), webClientCustomizer)
        val compositeWebClientCustomizer = CompositeWebClientCustomizer(customizers)
        return NftOrderApiClientFactory(nftOrderApiServiceUriProvider, compositeWebClientCustomizer)
    }
}