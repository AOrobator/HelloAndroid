package com.orobator.helloandroid.lesson18.followalong.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.orobator.helloandroid.lesson18.followalong.model.Repo;

/**
 * Database schema that holds the list of repos.
 */
@Database(
    entities = { Repo.class },
    version = 1,
    exportSchema = false
)
public abstract class RepoDatabase extends RoomDatabase {
  volatile private static RepoDatabase INSTANCE = null;

  public static synchronized RepoDatabase getInstance(Context context) {
    if (INSTANCE != null) {
      return INSTANCE;
    } else {
      INSTANCE = buildDatabase(context);
      return INSTANCE;
    }
  }

  private static RepoDatabase buildDatabase(Context context) {
    return Room.databaseBuilder(
        context.getApplicationContext(),
        RepoDatabase.class,
        "Github.db")
        .build();
  }

  public abstract RepoDao reposDao();
}
