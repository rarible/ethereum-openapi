package com.rarible.protocol.unlockable.api.client.autoconfigure

import com.rarible.core.application.ApplicationEnvironmentInfo
import com.rarible.protocol.client.CompositeWebClientCustomizer
import com.rarible.protocol.client.DefaultProtocolWebClientCustomizer
import com.rarible.protocol.client.NoopWebClientCustomizer
import com.rarible.protocol.unlockable.api.client.K8sUnlockableApiServiceUriProvider
import com.rarible.protocol.unlockable.api.client.UnlockableApiClientFactory
import com.rarible.protocol.unlockable.api.client.UnlockableApiServiceUriProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean

const val UNLOCKABLE_WEB_CLIENT_CUSTOMIZER = "UNLOCKABLE_WEB_CLIENT_CUSTOMIZER"

class UnlockableApiClientAutoConfiguration(
    private val applicationEnvironmentInfo: ApplicationEnvironmentInfo
) {

    @Autowired(required = false)
    @Qualifier(UNLOCKABLE_WEB_CLIENT_CUSTOMIZER)
    private var webClientCustomizer: WebClientCustomizer = NoopWebClientCustomizer()

    @Bean
    @ConditionalOnMissingBean(UnlockableApiServiceUriProvider::class)
    fun unlockableApiServiceUriProvider(): UnlockableApiServiceUriProvider {
        return K8sUnlockableApiServiceUriProvider()
    }

    @Bean
    @ConditionalOnMissingBean(UnlockableApiClientFactory::class)
    fun unlockableApiClientFactory(
        @Value("\${rarible.core.client.name:}") clientName: String,
        unlockableApiServiceUriProvider: UnlockableApiServiceUriProvider
    ): UnlockableApiClientFactory {
        val customizers = listOf(DefaultProtocolWebClientCustomizer(clientName), webClientCustomizer)
        val compositeWebClientCustomizer = CompositeWebClientCustomizer(customizers)
        return UnlockableApiClientFactory(unlockableApiServiceUriProvider, compositeWebClientCustomizer)
    }
}