package ru.otus.hw09.core.model;

import lombok.Data;
import ru.otus.hw09.orm.Id;

@Data
public class User {
  @Id
  private long id;
  private String name;
  private int age;

  public User(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public User() {
  }

}
