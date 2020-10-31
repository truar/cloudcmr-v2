package com.cloud.cmr.cloudcmrfunctions;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PubSubMemberGatewayTest {

    @Test
    void publish_a_message_in_a_correct_json() {
        MemberDTO member = new MemberDTO("PC11737989", "john", "doe", "M",
                LocalDate.of(1967, 1, 1), "john.doe@mail.fr",
                "0102030405", "0601020304",
                new AddressDTO("1 chemin de la localisation", "",
                        "Lieu-Dit", "12345", "Ma Ville"));
        PubSubTemplate pubSubTemplate = mock(PubSubTemplate.class);
        String topic = "topic";
        MemberGateway gateway = new PubSubMemberGateway(pubSubTemplate, topic);

        gateway.send(member);

        verify(pubSubTemplate).publish(topic, member);
    }
}