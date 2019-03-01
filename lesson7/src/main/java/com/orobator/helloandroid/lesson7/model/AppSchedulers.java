package com.orobator.helloandroid.lesson7.model;

import io.reactivex.Scheduler;

public class AppSchedulers {
  public final Scheduler main;
  public final Scheduler io;

  public AppSchedulers(Scheduler main, Scheduler io) {
    this.main = main;
    this.io = io;
  }
}
