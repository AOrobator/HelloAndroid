package com.orobator.helloandroid.lesson18.followalong.model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/**
 * Immutable model class for a Github repo that holds all the information about
 * a repository. Objects of this type are received from the Github API,
 * therefore all the fields are annotated with the serialized name.
 *
 * This class also defines the Room repos table, where the repo {@link Repo#id} is
 * the primary key.
 */
@Entity(tableName = "repos")
public class Repo {

  @PrimaryKey @SerializedName("id")
  private final long id;

  @SerializedName("name")
  private final String name;

  @SerializedName("full_name")
  private final String fullName;

  @SerializedName("description")
  private final String description;

  @SerializedName("html_url")
  private final String url;

  @SerializedName("stargazers_count")
  private final int stars;

  @SerializedName("forks_count")
  private final int forks;

  @SerializedName("language") @Nullable
  private final String language;

  public Repo(long id, String name, String fullName, String description, String url, int stars,
      int forks, @Nullable String language) {
    this.id = id;
    this.name = name;
    this.fullName = fullName;
    this.description = description;
    this.url = url;
    this.stars = stars;
    this.forks = forks;
    this.language = language;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getFullName() {
    return fullName;
  }

  public String getDescription() {
    return description;
  }

  public String getUrl() {
    return url;
  }

  public int getStars() {
    return stars;
  }

  public int getForks() {
    return forks;
  }

  @Nullable public String getLanguage() {
    return language;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Repo repo = (Repo) o;
    return id == repo.id &&
        stars == repo.stars &&
        forks == repo.forks &&
        Objects.equals(name, repo.name) &&
        Objects.equals(fullName, repo.fullName) &&
        Objects.equals(description, repo.description) &&
        Objects.equals(url, repo.url) &&
        Objects.equals(language, repo.language);
  }

  @Override public int hashCode() {
    return Objects.hash(id, name, fullName, description, url, stars, forks, language);
  }

  @Override public String toString() {
    return "Repo{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", fullName='" + fullName + '\'' +
        ", description='" + description + '\'' +
        ", url='" + url + '\'' +
        ", stars=" + stars +
        ", forks=" + forks +
        ", language='" + language + '\'' +
        '}';
  }
}
