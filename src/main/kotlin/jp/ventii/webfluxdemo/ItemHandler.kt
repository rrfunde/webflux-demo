package jp.ventii.webfluxdemo

import jp.ventii.webfluxdemo.models.Item
import jp.ventii.webfluxdemo.services.ItemService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ItemHandler(
    private val itemService: ItemService
) {

    fun getAllItems(request: ServerRequest): Mono<ServerResponse> =
        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(itemService.getAllItems(), Item::class.java)

    fun getItemById(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id").toLong()
        return itemService.getItemById(id)
            .flatMap { item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(item) }
            .switchIfEmpty(ServerResponse.notFound().build())
    }

    fun createItem(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(Item::class.java)
            .flatMap { item ->
                if (item.isValid()) {
                    itemService.createItem(item)
                        .flatMap { createdItem ->
                            ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(createdItem)
                        }
                } else {
                    ServerResponse.badRequest().bodyValue("Invalid item data")
                }
            }
            .switchIfEmpty(ServerResponse.badRequest().bodyValue("Request body is missing"))
    fun updateItem(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id").toLong()
        return request.bodyToMono(Item::class.java)
            .flatMap { item -> itemService.updateItem(id, item) }
            .flatMap { item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(item) }
            .switchIfEmpty(ServerResponse.notFound().build())
    }

    fun deleteItem(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id").toLong()
        return itemService.deleteItem(id)
            .flatMap { ServerResponse.noContent().build() }
            .switchIfEmpty(ServerResponse.notFound().build())
    }
}
