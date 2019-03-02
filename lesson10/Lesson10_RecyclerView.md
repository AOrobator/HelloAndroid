## Lesson 10: RecyclerView

In this lesson, we'll be exploring how to use a RecyclerView by making a simple StackOverflow 
client. We'll end up with something that looks like this:

![Stack Overflow][StackOverflow]

First we'll have to make a separate module called stackoverflow-api that does all our networking 
calls. To start off, we'll want a function that gets the 20 hottest questions in descending order. 
Here's the [StackOverflow API] to get you started. Make sure to unit test your networking code to 
verify proper functionality.

After we finish the networking part of the code, we'll want to add the RecyclerView dependency to 
our build.gradle file.

```groovy
dependencies {
  implementation 'androidx.recyclerview:recyclerview:1.0.0'
}
```

Now we'll work on our layouts. We'll start off with list_item_question.xml, which will be the layout
for each individual list item. You'll want to use a combination of MaterialCardView, 
ConstraintLayout, and a ChipGroup inside a HorizontalScrollView. Don't worry about making the 
individual Chips for now. They will be created at runtime. Just in case you're unfamiliar with 
Chips, they represent the question tags in this layout. This layout should be a databinding layout 
that's bound to a QuestionViewModel.


[StackOverflow]: StackOverflow.jpg "StackOverflow"
[StackOverflow API]: https://api.stackexchange.com/docs
