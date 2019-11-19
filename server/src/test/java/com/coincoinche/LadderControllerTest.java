package com.coincoinche;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coincoinche.model.User;
import com.coincoinche.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

/** Unit tests for the Ladder controller. */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LadderControllerTest {

  @Autowired private MockMvc mvc;
  @Autowired private UserRepository userRepository;

  @Before
  public void setUp() {
    // create users
    User u1 = new User("u1");
    u1.setRating(1500);
    User u2 = new User("u2");
    u2.setRating(2000);
    User u3 = new User("u3");
    u3.setRating(1700);
    User u4 = new User("u4");
    u4.setRating(800);
    userRepository.save(u1);
    userRepository.save(u2);
    userRepository.save(u3);
    userRepository.save(u4);
  }

  @Test
  public void getLadder() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/ladder").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .string(
                    equalTo(
                        "[{\"username\":\"u2\",\"rating\":2000},{\"username\":\"u3\",\"rating\":1700},{\"username\":\"u1\",\"rating\":1500},{\"username\":\"u4\",\"rating\":800}]")));
  }
}
