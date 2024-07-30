package jp.ventii.webfluxdemo

import jp.ventii.webfluxdemo.models.Item
import jp.ventii.webfluxdemo.services.ItemService
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class ItemMutationResolver(
    private val itemService: ItemService
) {

    @MutationMapping
    fun createItem(name: String, description: String): Mono<Item> {
        val item = Item(name = name, description = description, price = 100.1, quantity = 1)
        return itemService.createItem(item)
    }

    @MutationMapping
    fun updateItem(id: Long, name: String, description: String): Mono<Item> {
        val item = Item(id = id, name = name, description = description, price = 100.1, quantity = 1)
        return itemService.updateItem(id, item)
    }

    @MutationMapping
    fun deleteItem(id: Long): Mono<Boolean> {
        return itemService.deleteItem(id).then(Mono.just(true)).onErrorReturn(false)
    }
}
