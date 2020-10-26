package com.cloud.cmr.cloudcmrfunctions;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.verify.VerificationTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

@SpringBootTest
class ImportMembersFunctionTests {

    private static final String BUCKET_NAME = "bucket_test";
    private static final String FILE_NAME = "file_name.csv";

    private static ClientAndServer mockServer;

    @BeforeAll
    public static void startServer() {
        mockServer = startClientAndServer(1080);
    }

    @AfterAll
    public static void stopServer() {
        mockServer.stop();
    }

    @Autowired
    private ImportMembersFunction function;

    @MockBean
    private GcpStorageResourceLoader gcpStorageResourceLoader;

    @Test
    void test_basic() {
        createExpectationForRestCall();
        GscEvent gscEvent = new GscEvent();
        gscEvent.setBucket(BUCKET_NAME);
        gscEvent.setName(FILE_NAME);
        when(gcpStorageResourceLoader.loadResource(BUCKET_NAME, FILE_NAME))
                .thenReturn(getTestResource());

        function.gcsEvent().accept(gscEvent);

        verify(gcpStorageResourceLoader).loadResource(BUCKET_NAME, FILE_NAME);
        verifyPostRequest();
    }

    private void createExpectationForRestCall() {
        new MockServerClient("localhost", 1080)
                .when(
                        getMockRequest(),
                        exactly(1))
                .respond(
                        response()
                                .withStatusCode(201)
                                .withDelay(TimeUnit.SECONDS, 1)
                );
    }

    private void verifyPostRequest() {
        new MockServerClient("localhost", 1080).verify(
                getMockRequest(),
                VerificationTimes.exactly(1)
        );
    }

    private HttpRequest getMockRequest() {
        return request()
                .withMethod("POST")
                .withPath("/api/members/batch")
                .withHeader("\"Content-type\", \"application/json\"")
                .withBody(exact("[" +
                        "{\"firstName\":\"john\",\"lastName\":\"doe\"}," +
                        "{\"firstName\":\"alice\",\"lastName\":\"bob\"}" +
                        "]"));
    }

    private Resource getTestResource() {
        String s = "EMPTY_HEADER_LINE\n" +
                "PC11737989;;doe;john;M;1967;01/01/1967;1 chemin de la localisation;;Lieu-Dit;12345;Ma Ville;FRANCE;;;john.doe@mail.fr;NON;RCA;2018;26/01/2018;19:28:13;26/01/2018;14/10/2018;;PC\n" +
                "PC11737989;;bob;alice;M;1967;01/01/1967;1 chemin de la localisation;;Lieu-Dit;12345;Ma Ville;FRANCE;;;john.doe@mail.fr;NON;RCA;2018;26/01/2018;19:28:13;26/01/2018;14/10/2018;;PC\n";
        return new ByteArrayResource(s.getBytes());
    }
}
