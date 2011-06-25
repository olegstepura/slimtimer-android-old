package com.jaanussiim.slimtimer.android.database;

public enum SettingKey {
  KEY_USERNAME(1), KEY_PASSWORD(2);

  private final Integer index;

  SettingKey(Integer index) {
    this.index = index;
  }

  public Integer getIndex() {
    return index;
  }
}
