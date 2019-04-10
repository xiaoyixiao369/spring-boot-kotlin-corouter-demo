package io.github.igordonxiao.corouter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.coRouter

@SpringBootApplication
class CorouterApplication

fun main(args: Array<String>) {
    runApplication<CorouterApplication>(*args)
}


@Configuration
class RouteConfigurations {

    @Bean
    fun mainRouter(userHandler: UserHandler) = coRouter {
        GET("/users", userHandler::all)
        GET("/users/{name}", userHandler::findByName)
    }
}

@Component
class UserHandler {

    private val users = setOf(
            User("gordon", 26), User("cassy", 20)
    )

    suspend fun all(request: ServerRequest) = ServerResponse.ok().bodyAndAwait(users)


    suspend fun findByName(request: ServerRequest) = ServerResponse.ok().bodyAndAwait(users.find { it.name.equals(request.pathVariable("name")) }
            ?: throw UserException.NotFound)

}

object UserException {
    object NotFound : RuntimeException("user not found")

}


data class User(val name: String, val age: Int)
