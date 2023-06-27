package com.rarible.protocol.order.api.subscriber

import com.rarible.core.kafka.RaribleKafkaConsumer
import com.rarible.core.kafka.RaribleKafkaConsumerSettings
import com.rarible.core.kafka.json.JsonDeserializer
import com.rarible.ethereum.domain.Blockchain
import com.rarible.protocol.dto.AuctionEventDto
import com.rarible.protocol.dto.OrderEventDto
import com.rarible.protocol.dto.OrderIndexerTopicProvider
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import java.util.*

class OrderIndexerEventsConsumerFactory(
    private val brokerReplicaSet: String,
    private val host: String,
    private val environment: String
) {
    @Deprecated("Use createOrderEventsConsumerSettings instead")
    fun createOrderEventsConsumer(consumerGroup: String, blockchain: Blockchain): RaribleKafkaConsumer<OrderEventDto> {
        return RaribleKafkaConsumer(
            clientId = "${createClientIdPrefix(blockchain)}.order-indexer-order-events-consumer",
            valueDeserializerClass = JsonDeserializer::class.java,
            valueClass = OrderEventDto::class.java,
            consumerGroup = consumerGroup,
            defaultTopic = OrderIndexerTopicProvider.getOrderUpdateTopic(environment, blockchain.value),
            bootstrapServers = brokerReplicaSet,
            offsetResetStrategy = getOffsetResetStrategy()
        )
    }

    fun createOrderEventsKafkaConsumerSettings(
        group: String,
        concurrency: Int,
        batchSize: Int,
        blockchain: Blockchain
    ): RaribleKafkaConsumerSettings<OrderEventDto> {
        return createRaribleKafkaConsumerSettings(
            group = group,
            concurrency = concurrency,
            batchSize = batchSize,
            topic = { OrderIndexerTopicProvider.getOrderUpdateTopic(environment, blockchain.value) }
        )
    }

    @Deprecated("Use createAuctionEventsConsumerSettings instead")
    fun createAuctionEventsConsumer(
        consumerGroup: String,
        blockchain: Blockchain
    ): RaribleKafkaConsumer<AuctionEventDto> {
        return RaribleKafkaConsumer(
            clientId = "${createClientIdPrefix(blockchain)}.order-indexer-auction-events-consumer",
            valueDeserializerClass = JsonDeserializer::class.java,
            valueClass = AuctionEventDto::class.java,
            consumerGroup = consumerGroup,
            defaultTopic = OrderIndexerTopicProvider.getAuctionUpdateTopic(environment, blockchain.value),
            bootstrapServers = brokerReplicaSet,
            offsetResetStrategy = getOffsetResetStrategy()
        )
    }

    fun createAuctionEventsKafkaConsumerSettings(
        group: String,
        concurrency: Int,
        batchSize: Int,
        blockchain: Blockchain
    ): RaribleKafkaConsumerSettings<AuctionEventDto> {
        return createRaribleKafkaConsumerSettings(
            group = group,
            concurrency = concurrency,
            batchSize = batchSize,
            topic = { OrderIndexerTopicProvider.getAuctionUpdateTopic(environment, blockchain.value) }
        )
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
