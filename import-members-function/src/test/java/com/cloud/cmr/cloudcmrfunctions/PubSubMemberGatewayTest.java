package com.cloud.cmr.cloudcmrfunctions;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PubSubMemberGatewayTest {

    @Test
    void publish_a_message_in_a_correct_json() throws ExecutionException, InterruptedException {
        MemberAddressDTO member = new MemberAddressDTO("PC11737989", "john", "doe", "M",
                LocalDate.of(1967, 1, 1), "john.doe@mail.fr",
                "0102030405", "0601020304",
                "1 chemin de la localisation", "",
                "Lieu-Dit", "12345", "Ma Ville");
        String topic = "topic";
        PubSubTemplate pubSubTemplate = mock(PubSubTemplate.class);
        when(pubSubTemplate.publish(topic, member)).thenAnswer(invocation -> {
            ListenableFutureTask<String> task = new ListenableFutureTask<>(() -> "1");
            task.run();
            return task;
        });
        MemberGateway gateway = new PubSubMemberGateway(pubSubTemplate, topic);

        ListenableFuture<String> result = gateway.send(member);
        assertThat(result.get()).isEqualTo("1");
        verify(pubSubTemplate).publish(topic, member);
    }
}