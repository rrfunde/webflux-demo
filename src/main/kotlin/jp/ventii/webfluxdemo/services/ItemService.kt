package jp.ventii.webfluxdemo.services

import jp.ventii.webfluxdemo.models.Item
import jp.ventii.webfluxdemo.repositories.ItemRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ItemService(private val itemRepository: ItemRepository) {
    fun getAllItems(): Flux<Item> = itemRepository.findAll()
    fun getItemById(id: Long): Mono<Item> = itemRepository.findById(id)
    fun createItem(item: Item): Mono<Item> = itemRepository.save(item)
    fun updateItem(id: Long, item: Item): Mono<Item> = itemRepository.findById(id)
        .flatMap { itemRepository.save(item.copy(id = it.id)) }
    fun deleteItem(id: Long): Mono<Void> = itemRepository.deleteById(id)
}