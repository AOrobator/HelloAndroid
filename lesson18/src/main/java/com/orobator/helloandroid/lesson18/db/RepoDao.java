package com.orobator.helloandroid.lesson18.db;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.orobator.helloandroid.lesson18.model.Repo;
import java.util.List;

/**
 * Room data access object for accessing the {@link Repo} table.
 */
@Dao
interface RepoDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(List<Repo> posts);

  // Do a similar query as the search API:
  // Look for repos that contain the query string in the name or in the
  // description and order those results descending, by the number of stars and
  // then by name.
  @Query("SELECT * FROM repos WHERE (name LIKE :queryString) "
      + "OR (description LIKE :queryString) "
      + "ORDER BY stars DESC, name ASC")
  DataSource.Factory<Integer, Repo> reposByName(String queryString);
}
