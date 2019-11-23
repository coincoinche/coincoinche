package com.coincoinche.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user", schema = "public")
public class User {
  private static final int DEFAULT_RATING = 1000;
  private static final int DEFAULT_RATING_ADJUSTMENT = 32;

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(columnDefinition = "VARCHAR(25)")
  private String username;

  @Column(columnDefinition = "VARCHAR(25)")
  private String password;

  @Column(columnDefinition = "integer")
  private int rating;

  @Column(columnDefinition = "integer", name = "rating_adjustment")
  private int ratingAdjustment;

  protected User() {
    super();
  }

  /**
   * Create a new user.
   *
   * @param username is the new user's username.
   */
  public User(String username) {
    this.username = username;
    this.password = "pass";
    this.rating = DEFAULT_RATING;
    this.ratingAdjustment = DEFAULT_RATING_ADJUSTMENT;
  }

  /**
   * Create a new user.
   *
   * @param username is the new user's username.
   * @param password is the new user's password.
   */
  public User(String username, String password) {
    this.username = username;
    this.password = password;
    this.rating = DEFAULT_RATING;
    this.ratingAdjustment = DEFAULT_RATING_ADJUSTMENT;
  }
  public UUID getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public int getRating() {
    return rating;
  }

  public int getRatingAdjustment() {
    return ratingAdjustment;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public void setRatingAdjustment(int ratingAdjustment) {
    this.ratingAdjustment = ratingAdjustment;
  }
}
