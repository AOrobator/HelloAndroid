package com.orobator.mockwebserverassertions

import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

abstract class BaseApiTest {
  protected lateinit var server: MockWebServer
    private set

  /**
   * The server's URL. You'll need this to make HTTP requests.
   */
  protected val baseUrl: String
    get() = server.url("/").toString()

  @Before
  open fun setup() {
    // Create a MockWebServer.
    // These are lean enough that you can create a new instance for every unit test.
    server = MockWebServer()

    // Start the server.
    server.start()
  }

  @After
  open fun teardown() = server.shutdown()
}
