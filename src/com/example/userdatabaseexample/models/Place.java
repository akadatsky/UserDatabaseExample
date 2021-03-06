package com.example.userdatabaseexample.models;

import com.mobindustry.cursormapper.Column;

public class Place {

  @Column("_id")
  private String id;
  private String descriprion;
  private int score;

  public Place(String descriprion, int score) {
    this.descriprion = descriprion;
    this.score = score;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDescriprion() {
    return descriprion;
  }

  public void setDescriprion(String descriprion) {
    this.descriprion = descriprion;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  @Override
  public String toString() {
    return "Place{" +
        "id='" + id + '\'' +
        ", descriprion='" + descriprion + '\'' +
        ", score=" + score +
        '}';
  }
}
