package com.example.userdatabaseexample.models;


import com.mobindustry.cursormapper.Column;

import java.util.Date;

public class User {

  @Column("_id")
  private String id;
  private String name;
  @Column("second_name")
  private String secondName;
  @Column("created_at")
  private Date createdAt;

  public User(String name, String secondName) {
    this.name = name;
    this.secondName = secondName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", secondName='" + secondName + '\'' +
        ", createdAt=" + createdAt +
        '}';
  }
}
