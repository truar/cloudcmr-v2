package com.cloud.cmr.cloudcmrfunctions;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class RestMemberGateway implements MemberGateway {

    @Value("${service.members.batch.url}")
    private URI memberServiceUrl;

    @Override
    public void send(List<MemberRest> members) {

        Gson gson = new Gson();
        String jsonInString = gson.toJson(members);

        System.out.println(jsonInString);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(memberServiceUrl)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInString))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .join();
    }
}
