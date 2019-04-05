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

[Paging library]: https://developer.android.com/topic/libraries/architecture/paging
[Paging Codelab]: https://codelabs.developers.google.com/codelabs/android-paging/index.html
 