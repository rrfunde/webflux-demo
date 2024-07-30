package jp.ventii.webfluxdemo.configs

import jp.ventii.webfluxdemo.ItemHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class RouterConfig(private val itemHandler: ItemHandler) {

    @Bean
    fun routerFunction(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            .path("/api/items") { builder ->
                builder
                    .GET("", itemHandler::getAllItems)
                    .GET("/{id}", itemHandler::getItemById)
                    .POST("", itemHandler::createItem)
                    .PUT("/{id}", itemHandler::updateItem)
                    .DELETE("/{id}", itemHandler::deleteItem)
            }
            .build()
    }
}
