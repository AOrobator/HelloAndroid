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
