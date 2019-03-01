package com.orobator.mockwebserverassertions

import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.amshove.kluent.`should equal`

/**
 * MockWebServer extensions for syntactic sugar while testing
 */
enum class HttpMethod {
  DELETE,
  GET,
  PATCH,
  POST,
  PUT
}

enum class ContentType(val type: String) {
  FORM_URL_ENCODED("application/x-www-form-urlencoded"),
  JSON("application/json; charset=UTF-8"),
}

data class ExpectedRequest(
  val authorization: String?,
  val method: HttpMethod,
  val contentType: ContentType?,
  val path: String,
  val body: String
)

infix fun MockWebServer.`received request`(expectedRequest: ExpectedRequest) {
  val actualRequest: RecordedRequest = takeRequest()

  actualRequest `has authorization` expectedRequest.authorization
  actualRequest `has method` expectedRequest.method.name
  actualRequest `has content type` expectedRequest.contentType?.type
  actualRequest `has path` expectedRequest.path
  actualRequest `has body` expectedRequest.body
}

infix fun RecordedRequest.`has body`(body: String) {
  this.body.readUtf8() `should equal` body
}

infix fun RecordedRequest.`has path`(path: String) {
  this.path `should equal` path
}

infix fun RecordedRequest.`has content type`(contentType: String?) {
  this.getHeader("Content-Type") `should equal` contentType
}

infix fun RecordedRequest.`has authorization`(authorization: String?) {
  this.getHeader("Authorization") `should equal` authorization
}

infix fun RecordedRequest.`has method`(method: String) {
  this.method `should equal` method
}
