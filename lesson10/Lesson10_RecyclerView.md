## Lesson 10: RecyclerView

In this lesson, we'll be exploring how to use a `RecyclerView` by making a simple StackOverflow 
client. We'll end up with something that looks like this:

![Stack Overflow][StackOverflow]

First we'll have to make a separate module called `stackoverflow-api` that does all our networking 
calls. To start off, we'll want a function that gets the 20 hottest questions in descending order. 
Here's the [StackOverflow API] to get you started. Make sure to unit test your networking code to 
verify proper functionality.

After we finish the networking part of the code, we'll want to add the `RecyclerView` dependency to 
our build.gradle file.

```groovy
dependencies {
  implementation 'androidx.recyclerview:recyclerview:1.0.0'
}
```

Now we'll work on our layouts. We'll start off with `list_item_question.xml`, which will be the 
layout for each individual list item. You'll want to use a combination of `MaterialCardView`, 
`ConstraintLayout`, and a `ChipGroup` inside a `HorizontalScrollView`. Don't worry about making the 
individual `Chip`s for now. They will be created at runtime. Just in case you're unfamiliar with 
`Chip`s, they represent the question tags in this layout. This layout should be a databinding layout 
that's bound to a `QuestionViewModel`.

Once the list item layout is done, we can move on to `activity_questions.xml`. We'll want four main 
views in this layout. First we'll want a `TextView` centered in the screen indicating that the list
is empty. Whenever you're displaying a collection of items, it's always a good practice to indicate 
the collection is empty to the user, instead of just showing a blank screen. Next we'll want a 
`ProgressBar` to show that we're loading data. We'll also want a `TextView` that displays any 
networking errors we may encounter as we're fetching these questions. Last, but certainly not least,
we'll add a `RecyclerView` to our layout. The declaration of our RecyclerView will look like this:

```xml
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/questions_recycler_view"
    android:layout_width="0dp"
    android:layout_height="0dp"
    android:clipToPadding="false"
    android:paddingBottom="4dp"
    android:paddingTop="4dp"
    android:visibility="@{vm.empty ? View.GONE : View.VISIBLE}"
    app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    tools:listitem="@layout/list_item_question"/>
``` 
We specify that the `RecyclerView` should not clip to padding, and it should have a top and bottom 
padding of 4dp. This means that the `RecyclerView` will have padding on the top and bottom that 
scrolls with our content. We're also going to change the visibility of our `RecyclerView` based on 
whether or not we have questions. You'll have to `<import type="android.view.View"/>` in the data 
section of the layout. We'll want similar functionality for the empty `TextView`, the `ProgressBar`,
and the error `TextView`. Next we'll specify the LayoutManager for the `RecyclerView`. A 
`RecyclerView` doesn't know how to layout its items, it only knows how to recycle views. We'll
specify a `LinearLayoutManager` so that our items are laid out vertically from top to bottom. This 
is the most common LayoutManager you'll use, but there is also, `StaggeredGridLayoutManager` and 
`GridLayoutManager`. Finally we'll use `tools:listitem` to specify which layout to use as a list 
item for our layout preview. Note that since this is in the tools namespace, it doesn't affect our 
final app at all.

`QuestionsViewModel` will take in a `QuestionsRepository` defined in `stackoverflow-api` as well as 
an instance of `AppSchedulers` so that we can make our network call on a background thread. These 
should be passed into the constructor and we'll use a combination of `ViewModelProvider.Factory` and
dependency injection with Dagger to achieve this. Feel free to use Lesson 9 as a reference.

`QuestionsViewModel` will have a `loadQuestions()` method that will grab questions from the 
repository. In the success case, we'll get back a `QuestionsResponse`. We'll convert the list of 
Questions we get in the response to `List<QuestionViewModel>` so we'll easily be able to databind 
each question to our layout. Once we have this list, we'll call `setValue` on an instance of 
`MutableLiveData<List<QuestionViewModel>>`. After exposing this as a 
`LiveData<List<QuestionViewModel>>`, our `QuestionsActivity` will observe it and pass on the result
to the RecyclerView's adapter.

A `RecyclerView` gets its views from a `RecylerView.Adapter`. The adapter lets the `RecyclerView` 
know how many views it has and how each list item should be rendered. It's also responsible for 
notifying the `RecyclerView` when the underlying data has changed. `RecyclerView.Adapter` has a type
parameter for `RecyclerView.ViewHolder`. You can think of a `ViewHolder` as an individual list item.
It holds a reference to the list item view, as well as metadata about its position in the `Adapter`.

We'll implement `QuestionViewHolder` first as it's rather straightforward.

```java
static public class QuestionViewHolder extends RecyclerView.ViewHolder {
    private final ListItemQuestionBinding binding;

    public QuestionViewHolder(@NonNull ListItemQuestionBinding binding) {
      super(binding.getRoot());

      this.binding = binding;
    }

    public void bind(QuestionViewModel viewModel) {
      binding.setVm(viewModel);

      binding.chipGroup.removeAllViews();
      for (String tag : viewModel.tags) {
        Chip chip = new Chip(itemView.getContext());
        chip.setText(tag);
        chip.setChipBackgroundColorResource(R.color.orange_100);
        binding.chipGroup.addView(chip);
      }
    }
  }
```

The super class's constructor takes in a View, so when we get our binding, we'll pass the root 
along. We'll declare a bind method that takes in a `QuestionViewModel` and sets it on 
`ListItemQuestionBinding`. The first thing that we should do after we set the ViewModel is remove
all views from the ChipGroup which holds the tags for the questions. It might seem a little strange 
that we're removing views when nothing has seemingly been added to it. We have to keep in mind that 
it's possible that this view has already been displayed in the `RecyclerView`, and as a result the 
View is populated with data from another list item. Therefore, we must reset the view to an initial 
state. Then we'll add a `Chip` for each tag the question has.

`QuestionsRecyclerAdapter` has 3 key methods which makes everything work. There are other methods 
you can override/implement to provide or receive additional information such as the view type of an 
item if there are multiple view types, or when a view has been recycled, but they are non-essential. 

The first essential method is `getItemCount`. This method tells the RecyclerView how many items in 
total it has. For this adapter, we have a list of `QuestionViewModel`s, so we just return the size 
of the list. It's possible for this list to change via `updateList` and when it does, we call 
`notifyDataSetChanged`, so the RecylerView can rebind all of its views. There are also more granular
change notification methods such as `notifyItemRemoved` or `notifyItemInserted` which provide 
support for better animations.

The second essential method is `onCreateViewHolder`, which as its name suggests, creates a 
ViewHolder for a given viewType. Since we only have 1 viewType here, we ignore this parameter. If we
had more than one viewType, in addition to using this parameter, we'd also have to override 
`getItemViewType`.

The third essential method is `onBindViewHolder`. Once a ViewHolder is created, we need to populate 
it with the relevant information. This method allows us to do just that by passing the position in 
the list of the ViewHolder. 

[StackOverflow]: StackOverflow.jpg "StackOverflow"
[StackOverflow API]: https://api.stackexchange.com/docs
