package com.artronics.cashplay

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletResponse

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    println("kir")
    SpringApplication.run(Application::class.java, *args)
}

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
class SimpleCorsFilter : Filter {
    override fun doFilter(req: ServletRequest?, res: ServletResponse?, filterChain: FilterChain?) {
        val response = res as HttpServletResponse

        with(response) {
            setHeader("Access-Control-Allow-Origin", "*")
            setHeader("Access-Control-Allow-Credentials", "true")
            setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH")
            setHeader("Access-Control-Max-Age", "3600")
            setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Methods, GET,PUT,POST,DELETE,PATCH,OPTIONS, Origin,Authorization, X-Requested-With, Content-Type, Accept")
        }

        filterChain?.doFilter(req, res)
    }

    override fun init(p0: FilterConfig?) {
    }
    override fun destroy() {
    }
}
