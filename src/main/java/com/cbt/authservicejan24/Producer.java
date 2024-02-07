package com.cbt.authservicejan24;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer
{
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "auth-events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendAuthEvent(String type, String username) throws JsonProcessingException {
        AuthEvent authEvent = new AuthEvent();
        authEvent.setType(type);
        authEvent.setUsername(username);

        ObjectMapper objectMapper = new ObjectMapper();
        String datum =  objectMapper.writeValueAsString(authEvent);

        logger.info(String.format("#### -> Producing message -> %s", datum));
        this.kafkaTemplate.send(TOPIC,"auth-key-1", datum);
    }

}
