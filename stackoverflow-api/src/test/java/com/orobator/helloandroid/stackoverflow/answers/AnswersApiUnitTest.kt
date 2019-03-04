package com.orobator.helloandroid.stackoverflow.answers

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
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class AnswersApiUnitTest : BaseApiTest() {
  private lateinit var answersRepository: AnswersRepositoryImpl

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

    val answersApi = retrofit.create(AnswersApi::class.java)
    answersRepository = AnswersRepositoryImpl(answersApi)
  }

  @Test
  fun getAnswersForQuestion() {
    server.enqueue(successfulGetAnswersResponse)

    val testObserver: TestObserver<AnswersResponse> =
      answersRepository
          .getAnswersForQuestion(42)
          .test()

    testObserver `completed with single value` AnswersResponse(
        listOf(
            Answer(
                User(
                    7563,
                    634336,
                    "registered",
                    null,
                    "https://www.gravatar.com/avatar/b3d25e1315c6c0952b28f9643735aa6e?s=128&d=identicon&r=PG",
                    "Tugrul Ates",
                    "https://stackoverflow.com/users/634336/tugrul-ates"
                ),
                false,
                3,
                1550363856,
                1550363856,
                54729053,
                54729015,
                "<p>No, this doesn't seem like a bad practice, without further context.</p>\n\n<p>Since the config values will not change and you're just making a copy of the individual config parameters you need, you're making the code a little bit more readable without introducing any downsides.</p>\n"
            ),
            Answer(
                User(
                    21142,
                    135769,
                    "registered",
                    82,
                    "https://i.stack.imgur.com/eQzEN.jpg?s=128&g=1",
                    "3Dave",
                    "https://stackoverflow.com/users/135769/3dave"
                ),
                true,
                4,
                1550363723,
                1550363723,
                54729040,
                54729015,
                "<p>The optimizer should clean this up, so I doubt there'd be a perf impact. The question is: does it make your code more readable and/or easier to maintain? Using temp variables in this way is pretty common. Unless you can come up with a <em>reason</em> that it's \"bad,\" go for it.</p>\n\n<p>Another thing to consider is that, by copying to a local variable and avoiding dereferencing the pointer every time, you're potentially speeding up access to the value in question. HOWEVER: that value is likely cached after the first access, and, again the Magical, Wonderful Optimizer will probably fix that. (But, as an engineer, you should never count on that - test and validate rather than assume.)</p>\n\n<p>In my opinion (cringe), you're fine. Go with it.</p>\n"
            )
        ),
        false,
        10000,
        9972
    )

    server `received request` ExpectedRequest(
        authorization = null,
        method = GET,
        contentType = null,
        path = "/2.2/questions/42/answers?order=desc&sort=activity&site=stackoverflow&filter=withbody",
        body = ""
    )
  }
}