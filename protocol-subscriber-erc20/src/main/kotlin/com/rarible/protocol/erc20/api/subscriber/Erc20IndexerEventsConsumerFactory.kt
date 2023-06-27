package com.rarible.protocol.erc20.api.subscriber

import com.rarible.core.kafka.RaribleKafkaConsumerSettings
import com.rarible.ethereum.domain.Blockchain
import com.rarible.protocol.dto.Erc20BalanceEventDto
import com.rarible.protocol.dto.Erc20BalanceEventTopicProvider
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import java.util.*

class Erc20IndexerEventsConsumerFactory(
    private val brokerReplicaSet: String,
    private val host: String,
    private val environment: String
) {
    @Deprecated("Use createErc20BalanceEventsKafkaConsumerSettings instead")
    fun createErc20BalanceEventsConsumer(
        consumerGroup: String,
        blockchain: Blockchain
    ): Erc20KafkaConsumerArguments<Erc20BalanceEventDto> {
        return Erc20KafkaConsumerArguments(
            clientId = "${createClientIdPrefix(blockchain)}.erc20-indexer-balance-events-consumer",
            valueDeserializerClass = Erc20EventDtoDeserializer::class.java,
            consumerGroup = consumerGroup,
            defaultTopic = Erc20BalanceEventTopicProvider.getTopic(environment, blockchain.value),
            bootstrapServers = brokerReplicaSet,
            offsetResetStrategy = getOffsetResetStrategy()
        )
    }

    fun createErc20BalanceEventsKafkaConsumerSettings(
        group: String,
        concurrency: Int,
        batchSize: Int,
        blockchain: Blockchain
    ): RaribleKafkaConsumerSettings<Erc20BalanceEventDto> {
        return RaribleKafkaConsumerSettings(
            hosts = host,
            topic = Erc20BalanceEventTopicProvider.getTopic(environment, blockchain.value),
            valueClass = Erc20BalanceEventDto::class.java,
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
