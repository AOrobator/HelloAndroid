package com.orobator.helloandroid.lesson7.model.api

import com.orobator.helloandroid.lesson7.`completed with single value`
import com.orobator.helloandroid.numbers.api.NumbersApi
import com.orobator.helloandroid.numbers.api.NumbersRepositoryImpl
import com.orobator.helloandroid.numbers.model.NumberFact
import com.orobator.mockwebserverassertions.BaseApiTest
import com.orobator.mockwebserverassertions.ExpectedRequest
import com.orobator.mockwebserverassertions.HttpMethod.GET
import com.orobator.mockwebserverassertions.`received request`
import io.reactivex.observers.TestObserver
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NumberRepositoryUnitTest : BaseApiTest() {
  lateinit var numbersRepository: NumbersRepositoryImpl

  override fun setup() {
    super.setup()

    val client = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor(::println)
                .setLevel(Level.BODY)
        )
        .build()

    val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val numbersApi: NumbersApi = retrofit.create(NumbersApi::class.java)
    numbersRepository = NumbersRepositoryImpl(numbersApi)
  }

  @Test
  fun getTriviaFact() {
    server.enqueue(triviaNumberFactResponse)

    val testObserver: TestObserver<NumberFact> =
      numbersRepository
          .getTriviaFact(2)
          .test()

    val expectedFact = NumberFact(
        "2 is the first magic number in physics.",
        2,
        true,
        "trivia"
    )

    testObserver `completed with single value` expectedFact
    server `received request` ExpectedRequest(
        authorization = null,
        method = GET,
        contentType = null,
        path = "/2/trivia?json",
        body = ""
    )
  }
}