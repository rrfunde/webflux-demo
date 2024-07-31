package jp.ventii.webfluxdemo

import jp.ventii.webfluxdemo.models.Item
import jp.ventii.webfluxdemo.services.ItemService
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class ItemQueryResolver(
    private val itemService: ItemService
) {

    @QueryMapping
    fun getAllItems(): Flux<Item> = itemService.getAllItems()

    @QueryMapping
    fun getItemById(id: Long): Mono<Item> = itemService.getItemById(id)
}

@Controller
class HelloQueryResolver {

    @PreAuthorize("permitAll()")
    @QueryMapping
    fun hello(): String {
        return "Hello, world!"
    }
}