package jp.ventii.webfluxdemo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class FirebaseAuthenticationFilter : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authHeader = exchange.request.headers.getFirst("Authorization")

        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val authToken = authHeader.substring(7)
            Mono.fromFuture { apiFutureToCompletableFuture(FirebaseAuth.getInstance().verifyIdTokenAsync(authToken)) }
                .flatMap { decodedToken: FirebaseToken ->
                    val userDetails: UserDetails = User.withUsername(decodedToken.uid)
                        .password("")
                        .authorities(emptyList())
                        .build()
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, authToken, userDetails.authorities)
                    ReactiveSecurityContextHolder.getContext()
                        .map { context ->
                            context.authentication = authentication
                            context
                        }
                }
                .then(chain.filter(exchange))
        } else {
            chain.filter(exchange)
        }
    }

    private fun <T> apiFutureToCompletableFuture(apiFuture: com.google.api.core.ApiFuture<T>): java.util.concurrent.CompletableFuture<T> {
        val completableFuture = java.util.concurrent.CompletableFuture<T>()
        apiFuture.addListener(
            {
                try {
                    completableFuture.complete(apiFuture.get())
                } catch (e: Exception) {
                    completableFuture.completeExceptionally(e)
                }
            },
            Runnable::run
        )
        return completableFuture
    }
}
