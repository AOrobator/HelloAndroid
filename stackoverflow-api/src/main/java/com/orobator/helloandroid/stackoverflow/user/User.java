package com.orobator.helloandroid.stackoverflow.user;

import com.google.gson.annotations.SerializedName;
import io.reactivex.annotations.Nullable;
import java.util.Objects;

public class User {
  @SerializedName("reputation")
  private int reputation;

  @SerializedName("user_id")
  private int userId;

  @SerializedName("user_type")
  private String userType;

  @SerializedName("accept_rate")
  @Nullable
  private Integer acceptRate;

  @SerializedName("profile_image")
  private String profileImageUrl;

  @SerializedName("display_name")
  private String displayName;

  @SerializedName("link")
  private String profileUrl;

  public User(int reputation, int userId, String userType, @Nullable Integer acceptRate,
      String profileImageUrl, String displayName, String profileUrl) {
    this.reputation = reputation;
    this.userId = userId;
    this.userType = userType;
    this.acceptRate = acceptRate;
    this.profileImageUrl = profileImageUrl;
    this.displayName = displayName;
    this.profileUrl = profileUrl;
  }

  public int getReputation() {
    return reputation;
  }

  public int getUserId() {
    return userId;
  }

  public String getUserType() {
    return userType;
  }

  public int getAcceptRate() {
    return acceptRate;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getProfileUrl() {
    return profileUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return reputation == user.reputation &&
        userId == user.userId &&
        userType.equals(user.userType) &&
        Objects.equals(acceptRate, user.acceptRate) &&
        profileImageUrl.equals(user.profileImageUrl) &&
        displayName.equals(user.displayName) &&
        profileUrl.equals(user.profileUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reputation, userId, userType, acceptRate, profileImageUrl, displayName,
        profileUrl);
  }
}
