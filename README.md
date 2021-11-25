# Rarible Ethereum Protocol OpenAPI

![Languages](https://img.shields.io/github/languages/top/rarible/ethereum-openapi)
![License](https://img.shields.io/github/license/rarible/ethereum-openapi)

Indexers use Rarible Ethereum Protocol OpenAPI to describe APIs (and events). Clients (Kotlin, TypeScript, etc.) and server controller interfaces are generated automatically using YAML OpenAPI files.

Rarible Ethereum Protocol OpenAPI is a component of the [Rarible Protocol](https://github.com/rarible/union-service).

See more information in our [documentation](https://docs.rarible.org/ethereum/ethereum-overview).

## API Reference

Use these base URLs to access API on different Ethereum networks:

| Base URL | Network | Chain ID |
| :--- | :--- | :--- |
| [https://ethereum-api.rarible.org/v0.1/doc](https://ethereum-api.rarible.org/v0.1/doc) | Mainnet | 1 |
| [https://ethereum-api-staging.rarible.org/v0.1/doc](https://ethereum-api-staging.rarible.org/v0.1/doc) | Rinkeby | 4 |
| [https://ethereum-api-dev.rarible.org/v0.1/doc](https://ethereum-api-dev.rarible.org/v0.1/doc) | Ropsten | 3 |
| [https://ethereum-api-e2e.rarible.org/v0.1/doc](https://ethereum-api-e2e.rarible.org/v0.1/doc) | - | - |

## Backward compatibility

Be careful of backward compatibility:

- Do not change types of existing fields in an incompatible way. Unless properly coordinated with all possible clients.
- Do not add new *required* fields.
    - There may be Kafka queues, which are left from previous deploys, containing old objects not having this field.

      When Kafka consumers from those queues try to read the new model, they may fail with the "No such field" exception.

      This actually has happened to us when we made `NftItemDto.meta` to be a required field:
      > com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException: Instantiation of [simple type, class com.rarible.protocol.dto.NftItemDto] value failed for JSON property meta due to missing (therefore NULL) value for creator parameter meta which is a non-nullable type

    - How to overcome this:
        - *firstly*, deploy the application with *optional* type (as before) but always have a value
        - wait for some time to guarantee all Kafka queues are emptied
        - *secondly*, make the field as *required*
- Adding new model objects is backward compatible.
- Adding new *optional* fields is backward compatible.

## Suggestions

You are welcome to [suggest features](https://github.com/rarible/protocol/discussions) and [report bugs found](https://github.com/rarible/protocol/issues)!

## License

Rarible Ethereum Protocol OpenAPI (with generated clients) is available under [MIT License](LICENSE).





