package com.orobator.helloandroid.lesson7.model.api

import okhttp3.mockwebserver.MockResponse
import org.intellij.lang.annotations.Language

@Language("JSON")
const val triviaNumberFactJson =
  """
{
 "text": "2 is the first magic number in physics.",
 "number": 2,
 "found": true,
 "type": "trivia"
}"""

val triviaNumberFactResponse: MockResponse =
  MockResponse()
      .setResponseCode(200)
      .setBody(triviaNumberFactJson)