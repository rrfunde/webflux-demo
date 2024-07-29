package jp.ventii.webfluxdemo.repositories

import jp.ventii.webfluxdemo.models.Item
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : ReactiveCrudRepository<Item, Long>
