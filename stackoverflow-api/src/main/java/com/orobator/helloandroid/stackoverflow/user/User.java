package com.orobator.helloandroid.stackoverflow.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import io.reactivex.annotations.Nullable;
import java.util.Objects;

public class User implements Parcelable {
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

  protected User(Parcel in) {
    reputation = in.readInt();
    userId = in.readInt();
    userType = in.readString();
    if (in.readByte() == 0) {
      acceptRate = null;
    } else {
      acceptRate = in.readInt();
    }
    profileImageUrl = in.readString();
    displayName = in.readString();
    profileUrl = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(reputation);
    dest.writeInt(userId);
    dest.writeString(userType);
    if (acceptRate == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(acceptRate);
    }
    dest.writeString(profileImageUrl);
    dest.writeString(displayName);
    dest.writeString(profileUrl);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<User> CREATOR = new Creator<User>() {
    @Override
    public User createFromParcel(Parcel in) {
      return new User(in);
    }

    @Override
    public User[] newArray(int size) {
      return new User[size];
    }
  };

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
