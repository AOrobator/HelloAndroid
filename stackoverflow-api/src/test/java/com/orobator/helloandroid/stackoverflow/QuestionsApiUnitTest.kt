package com.orobator.helloandroid.stackoverflow

import com.orobator.helloandroid.stackoverflow.questions.Question
import com.orobator.helloandroid.stackoverflow.questions.QuestionsApi
import com.orobator.helloandroid.stackoverflow.questions.QuestionsApi.Order
import com.orobator.helloandroid.stackoverflow.questions.QuestionsApi.Sort
import com.orobator.helloandroid.stackoverflow.questions.QuestionsDownloader
import com.orobator.helloandroid.stackoverflow.questions.QuestionsResponse
import com.orobator.helloandroid.stackoverflow.user.User
import com.orobator.mockwebserverassertions.BaseApiTest
import com.orobator.mockwebserverassertions.ExpectedRequest
import com.orobator.mockwebserverassertions.HttpMethod.GET
import com.orobator.mockwebserverassertions.`completed with single value`
import com.orobator.mockwebserverassertions.`received request`
import io.reactivex.observers.TestObserver
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class QuestionsApiUnitTest : BaseApiTest() {
  private lateinit var questionsDownloader: QuestionsDownloader

  @Before
  override fun setup() {
    super.setup()

    val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor { println(it) }.setLevel(Level.BODY))
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val questionsApi = retrofit.create(QuestionsApi::class.java)

    questionsDownloader = QuestionsDownloader(questionsApi)
  }

  @Test
  fun getQuestions() {
    server.enqueue(successfulGetQuestionsResponse)

    val testObserver: TestObserver<QuestionsResponse> =
      questionsDownloader
          .getQuestions(1, 3, Order.DESC, Sort.HOT)
          .test()

    testObserver `completed with single value` QuestionsResponse(
        listOf(
            Question(
                listOf("c", "pointers", "struct"),
                User(
                    20,
                    5101709,
                    "registered",
                    null,
                    "https://i.stack.imgur.com/fkAgH.png?s=128&g=1",
                    "Shwig",
                    "https://stackoverflow.com/users/5101709/shwig"
                ),
                true,
                16,
                2,
                54729040,
                2,
                1550364803,
                1550363442,
                1550364803,
                54729015,
                "https://stackoverflow.com/questions/54729015/is-it-bad-practice-to-store-a-struct-member-value-in-local-var-with-a-shorter-na",
                "Is it bad practice to store a struct member value in local var with a shorter name?"
            ),
            Question(
                listOf("mysql", "sql", "count", "where-clause"),
                User(
                    20,
                    590030,
                    "registered",
                    75,
                    "https://www.gravatar.com/avatar/bb11d9dd212e7310dc4d8e71228f2ec6?s=128&d=identicon&r=PG",
                    "sebrenner",
                    "https://stackoverflow.com/users/590030/sebrenner"
                ),
                true,
                23,
                1,
                null,
                1,
                1550363263,
                1550361755,
                1550363263,
                54728880,
                "https://stackoverflow.com/questions/54728880/how-to-select-rows-with-where-statement-and-count-1",
                "How to select rows with WHERE statement and COUNT(*) = 1"
            ),
            Question(
                listOf("java"),
                User(
                    1,
                    7965712,
                    "registered",
                    null,
                    "https://www.gravatar.com/avatar/3ceb9581210e765a7e4fefce81928fbc?s=128&d=identicon&r=PG&f=1",
                    "Dr. Bright",
                    "https://stackoverflow.com/users/7965712/dr-bright"
                ),
                false,
                28,
                4,
                null,
                -1,
                1550365222,
                1550363841,
                null,
                54729052,
                "https://stackoverflow.com/questions/54729052/how-to-check-if-time-in-millis-is-yesterday",
                "How to check if time in millis is yesterday"
            )
        ),
        true,
        10000,
        9995
    )

    server `received request` ExpectedRequest(
        authorization = null,
        method = GET,
        contentType = null,
        path = "/2.2/questions?page=1&pagesize=3&order=desc&sort=hot&site=stackoverflow",
        body = ""
    )
  }
}