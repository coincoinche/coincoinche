package com.coincoinche.ratingplayer;

import com.coincoinche.model.User;

/** Wrapper around User to implement a player rated with Elo. */
public class EloPlayer {

  private User user;

  public EloPlayer(User user) {
    this.user = user;
  }

  public int getRating() {
    return user.getRating();
  }

  public void setRating(int rating) {
    user.setRating(rating);
  }

  public int getRatingAdjustment() {
    return user.getRatingAdjustment();
  }

  public void setRatingAdjustment(int ratingAdjustment) {
    user.setRatingAdjustment(ratingAdjustment);
  }

  public User getUser() {
    return user;
  }
}
