package com.orobator.helloandroid.lesson10;

import android.view.View;
import android.widget.ImageView;
import androidx.databinding.BindingAdapter;
import com.bumptech.glide.Glide;

public class BindingAdapters {
  @BindingAdapter({ "imgUrl" })
  public static void loadImage(ImageView imageView, String url) {
    Glide.with(imageView).load(url).into(imageView);
  }

  @BindingAdapter({ "isVisible" })
  public static void setVisibility(View view, boolean visible) {
    view.setVisibility(visible ? View.VISIBLE : View.GONE);
  }
}
