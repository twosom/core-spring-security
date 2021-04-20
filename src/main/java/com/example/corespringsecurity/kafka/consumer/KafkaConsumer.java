package com.example.corespringsecurity.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "jwt")
    public void getJwt(String kafkaMessage) {
        log.info("kafkaMessage = {}",kafkaMessage);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = requestAttributes.getRequest().getSession(true);


        if (StringUtils.hasText(kafkaMessage)) {
            session.setAttribute("jwt", kafkaMessage);
        }
    }
}
