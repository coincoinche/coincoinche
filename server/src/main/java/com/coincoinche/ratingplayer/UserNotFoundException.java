package com.coincoinche.ratingplayer;

/**
 * UserNotFoundException is thrown when we try to get a user by its username but no user was found.
 */
public class UserNotFoundException extends Exception {

  private static final long serialVersionUID = 6174990858430620881L;

  public UserNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
