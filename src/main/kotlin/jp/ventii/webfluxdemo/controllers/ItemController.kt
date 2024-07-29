package jp.ventii.webfluxdemo.controllers

import jp.ventii.webfluxdemo.models.Item
import jp.ventii.webfluxdemo.repositories.ItemRepository
import jp.ventii.webfluxdemo.services.ItemService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@RestController
@RequestMapping("/api/items")
class ItemController(private val itemService: ItemService) {
    @GetMapping
    fun getAllItems(): Flux<Item> = itemService.getAllItems()

    @GetMapping("/{id}")
    fun getItemById(@PathVariable id: Long): Mono<Item> = itemService.getItemById(id)

    @PostMapping
    fun createItem(@RequestBody item: Item): Mono<Item> = itemService.createItem(item)

    @PutMapping("/{id}")
    fun updateItem(@PathVariable id: Long, @RequestBody item: Item): Mono<Item> = itemService.updateItem(id, item)

    @DeleteMapping("/{id}")
    fun deleteItem(@PathVariable id: Long): Mono<Void> = itemService.deleteItem(id)
}