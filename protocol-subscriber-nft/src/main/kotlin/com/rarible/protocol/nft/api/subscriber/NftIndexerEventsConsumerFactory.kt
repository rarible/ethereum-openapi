package com.rarible.protocol.nft.api.subscriber

import com.rarible.core.kafka.RaribleKafkaConsumer
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

    private fun createClientIdPrefix(blockchain: Blockchain): String {
        return "$environment.${blockchain.value}.$host.${UUID.randomUUID()}"
    }

    private fun getOffsetResetStrategy(): OffsetResetStrategy {
        return OffsetResetStrategy.EARLIEST
    }
}
