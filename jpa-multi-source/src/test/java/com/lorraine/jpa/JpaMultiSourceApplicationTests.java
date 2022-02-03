package com.lorraine.jpa;

import com.lorraine.jpa.primary.User;
import com.lorraine.jpa.primary.UserRepository;
import com.lorraine.jpa.secondary.Message;
import com.lorraine.jpa.secondary.MessageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JpaMultiSourceApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void test() {
        userRepository.deleteAll();
        userRepository.save(new User("John", 10));
        userRepository.save(new User("Allen", 12));

        Assertions.assertEquals(2, userRepository.findAll().size());

        messageRepository.deleteAll();
        messageRepository.save(new Message("New message", "hello, John"));
        messageRepository.save(new Message("New message", "hello, Allen"));
        Assertions.assertEquals(2, messageRepository.findAll().size());
    }

}
