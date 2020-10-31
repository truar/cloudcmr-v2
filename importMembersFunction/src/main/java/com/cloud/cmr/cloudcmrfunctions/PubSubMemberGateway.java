package com.cloud.cmr.cloudcmrfunctions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.support.converter.JacksonPubSubMessageConverter;
import org.springframework.stereotype.Component;

@Component
public class PubSubMemberGateway implements MemberGateway {

    private final PubSubTemplate pubSubTemplate;
    private final String topic;

    public PubSubMemberGateway(PubSubTemplate pubSubTemplate,
                               @Value("${pubsub.gateway.topic}") String topic) {
        this.pubSubTemplate = pubSubTemplate;
        this.pubSubTemplate.setMessageConverter(new JacksonPubSubMessageConverter(new ObjectMapper()));
        this.topic = topic;
    }

    @Override
    public void send(MemberDTO member) {
        System.out.println("member = " + member);
        this.pubSubTemplate.publish(topic, member);
    }
}
