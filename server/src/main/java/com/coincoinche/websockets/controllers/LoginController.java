package com.coincoinche.websockets.controllers;

import com.coincoinche.model.User;
import com.coincoinche.repositories.UserRepository;
import com.coincoinche.websockets.events.EventType;
import com.coincoinche.websockets.events.LogInEvent;
import com.coincoinche.websockets.events.SignUpEvent;
import com.coincoinche.websockets.messages.InvalidEventMessage;
import com.coincoinche.websockets.messages.LoggedInMessage;
import com.coincoinche.websockets.messages.Message;
import com.coincoinche.websockets.messages.UserCreatedMessage;
import com.coincoinche.websockets.messages.UserExistsMessage;
import com.coincoinche.websockets.messages.WrongPasswordMessage;
import com.coincoinche.websockets.messages.WrongUsernameMessage;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {
  private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

  @Autowired private UserRepository userRepository;

  /**
   * Endpoint called to log the player in.
   *
   * @param event - incoming event.
   * @return message acknowledging reception.
   */
  @MessageMapping("/login")
  @SendTo("/topic/login")
  public Message logIn(@Payload LogInEvent event) {
    String username = event.getUsername();
    logger.debug("Received event {}, username {}", event.getType(), username);

    if (!event.getType().equals(EventType.LOG_IN)) {
      return new InvalidEventMessage();
    }

    List<User> users = this.userRepository.findByUsername(username);
    if (users.size() == 0) {
      return new WrongUsernameMessage();
    }
    User user = users.get(0);
    if (user.getPassword().equals(event.getPassword())) {
      return new LoggedInMessage();
    }

    return new WrongPasswordMessage();
  }

  /**
   * Endpoint called to sign up a new player.
   *
   * @param event - incoming event.
   * @return message acknowledging reception.
   */
  @MessageMapping("/signup")
  @SendTo("/topic/signup")
  public Message signUp(@Payload SignUpEvent event) {
    String username = event.getUsername();
    logger.debug("Received event {}, username {}", event.getType(), username);

    if (!event.getType().equals(EventType.SIGN_UP)) {
      return new InvalidEventMessage();
    }

    List<User> users = this.userRepository.findByUsername(username);
    if (users.size() != 0) {
      return new UserExistsMessage();
    }
    // Create user.
    this.userRepository.save(new User(event.getUsername(), event.getPassword()));
    return new UserCreatedMessage();
  }
}
