package com.genics85.plugins

import com.genics85.controllers.ArticleControllerImpl
import com.genics85.dao.DAOFacadeImpl
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.slf4j.LoggerFactory

private const val BASE_URL:String="/API"
fun Application.dbRouting() {

    val di= DI{
        bindSingleton{DAOFacadeImpl()}
    }

    var dbFun = ArticleControllerImpl(di)

    routing {
        route(BASE_URL){
            get("/ping"){
                call.respond("pong")
            }
            post ("/add"){
                var news = dbFun.createArticle("Something great","This is not even a new anymore")
                call.respond(news.message)
            }
            get("/get"){
                var articles=dbFun.getArticles()
                call.respond(articles.data.first().toString())
            }
            post("/del"){
                var art=dbFun.deleteArticle(1)
                call.respond(art.message)
            }
            post("/edit"){
                var art=dbFun.editArticle(1,"Changed to this one","Something nice for your pleasure")
                call.respond(art.message)
            }
        }
    }
}