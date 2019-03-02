package com.orobator.helloandroid.stackoverflow

import okhttp3.mockwebserver.MockResponse
import org.intellij.lang.annotations.Language

@Language("JSON")
const val successfulGetQuestionsJson =
  """
{
  "items": [
    {
      "tags": [
        "c",
        "pointers",
        "struct"
      ],
      "owner": {
        "reputation": 20,
        "user_id": 5101709,
        "user_type": "registered",
        "profile_image": "https://i.stack.imgur.com/fkAgH.png?s=128&g=1",
        "display_name": "Shwig",
        "link": "https://stackoverflow.com/users/5101709/shwig"
      },
      "is_answered": true,
      "view_count": 16,
      "accepted_answer_id": 54729040,
      "answer_count": 2,
      "score": 2,
      "last_activity_date": 1550364803,
      "creation_date": 1550363442,
      "last_edit_date": 1550364803,
      "question_id": 54729015,
      "link": "https://stackoverflow.com/questions/54729015/is-it-bad-practice-to-store-a-struct-member-value-in-local-var-with-a-shorter-na",
      "title": "Is it bad practice to store a struct member value in local var with a shorter name?"
    },
    {
      "tags": [
        "mysql",
        "sql",
        "count",
        "where-clause"
      ],
      "owner": {
        "reputation": 20,
        "user_id": 590030,
        "user_type": "registered",
        "accept_rate": 75,
        "profile_image": "https://www.gravatar.com/avatar/bb11d9dd212e7310dc4d8e71228f2ec6?s=128&d=identicon&r=PG",
        "display_name": "sebrenner",
        "link": "https://stackoverflow.com/users/590030/sebrenner"
      },
      "is_answered": true,
      "view_count": 23,
      "answer_count": 1,
      "score": 1,
      "last_activity_date": 1550363263,
      "creation_date": 1550361755,
      "last_edit_date": 1550363263,
      "question_id": 54728880,
      "link": "https://stackoverflow.com/questions/54728880/how-to-select-rows-with-where-statement-and-count-1",
      "title": "How to select rows with WHERE statement and COUNT(*) = 1"
    },
    {
      "tags": [
        "java"
      ],
      "owner": {
        "reputation": 1,
        "user_id": 7965712,
        "user_type": "registered",
        "profile_image": "https://www.gravatar.com/avatar/3ceb9581210e765a7e4fefce81928fbc?s=128&d=identicon&r=PG&f=1",
        "display_name": "Dr. Bright",
        "link": "https://stackoverflow.com/users/7965712/dr-bright"
      },
      "is_answered": false,
      "view_count": 28,
      "answer_count": 4,
      "score": -1,
      "last_activity_date": 1550365222,
      "creation_date": 1550363841,
      "question_id": 54729052,
      "link": "https://stackoverflow.com/questions/54729052/how-to-check-if-time-in-millis-is-yesterday",
      "title": "How to check if time in millis is yesterday"
    }
  ],
  "has_more": true,
  "quota_max": 10000,
  "quota_remaining": 9995
}
"""

val successfulGetQuestionsResponse: MockResponse =
  MockResponse()
      .setResponseCode(200)
      .setBody(successfulGetQuestionsJson)