# Lesson 18: Paging

Paging data is an efficient way of incrementally loading data from large collections into a limited 
amount of memory. Many APIs only return paged results for large collections to keep response sizes 
small and limit the memory usage of a server. In addition to being used for network requests, one 
might also implement paging for database queries of large collections. Google provides the 
[Paging library] for conveniently implementing paging. In this lesson, we'll work through Google's 
[Paging Codelab] to learn how to use it.

## 1. Introduction

### What you'll build
In this codelab, you start with a sample app that already displays a list of GitHub repositories, 
loading data from the database and that is backed by network data. Whenever the user scrolls and 
gets to the end of the displayed list, a new network request is triggered and its result is saved in
the database.

You will add code through a series of steps, integrating the Paging library components as you 
progress. These components are described in Step 2.


## 2. Paging library components

The Paging Library makes it easier for you to load data gradually and gracefully within your app's 
UI.

The [Guide to App Architecture] proposes an architecture with the following main components:

 * A local database that serves as a single source of truth for data presented to the user and the 
   actions the user has taken to change that data.
 * A web API service
 * A repository that works with the database and the API service, providing a unified data interface
 * A ViewModel that provides data specific for the UI
 * The UI, which shows a visual representation of the data in the ViewModel
 
The Paging library works with all of these components and coordinates the interactions between them,
so that it can page content from a data source and display that content in the UI.

![paging_library_overview]

This codelab introduces you to the Paging library and its main components:

 * [PagedList] - a collection that loads data in pages, asynchronously. A `PagedList` can be used to
   load data from sources you define, and present it easily in your UI with a `RecyclerView`.
 * [DataSource] and [DataSource.Factory] - a `DataSource` is the base class for loading snapshots of 
   data into a `PagedList`. A `DataSource.Factory` is responsible for creating a `DataSource`.
 * [LivePagedListBuilder] - builds a `LiveData<PagedList>`, based on `DataSource.Factory` and a 
   [PagedList.Config].
 * [BoundaryCallback] - signals when a `PagedList` has reached the end of available data.
 * [PagedListAdapter] - a `RecyclerView.Adapter` that presents paged data from `PagedLists` in a 
   `RecyclerView`. `PagedListAdapter` listens to `PagedList` loading callbacks as pages are loaded, 
   and uses [DiffUtil] to compute fine grained updates as new `PagedLists` are received.

In this codelab, you implement examples of each of the components described above.

## 3. Project overview

![github_app]

The app allows you to search GitHub for repositories whose name or description contains a specific 
word. The list of repositories is displayed, in descending order based on the number of stars, then 
by the name. The database is the source of truth for data that is displayed by the UI, and it's 
backed by network requests.

The list of repositories, by name, is retrieved via a `LiveData` object in `RepoDao.reposByName`. 
Whenever new data from the network is inserted into the database, the `LiveData` will emit again with 
the entire result of the query.

The current implementation has two memory/performance issues:

 * The entire **repo** table of the database is loaded at once.
 * The entire list of results from the database is kept in memory.
 
The app follows the architecture recommended in the "Guide to App Architecture", using Room as local
data storage. Here's what you will find in each package:

 * **api** - contains Github API calls, using Retrofit
 * **db** - database cache for network data
 * **data** - contains the repository class, responsible for triggering API requests and saving the 
   response in the database
 * **ui** - contains classes related to displaying an `Activity` with a `RecyclerView`
 * **model** - contains the `Repo` data model, which is also a table in the Room database; and 
   `RepoSearchResult`, a class that is used by the UI to observe both search results data and network errors

__Caution: The `GithubRepository` and `Repo` classes have similar names but serve very different 
purposes.The repository class, `GithubRepository`, works with `Repo` data objects that represent 
GitHub code repositories.__

## 4. Load data in chunks with the PagedList

In our current implementation, we use a `LiveData<List<Repo>>` to get the data from the database and
pass it to the UI. Whenever the data from the local database is modified, the `LiveData` emits an 
updated list. The alternative to `List<Repo>` is a `PagedList<Repo>`. A [PagedList] is a version of 
a List that loads content in chunks. Similar to the List, the `PagedList` holds a snapshot of 
content, so updates occur when new instances of `PagedList` are delivered via `LiveData`.

When a `PagedList` is created, it immediately loads the first chunk of data and expands over time as 
content is loaded in future passes. The size of the `PagedList` is the number of items loaded during
each pass. The class supports both infinite lists and very large lists with a fixed number of 
elements.

**Replace occurrences of `List<Repo>` with `PagedList<Repo>`:**

 * `RepoSearchResult` is the data model that's used by the UI to display data. Since the data is no 
   longer a `LiveData<List<Repo>>` but is paginated, it needs to be replaced with 
   `LiveData<PagedList<Repo>>`. Make this change in the `RepoSearchResult` class.

 * `SearchRepositoriesViewModel` works with the data from the `GithubRepository`. Change the type of 
   the repos val exposed by the `ViewModel`, from `LiveData<List<Repo>>` to 
   `LiveData<PagedList<Repo>>`. 
   
 * `SearchRepositoriesActivity` observes the repos from the `ViewModel`.
  
Change the type of the observer from `List<Repo>` to `PagedList<Repo>`.

```java
viewModel.repos.observe(this, (PagedList<Repo> repos) -> {
      int size = 0;
      if (repos != null) {
        size = repos.size();
      }
      Log.d("Activity", "list: " + size + "");
      showEmptyList(size == 0);
      adapter.submitList(repos);
    });
```

## 5. Define the source of data for the paged list

The `PagedList` loads content dynamically from a source. In our case, because the database is the 
main source of truth for the UI, it also represents the source for the `PagedList`. If your app gets
data directly from the network and displays it without caching, then the class that makes network 
requests would be your data source.

A source is defined by a [DataSource] class. To page in data from a source that can change—such as a
source that allows inserting, deleting or updating data—you will also need to implement a 
`DataSource.Factory` that knows how to create the `DataSource`. Whenever the data set is updated,
the `DataSource` is invalidated and re-created automatically through the `DataSource.Factory`.

The Room persistence library provides native support for data sources associated with the `Paging`
library. For a given query, Room allows you to return a `DataSource.Factory` from the DAO and 
handles the implementation of the `DataSource` for you.

**Update the code to get a `DataSource.Factory` from Room:**

 * `RepoDao`: update the `reposByName()` function to return a `DataSource.Factory<Int, Repo>`.
   ```java
   DataSource.Factory<Integer, Repo> reposByName(String queryString)
   ```
 * `GithubLocalCache` uses this function. Change the return type of `reposByName` function to be
   `DataSource.Factory<Integer, Repo>`.
   
## 6. Build and configure a paged list
To build and configure a `LiveData<PagedList>`, use a [LivePagedListBuilder]. Besides the 
`DataSource.Factory`, you need to provide a `PagedList` configuration, which can include the 
following options:

 * the size of a page loaded by a `PagedList`
 * how far ahead to load
 * how many items to load when the first load occurs
 * whether null items are to be added to the `PagedList`, to represent data that hasn't been loaded 
   yet.
 
**Update `GithubRepository` to build and configure a paged list:**

 * Define the number of items per page, to be retrieved by the paging library. Add another private 
   `static final String` called `DATABASE_PAGE_SIZE`, and set it to 20. Our `PagedList` will then 
   page data from the `DataSource` in chunks of 20 items.
   ```java
   public class GithubRepository {
     private static final int NETWORK_PAGE_SIZE = 50;
     private static final int DATABASE_PAGE_SIZE = 20;
   }
   ```

_Note: The `DataSource` page size should be several screens worth of items. If the page is too 
small, your list might flicker as pages content doesn't cover the full screen. Larger page sizes are
good for loading efficiency, but can increase latency to show updates._

In `GithubRepository.search()` method, make the following changes:

 * Remove the `lastRequestedPage` initialization and the call to `requestAndSaveData()`, but don't 
   completely remove this function for now.
 * Create a new variable to hold the `DataSource.Factory` from `cache.reposByName()`:
   ```java
   // Get data source factory from the local cache
   DataSource.Factory<Integer, Repo> dataSourceFactory = cache.reposByName(query);
   ``` 
 * In the `search()` function, construct the data value from a `LivePagedListBuilder`. The 
   `LivePagedListBuilder` is constructed using the `dataSourceFactory` and the database page size 
   that you each defined earlier.
   ```java
   public RepoSearchResult search(String query) {
       // Get data source factory from the local cache
       DataSource.Factory<Integer, Repo> dataSourceFactory = cache.reposByName(query);
   
       // Get the paged list
       LiveData<PagedList<Repo>> data = new LivePagedListBuilder<>(dataSourceFactory, DATABASE_PAGE_SIZE).build();
   
       // Get the network errors exposed by the boundary callback
       return new RepoSearchResult(data, networkErrors);
   }
   ```
   
## 7. Make the RecyclerView Adapter work with a PagedList
To bind a `PagedList` to a `RecycleView`, use a [PagedListAdapter]. The `PagedListAdapter` gets 
notified whenever the `PagedList` content is loaded and then signals the `RecyclerView` to update.

**Update the `ReposAdapter` to work with a `PagedList`:**

 * Right now, `ReposAdapter` is a `ListAdapter`. Make it a `PagedListAdapter`:
   ```java
   class ReposAdapter extends PagedListAdapter<Repo, RecyclerView.ViewHolder>
   ```
Our app finally compiles! Run it, and check out how it works.

## 8. Trigger network updates
Currently, we use an `OnScrollListener` attached to the `RecyclerView` to know when to trigger more 
data. We can let the Paging library handle list scrolling for us, though.

**Remove the custom scroll handling:**

 * `SearchRepositoriesActivity`: remove the `setupScrollListener()` method and all references to it
 * `SearchRepositoriesViewModel`: remove the `listScrolled()` method and the `VISIBLE_THRESHOLD` 
   constant
   
After removing the custom scroll handling, our app has the following behavior:

 * Whenever we scroll, the `PagedListAdapter` will try to get the item from a specific position.
 * If the item at that index isn't loaded in the `PagedList` yet, the Paging library tries to get 
   the data from the data source.
   
A problem appears when the data source doesn't have any more data to give us, either because zero 
items were returned from the initial loading of the data or because we've reached the end of the 
data from the `DataSource`. To resolve this issue, implement a [BoundaryCallback]. This class 
notifies us when either situation occurs, so we know when to request more data. Because our 
`DataSource` is a Room database, backed by network data, the callbacks let us know that we should 
request more data from the API.

**Handle data loading with `BoundaryCallback`:**

 * In the `data` package, create a new class called `RepoBoundaryCallback` that implements 
   `PagedList.BoundaryCallback<Repo>`. Because this class handles the network requests and database 
   data saving for a specific query, add the following parameters to the constructor: a query 
   `String`, the `GithubService`, and the `GithubLocalCache`.
 * In `RepoBoundaryCallback`, override `onZeroItemsLoaded()` and `onItemAtEndLoaded()`.
   ```java
   public class RepoBoundaryCallback extends PagedList.BoundaryCallback {
     private final String query;
     private final GithubService service;
     private final GithubLocalCache cache;
   
     public RepoBoundaryCallback(String query, GithubService service, GithubLocalCache cache) {
       this.query = query;
       this.service = service;
       this.cache = cache;
     }
   
     @Override public void onZeroItemsLoaded() {
   
     }
   
     @Override public void onItemAtEndLoaded(@NonNull Object itemAtEnd) {
   
     }
   }
   ```

 * Move the following fields from `GithubRepository` to `RepoBoundaryCallback`: 
   `isRequestInProgress`, `lastRequestedPage`, and `networkErrors`.
 * Create a getter for `networkErrors`, where the return type is `LiveData<String>`. We need to make
   this change because, internally, in the `RepoBoundaryCallback` class, we can work with a 
   `MutableLiveData`, but outside the class, we only expose a `LiveData` object, whose values can't 
   be modified.
   ```java
   // Keep the last requested page. When the request is successful, increment
   // the page number.
   private int lastRequestedPage = 1;
   
   // Avoid triggering multiple requests in the same time
   private boolean isRequestInProgress = false;
   
   // LiveData of network errors.
   private MutableLiveData<String> networkErrors = new MutableLiveData<>();
   
   public LiveData<String> getNetworkErrors() {
     return networkErrors;
   }
   ```

 * Move the `NETWORK_PAGE_SIZE` constant from `GithubRepository` to `RepoBoundaryCallback`.
 * Move `GithubRepository.requestAndSaveData()` method to `RepoBoundaryCallback`.
 * We should request data from network and save it in the cache whenever the Paging data source 
   notifies us that there are no items in the source (when 
   `RepoBoundaryCallback.onZeroItemsLoaded()` is called) or that the last item from the data source 
   has been loaded (when `RepoBoundaryCallback.onItemAtEndLoaded()` is called). So, call the 
   `requestAndSaveData()` method from `onZeroItemsLoaded()` and `onItemAtEndLoaded()`:
   ```java
   @Override 
     public void onZeroItemsLoaded() {
       requestAndSaveData(query);
     }
   
     @Override 
     public void onItemAtEndLoaded(@NonNull Object itemAtEnd) {
       requestAndSaveData(query);
     }
   ```
**Update `GithubRepository` to use the `BoundaryCallback` when creating the `PagedList`:**

 * In the `search()` method, construct the `RepoBoundaryCallback` using the query, service, and cache.
 * Create a value in the `search()` method that maintains a reference to the network errors that 
   `RepoBoundaryCallback` discovers. Also change the type of `networkErrors` in `RepoSearchResult` 
   to `LiveData` instead of `MutableLiveData`. 
 * Set the boundary callback to `LivePagedListBuilder`.
   ```java
   public RepoSearchResult search(String query) {
       Log.d("GithubRepository", "New query: " + query);
   
       // Get data source factory from the local cache
       DataSource.Factory<Integer, Repo> dataSourceFactory = cache.reposByName(query);
       
       // Construct the boundary callback
       RepoBoundaryCallback boundaryCallback = new RepoBoundaryCallback(query, service, cache);
       LiveData<String> networkErrors = boundaryCallback.getNetworkErrors();
   
       // Get the paged list
       LiveData<PagedList<Repo>> data =
           new LivePagedListBuilder<>(dataSourceFactory, DATABASE_PAGE_SIZE)
               .setBoundaryCallback(boundaryCallback)
               .build();
       
       // Get the network errors exposed by the boundary callback
       return new RepoSearchResult(data, networkErrors);
     }
   ```
 * Remove the `requestMore()` function from `GithubRepository`
 
That's it! With the current setup, the Paging library components are the ones triggering the API 
requests at the right time, saving data in the database, and displaying the data. So, run the app 
and search for repositories.

## 9. Wrap up
Now that we added all the components, let's take a step back and see how everything works together.

The `DataSource.Factory` (implemented by Room) creates the `DataSource`. Then, 
`LivePagedListBuilder` builds the `LiveData<PagedList>`, using the passed-in `DataSource.Factory`, 
`BoundaryCallback`, and `PagedList` configuration. This `LivePagedListBuilder` object is responsible 
for creating `PagedList` objects. When a `PagedList` is created, two things happen at the same time:

 * The `LiveData` emits the new `PagedList` to the `ViewModel`, which in turn passes it to the UI. 
   The UI observes the changed `PagedList` and uses its `PagedListAdapter` to update the 
   `RecyclerView` that presents the `PagedList` data. (represented in the following animation by an 
   empty square).
 * The `PagedList` tries to get the first chunk of data from the `DataSource`. When the 
   `DataSource` is empty, for example when the app is started for the first time and the database is 
   empty, it calls `BoundaryCallback.onZeroItemsLoaded()`. In this method, the `BoundaryCallback` 
   requests more data from the network and inserts the response data in the database.

![page_list_loading]

After the data is inserted in the `DataSource`, a new `PagedList` object is created (represented in 
the following animation by a filled-in square). This new data object is then passed to the 
`ViewModel` and UI using `LiveData` and displayed with the help of the `PagedListAdapter`.

![updating_adapter]

When the user scrolls, the `PagedList` requests that the `DataSource` load more data, querying the 
database for the next chunk of data. When the `PagedList` paged all the available data from the 
`DataSource`, `BoundaryCallback.onItemAtEndLoaded()` is called. The `BoundaryCallback` requests data 
from the network and inserts the response data in the database. The UI then gets re-populated based 
on the newly-loaded data.

![updating_adapter]      


[Paging library]: https://developer.android.com/topic/libraries/architecture/paging
[Paging Codelab]: https://codelabs.developers.google.com/codelabs/android-paging/index.html
[Guide to App Architecture]: https://developer.android.com/jetpack/docs/guide
[paging_library_overview]: img/paging_library_overview.png "Paging Library Overview"
[PagedList]: https://developer.android.com/reference/androidx/paging/PagedList 
[DataSource]: https://developer.android.com/reference/androidx/paging/DataSource
[DataSource.Factory]: https://developer.android.com/reference/androidx/paging/DataSource.Factory
[LivePagedListBuilder]: https://developer.android.com/reference/androidx/paging/LivePagedListBuilder
[PagedList.Config]: https://developer.android.com/reference/androidx/paging/PagedList.Config
[BoundaryCallback]: https://developer.android.com/reference/androidx/paging/PagedList.BoundaryCallback
[PagedListAdapter]: https://developer.android.com/reference/androidx/paging/PagedListAdapter
[DiffUtil]: https://developer.android.com/reference/androidx/recyclerview/widget/DiffUtil
[github_app]: img/github_app.png "Github app you'll be building"
[page_list_loading]: img/page_list_loading.png "Initial PagedList loading"
[updating_adapter]: img/updating_adapter.png "Updating the adapter"
[updating_adapter]: img/fetching_more_data.png "Fetching more data on scroll"