package com.cloud.cmr.messaging.member;

import com.cloud.cmr.application.member.ImportMemberCommand;
import com.cloud.cmr.application.member.MemberManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.pubsub.v1.PubsubMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.converter.JacksonPubSubMessageConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MemberSubscription {

    private final PubSubTemplate pubSubTemplate;
    private final MemberManager memberManager;
    @Value("${subscription.name}")
    private String subscriptionName;

    public MemberSubscription(PubSubTemplate pubSubTemplate, MemberManager memberManager) {
        this.pubSubTemplate = pubSubTemplate;
        this.memberManager = memberManager;
        this.pubSubTemplate.setMessageConverter(new JacksonPubSubMessageConverter(new ObjectMapper()));
    }

    @PostConstruct
    public void configureSubscription() {
        this.pubSubTemplate.subscribe(subscriptionName, this::handleMessage);
    }

    private void handleMessage(BasicAcknowledgeablePubsubMessage basicAcknowledgeablePubsubMessage) {
        System.out.println("A message has been received");
        PubsubMessage pubsubMessage = basicAcknowledgeablePubsubMessage.getPubsubMessage();
        MemberExternalDataDTO memberExternalDataDTO = this.pubSubTemplate.getMessageConverter().fromPubSubMessage(pubsubMessage, MemberExternalDataDTO.class);
        System.out.println("Object extracted from the message : " + memberExternalDataDTO);
        memberManager.importMember(new ImportMemberCommand(
                memberExternalDataDTO.getLicenceNumber(),
                memberExternalDataDTO.getLastName(),
                memberExternalDataDTO.getFirstName(),
                memberExternalDataDTO.getEmail(),
                memberExternalDataDTO.getBirthDate(),
                memberExternalDataDTO.getGender().equals("M") ? "MALE" : "FEMALE",
                memberExternalDataDTO.getPhone(),
                memberExternalDataDTO.getMobile(),
                memberExternalDataDTO.getLine1(),
                memberExternalDataDTO.getLine2(),
                memberExternalDataDTO.getLine3(),
                memberExternalDataDTO.getZipCode(),
                memberExternalDataDTO.getCity()
        ));
        System.out.println("Member successfully imported");
        // Ack the message
        basicAcknowledgeablePubsubMessage.ack()
                .completable()
                .join();
        System.out.println("Message has been acked");
    }
}
