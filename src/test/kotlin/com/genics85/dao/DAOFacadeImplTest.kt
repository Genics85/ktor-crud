package com.genics85.dao

import com.genics85.models.Article
import io.ktor.util.reflect.*
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.jemos.podam.api.PodamFactoryImpl
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DAOFacadeImplTest {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    private val factory: PodamFactoryImpl = PodamFactoryImpl()
    private lateinit var underTest:DAOFacadeImpl
    private lateinit var articles: List<Article>

    @BeforeAll
    fun setUp() {
        DatabaseFactory.connect()
        underTest=DAOFacadeImpl()
        articles= factory.manufacturePojoWithFullData(List::class.java,Article::class.java) as List<Article>
        articles.forEach{
            underTest.addNewArticle(it)
        }
    }

    @AfterAll
    fun tearDown() {
        transaction {
            SchemaUtils.drop()
        }
    }

    @Test
    fun allArticles() {
        //WHEN
        val expected=underTest.allArticles()
        //THEN
        assertThat(expected.size).isNotEqualTo(0)
        assertThat(expected.first()).instanceOf(Article::class)
        assertThat(expected.isNotEmpty())
    }
//
//    @Test
//    fun getArticle() {
//    }

    @Test
    fun addNewArticle() {
        //GIVEN
        val oneArticle=factory.manufacturePojoWithFullData(Article::class.java)
        //WHEN
        val expected=underTest.addNewArticle(oneArticle)
        //THEN
        assertThat(expected).isNotEqualTo(0)

    }

    @Test
    fun editArticle() {
        //GIVEN
        val list=underTest.allArticles()
        val oneArticle = list[Random.nextInt(1,list.size)]
        oneArticle.title= "Something new here"
        oneArticle.body="nice body for your bae"
        //WHEN
        val expected = oneArticle.id?.let { underTest.editArticle(it,oneArticle.body,oneArticle.title) }
        //THEN
        assertThat(expected).isGreaterThan(0)
    }
//
//    @Test
//    fun deleteArticle() {
//    }
}