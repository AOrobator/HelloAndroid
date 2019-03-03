package com.orobator.helloandroid.stackoverflow.answers

import okhttp3.mockwebserver.MockResponse
import org.intellij.lang.annotations.Language

@Language("JSON")
const val getAnswersJson: String = """
{
  "items": [
    {
      "owner": {
        "reputation": 7563,
        "user_id": 634336,
        "user_type": "registered",
        "profile_image": "https://www.gravatar.com/avatar/b3d25e1315c6c0952b28f9643735aa6e?s=128&d=identicon&r=PG",
        "display_name": "Tugrul Ates",
        "link": "https://stackoverflow.com/users/634336/tugrul-ates"
      },
      "is_accepted": false,
      "score": 3,
      "last_activity_date": 1550363856,
      "creation_date": 1550363856,
      "answer_id": 54729053,
      "question_id": 54729015,
      "body": "<p>No, this doesn't seem like a bad practice, without further context.</p>\n\n<p>Since the config values will not change and you're just making a copy of the individual config parameters you need, you're making the code a little bit more readable without introducing any downsides.</p>\n"
    },
    {
      "owner": {
        "reputation": 21142,
        "user_id": 135769,
        "user_type": "registered",
        "accept_rate": 82,
        "profile_image": "https://i.stack.imgur.com/eQzEN.jpg?s=128&g=1",
        "display_name": "3Dave",
        "link": "https://stackoverflow.com/users/135769/3dave"
      },
      "is_accepted": true,
      "score": 4,
      "last_activity_date": 1550363723,
      "creation_date": 1550363723,
      "answer_id": 54729040,
      "question_id": 54729015,
      "body": "<p>The optimizer should clean this up, so I doubt there'd be a perf impact. The question is: does it make your code more readable and/or easier to maintain? Using temp variables in this way is pretty common. Unless you can come up with a <em>reason</em> that it's \"bad,\" go for it.</p>\n\n<p>Another thing to consider is that, by copying to a local variable and avoiding dereferencing the pointer every time, you're potentially speeding up access to the value in question. HOWEVER: that value is likely cached after the first access, and, again the Magical, Wonderful Optimizer will probably fix that. (But, as an engineer, you should never count on that - test and validate rather than assume.)</p>\n\n<p>In my opinion (cringe), you're fine. Go with it.</p>\n"
    }
  ],
  "has_more": false,
  "quota_max": 10000,
  "quota_remaining": 9972
}"""

val successfulGetAnswersResponse: MockResponse =
  MockResponse()
      .setResponseCode(200)
      .setBody(getAnswersJson)