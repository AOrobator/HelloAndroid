package com.orobator.helloandroid.lesson18.followalong.api;

import com.google.gson.annotations.SerializedName;
import com.orobator.helloandroid.lesson18.followalong.model.Repo;
import java.util.ArrayList;
import java.util.List;

/**
 * Data class to hold repo responses from searchRepo API calls
 */
class RepoSearchResponse {

  @SerializedName("total_count")
  private final int total;

  @SerializedName("items")
  private final List<Repo> items;

  private final int nextPage;

  public RepoSearchResponse(int total, List<Repo> items, int nextPage) {
    this.total = total;
    this.items = items == null ? new ArrayList<>() : items;
    this.nextPage = nextPage;
  }

  public int getTotal() {
    return total;
  }

  public List<Repo> getItems() {
    return items;
  }

  public int getNextPage() {
    return nextPage;
  }
}
