package com.orobator.helloandroid.lesson5

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers

infix fun Int.typeText(text: String) = Espresso.onView(ViewMatchers.withId(this)).perform(
    ViewActions.replaceText(text)
)

fun Int.click() = Espresso.onView(ViewMatchers.withId(this)).perform(ViewActions.click())

infix fun Int.hasText(text: String) = Espresso.onView(ViewMatchers.withId(this)).check(
    ViewAssertions.matches(ViewMatchers.withText(text))
)