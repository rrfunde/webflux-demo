package jp.ventii.webfluxdemo.services


import jp.ventii.webfluxdemo.models.Item
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class WebClientService(private val webClient: WebClient) {

    private val logger = LoggerFactory.getLogger(WebClientService::class.java)

    fun getAllItems(): Flux<Item> {
        return webClient.get()
            .uri("/items")
            .retrieve()
            .onStatus({ status -> status.is4xxClientError || status.is5xxServerError }) { clientResponse ->
                logger.error("Error response status: ${clientResponse.statusCode()}")
                clientResponse.bodyToMono(String::class.java).flatMap { body ->
                    logger.error("Error response body: $body")
                    Mono.error(RuntimeException("Error response: $body"))
                }
            }
            .bodyToFlux(Item::class.java)
            .doOnError { e -> logger.error("Exception occurred while fetching items", e) }
    }

    fun getItemById(id: Long): Mono<Item> {
        return webClient.get()
            .uri("/items/{id}", id)
            .retrieve()
            .bodyToMono(Item::class.java)
    }

    fun createItem(item: Item): Mono<Item> {
        return webClient.post()
            .uri("/items")
            .bodyValue(item)
            .retrieve()
            .bodyToMono(Item::class.java)
    }

    fun updateItem(id: Long, item: Item): Mono<Item> {
        return webClient.put()
            .uri("/items/{id}", id)
            .bodyValue(item)
            .retrieve()
            .bodyToMono(Item::class.java)
    }

    fun deleteItem(id: Long): Mono<Void> {
        return webClient.delete()
            .uri("/items/{id}", id)
            .retrieve()
            .bodyToMono(Void::class.java)
    }
}
