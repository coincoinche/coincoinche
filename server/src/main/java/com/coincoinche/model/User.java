package com.coincoinche.model;

import java.util.UUID;
import javax.persistence.*;

@Entity
@Table(name = "user", schema = "public")
public class User {
  public User(String username) {
    this.username = username;
  }

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(columnDefinition = "VARCHAR(25)")
  private String username;

  public UUID getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
