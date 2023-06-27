package com.rarible.protocol.nft.api.subscriber

import com.rarible.core.kafka.RaribleKafkaConsumer
import com.rarible.core.kafka.RaribleKafkaConsumerSettings
import com.rarible.core.kafka.json.JsonDeserializer
import com.rarible.ethereum.domain.Blockchain
import com.rarible.protocol.dto.NftCollectionEventDto
import com.rarible.protocol.dto.NftCollectionEventTopicProvider
import com.rarible.protocol.dto.NftItemEventDto
import com.rarible.protocol.dto.NftItemEventTopicProvider
import com.rarible.protocol.dto.NftOwnershipEventDto
import com.rarible.protocol.dto.NftOwnershipEventTopicProvider
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import java.util.*

class NftIndexerEventsConsumerFactory(
    private val brokerReplicaSet: String,
    private val host: String,
    private val environment: String
) {
    @Deprecated("Use createCollectionEventsKafkaConsumerSettings instead")
    fun createCollectionEventsConsumer(
        consumerGroup: String,
        blockchain: Blockchain
    ): RaribleKafkaConsumer<NftCollectionEventDto> {
        return RaribleKafkaConsumer(
            clientId = "${createClientIdPrefix(blockchain)}.nft-indexer-collection-events-consumer",
            valueDeserializerClass = JsonDeserializer::class.java,
            valueClass = NftCollectionEventDto::class.java,
            consumerGroup = consumerGroup,
            defaultTopic = NftCollectionEventTopicProvider.getTopic(environment, blockchain.value),
            bootstrapServers = brokerReplicaSet,
            offsetResetStrategy = getOffsetResetStrategy()
        )
    }

    fun createCollectionEventsKafkaConsumerSettings(
        consumerGroup: String,
        concurrency: Int,
        batchSize: Int,
        blockchain: Blockchain,
    ): RaribleKafkaConsumerSettings<NftCollectionEventDto> {
        return createRaribleKafkaConsumerSettings(
            group = consumerGroup,
            concurrency = concurrency,
            batchSize = batchSize
        ) { NftCollectionEventTopicProvider.getTopic(environment, blockchain.value) }
    }

    @Deprecated("Use createItemEventsKafkaConsumerSettings instead")
    fun createItemEventsConsumer(consumerGroup: String, blockchain: Blockchain): RaribleKafkaConsumer<NftItemEventDto> {
        return RaribleKafkaConsumer(
            clientId = "${createClientIdPrefix(blockchain)}.nft-indexer-item-events-consumer",
            valueDeserializerClass = JsonDeserializer::class.java,
            valueClass = NftItemEventDto::class.java,
            consumerGroup = consumerGroup,
            defaultTopic = NftItemEventTopicProvider.getTopic(environment, blockchain.value),
            bootstrapServers = brokerReplicaSet,
            offsetResetStrategy = getOffsetResetStrategy()
        )
    }

    fun createItemEventsKafkaConsumerSettings(
        consumerGroup: String,
        concurrency: Int,
        batchSize: Int,
        blockchain: Blockchain
    ): RaribleKafkaConsumerSettings<NftItemEventDto> {
        return createRaribleKafkaConsumerSettings(
            group = consumerGroup,
            concurrency = concurrency,
            batchSize = batchSize
        ) { NftItemEventTopicProvider.getTopic(environment, blockchain.value) }
    }

    @Deprecated("Use createOwnershipEventsKafkaConsumerSettings instead")
    fun createOwnershipEventsConsumer(
        consumerGroup: String,
        blockchain: Blockchain
    ): RaribleKafkaConsumer<NftOwnershipEventDto> {
        return RaribleKafkaConsumer(
            clientId = "${createClientIdPrefix(blockchain)}.nft-indexer-ownership-events-consumer",
            valueDeserializerClass = JsonDeserializer::class.java,
            valueClass = NftOwnershipEventDto::class.java,
            consumerGroup = consumerGroup,
            defaultTopic = NftOwnershipEventTopicProvider.getTopic(environment, blockchain.value),
            bootstrapServers = brokerReplicaSet,
            offsetResetStrategy = getOffsetResetStrategy()
        )
    }

    fun createOwnershipEventsKafkaConsumerSettings(
        consumerGroup: String,
        concurrency: Int,
        batchSize: Int,
        blockchain: Blockchain
    ): RaribleKafkaConsumerSettings<NftOwnershipEventDto> {
        return createRaribleKafkaConsumerSettings(
            group = consumerGroup,
            concurrency = concurrency,
            batchSize = batchSize
        ) { NftOwnershipEventTopicProvider.getTopic(environment, blockchain.value) }
    }

    private inline fun <reified T> createRaribleKafkaConsumerSettings(
        group: String,
        concurrency: Int,
        batchSize: Int,
        topic: () -> String
    ): RaribleKafkaConsumerSettings<T> {
        return RaribleKafkaConsumerSettings(
            hosts = host,
            topic = topic(),
            valueClass = T::class.java,
            group = group,
            concurrency = concurrency,
            batchSize = batchSize,
            offsetResetStrategy = getOffsetResetStrategy()
        )
    }

    private fun createClientIdPrefix(blockchain: Blockchain): String {
        return "$environment.${blockchain.value}.$host.${UUID.randomUUID()}"
    }

    private fun getOffsetResetStrategy(): OffsetResetStrategy {
        return OffsetResetStrategy.EARLIEST
    }
}
