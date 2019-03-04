## Lesson 10: RecyclerView

In this lesson, we'll be exploring how to use a `RecyclerView` by making a simple StackOverflow 
client. We'll end up with something that looks like this:

![Stack Overflow][StackOverflow]

First we'll have to make a separate module called `stackoverflow-api` that does all our networking 
calls. To start off, we'll want a function that gets the 20 hottest questions in descending order.
You'll also want a separate function to get the answers to a specific question. Make sure these 2 
methods are in separate API interfaces with separate repositories. Here's the [StackOverflow API] to
get you started. You'll want to use `filter=withbody` in order to get the actual text of the 
questions and answers. Make sure to unit test your networking code to verify proper functionality.

After we finish the networking part of the code, we'll want to add the `RecyclerView` dependency to 
our build.gradle file.

```groovy
dependencies {
  implementation 'androidx.recyclerview:recyclerview:1.0.0'
}
```

## RecyclerView Layout

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

## Loading RecyclerView Data

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

## Using Adapters

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

## Handling Click Events

It would be rather boring if all you could do was look at the items in a RecyclerView, so let's make 
them clickable! In this app, clicking on a question should take you to AnswersActivity where the 
answers for that particular question are shown. To start off, we'll modify our list_item so it 
responds to touch events. Make the following modifications to the ConstraintLayout directly inside 
the MaterialCardView:

```xml
<androidx.constraintlayout.widget.ConstraintLayout
          android:id="@+id/clickTarget"
          android:background="?selectableItemBackground"
          android:clickable="true"
          android:focusable="true">
```

We'll give it an id of clickTarget. As an aside, whenever we are referencing an id for the first 
time, we need to use `@+id/yourId` instead of `@id/yourId`. For subsequent references, you can use
`@id/yourId`. We then set this view's background to `?selectableItemBackground`. This, in 
combination with setting the view as clickable and focusable, will show a touch ripple whenever this 
view is touched or clicked.

In QuestionsViewModel, we'll save a reference to the `QuestionsResponse`, and use it in the 
following method:

```java
public void onQuestionClicked(int position) {
  if (questionsResponse != null) {
    Question question = questionsResponse.getItems().get(position);
    questionLiveData.setValue(question);
  }
}
```  

We'll also declare a `MutableLiveData<Question>` and expose it as a `LiveData` so 
`QuestionsActivity` can observe which `Question` is clicked. Then we'll pass `QuestionsViewModel` to
our Adapter so each list item/`ViewHolder` can forward click events to it. In `QuestionViewHolder` 
add the following statement to the bind method:

```java
binding
  .clickTarget
  .setOnClickListener(v -> 
      this.viewModel.onQuestionClicked(getAdapterPosition()));
```

Notice how we never store the adapter position for `QuestionViewHolder`. This is because it is 
subject to change when `QuestionViewHolder` gets recycled. Now that we're forwarding click events to
our ViewModel, we'll want to actually react to those changes. In `QuestionsActivity`, observe 
`QuestionsViewModel`'s `LiveData<Question>`. When this updates, we'll get an `Intent` with the 
`Question` for `AnswersActivity`, then fire the `Intent`:

```java
@Override public void onChanged(Question question) {
  Intent intent = AnswersActivity.getIntent(this, question);
  startActivity(intent);
}
```

The implementation for `getIntent()` is pretty simple, but there's a little something going on behind 
the scenes. We want to add our `Question` to the `Intent` as an extra so it can be retrieved by 
`AnswersActivity`. 

```java
public static Intent getIntent(Context context, Question question) {
  Intent intent = new Intent(context, AnswersActivity.class);
  intent.putExtra(KEY_QUESTION, question);
  return intent;
}
```

There are many types that `Intent#putExtra` accepts, but `Question` is currently 
not one of them. We'll fix this by making both `Question` and `User` implement the `Parcelable` 
interface. Implementing the `Parcelable` interface allows us to easily and quickly serialize 
objects. This will be much faster than having our model classes implement the`Serializable` 
interface. We don't want to write out the implementation by hand, so pressing Option + Enter, after 
declaring that you implement the `Parcelable` interface will add the `Parcelable` implementation for
you. This gives us a couple things.

First there is a new constructor that takes in a `Parcel`. When we said `Question` was a 
`Parcelable` that means it can be written to a `Parcel`. `Parcel`s generally contain the ability to 
read and write primitives as well as other `Parcelable`s. We also get a method 
`writeToParcel(Parcel, int)` which does what you think it does. Notice that the data is written and 
read from the `Parcel` in the exact same order. This is required for proper functionality.

The next method we see is `describeContents`. This method should always return 0 unless you need to 
put a FileDescriptor object into Parcelable. Then you should/must specify CONTENTS_FILE_DESCRIPTOR 
as the return value of describeContents()

The last thing we see for the `Parcelable` implementation is a static field 
`Creator<Question> CREATOR` that can create a Question from a parcel, as well as create an array of 
`Question`s. We'll have to make `User` implement the `Parcelable` interface as well, and we'll 
probably want to do that before making `Question` implement the `Parcelable` interface.

Once our model classes implement the `Parcelable` interface, we can retrieve the `Question` in 
`onCreate` like so:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_answers);
  
  Question question = getIntent().getParcelableExtra(KEY_QUESTION);
}
```

## Handling multiple view types

In the `AnswersActivity`, create a RecyclerView where the adapter has 2 view types, a `Question` and 
an `Answer`. You'll have to override `getItemViewType` in your adapter. You should return a unique 
constant for each view type. The first item should be the `Question` and the remaining items should 
be the `Answer`s. I'd also recommend making an abstract `ViewHolder` that the 2 view types extend 
from. That way you can declare your adapter like:

```java
public class AnswersRecyclerAdapter extends RecyclerView.ViewHolder<YourAbstractViewHolderClass> { ... }
```

Your end product should look something like this:

![Answers View][answers_view]

## Image Loading & BindingAdapters

Most apps display some imagery to the user instead of displaying walls of text. To remedy this, 
we'll load in the profile images for the users in the `AnswersActivity`. There are many image 
loading libraries for Android and their APIs are all very similar. The main ones to check out are 
[Glide] and [Picasso]. For this lesson, we'll be using Glide. First we'll have to add the relevant
dependencies to our build.gradle file:

```groovy
dependencies {
  implementation "com.github.bumptech.glide:glide:4.9.0"
  annotationProcessor 'com.github.bumptech.glide:compiler:4.9.0'
}
```

Loading images with Glide requires only 1 line of code!

```java
Glide
  .with(itemView)
  .load(questionViewModel.profilePicUrl)
  .into(binding.profileImageView);
```

Glide handles a lot of stuff behind the scenes for you, such as recognizing when an ImageView is 
being used in a RecyclerView and the load needs to be cancelled due to the view being recycled. 
Glide also allows users to specify three different placeholders that are used under different 
circumstances: placeholder, error, and fallback. 

Placeholders are Drawables that are shown while a request is in progress. When a request completes 
successfully, the placeholder is replaced with the requested resource.

```java
Glide.with(fragment)
  .load(url)
  .placeholder(R.drawable.placeholder)
  .into(view);
```

Error Drawables are shown when a request permanently fails. Error Drawables are also shown if the 
requested url/model is null and no fallback Drawable is set.

```java
Glide.with(activity)
  .load(url)
  .error(new ColorDrawable(Color.RED))
  .into(view);
```

Fallback Drawables are shown when the requested url/model is null. The primary purpose of fallback 
Drawables is to allow users to indicate whether or not null is expected. For example, a null profile 
url may indicate that the user has not set a profile photo and that a default should be used. 
However, null may also indicate that meta-data is invalid or couldn't be retrieved. By default Glide 
treats null urls/models as errors, so users who expect null should set a fallback Drawable.

```java
Glide.with(view)
  .load(url)
  .fallback(R.drawable.fallback)
  .into(view);
```

Note that placeholders are loaded from Android resources on the main thread. Glide expects 
placeholders to be small and easily cache-able by the system resource cache.

When loading images, you should always be aware of the size of the image vs the size that you 
display it at. Not doing so can cause some really [nasty bugs]. Thankfully, Glide automatically 
scales down images to fit in the ImageView, but if you'd like to customize that behavior, you can 
always specify a `Transformation`.

```java
import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

Glide.with(fragment)
  .load(url)
  .apply(fitCenterTransform())
  .into(imageView);
```

Transformations in Glide take a resource, mutate it, and return the mutated resource. Typically 
transformations are used to crop, or apply filters to Bitmaps, but they can also be used to 
transform animated GIFs, or even custom resource types.

It's possible to use databinding to specify the url in the XML layout instead of manually loading 
the image in your adapter. To do this requires the use of a `BindingAdapter`. 

```java
public class BindingAdapters {
  @BindingAdapter({ "imgUrl" })
  public static void loadImage(ImageView imageView, String url) {
    Glide.with(imageView).load(url).into(imageView);
  }
}
```

To make a BindingAdapter, you'll need to annotate a method with `@BindingAdapter` and specify the 
name of the attribute you'd like to use. The method should take in the type of View you're binding, 
as well as the data to be bound. Once this is defined, you can use it in your layouts like so:

```xml
<ImageView
    android:id="@+id/profile_ImageView"
    android:layout_width="32dp"
    android:layout_height="32dp"
    app:imgUrl="@{vm.profilePicUrl}"/>
```

To try BindingAdapters out for yourself, refactor the `QuestionsActivity` to use `BindingAdapters` 
to toggle visibility. You should be able to specify the attribute like so: 
`app:isVisible="@{vm.isVisible}`. 

[StackOverflow]: StackOverflow.jpg "StackOverflow"
[StackOverflow API]: https://api.stackexchange.com/docs
[answers_view]: answers_view.jpg "Answers View"
[Glide]: https://bumptech.github.io/glide/
[Picasso]: https://square.github.io/picasso/
[nasty bugs]: https://www.youtube.com/watch?v=_MI7-xMNEDY
