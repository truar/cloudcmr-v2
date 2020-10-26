package com.cloud.cmr.cloudcmrfunctions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ImportMembersFunctionTests {

    private static final String BUCKET_NAME = "bucket_test";
    private static final String FILE_NAME = "file_name.csv";

    @Autowired
    private ImportMembersFunction function;

    @Autowired
    private ApplicationContext context;

    @MockBean
    private GcpStorageResourceLoader gcpStorageResourceLoader;

    // Be careful, this test is based on a resource presents in the test/resources folder
    @Test
    void test_basic() {
        GscEvent gscEvent = new GscEvent();
        gscEvent.setBucket(BUCKET_NAME);
        gscEvent.setName(FILE_NAME);
        when(gcpStorageResourceLoader.loadResource(BUCKET_NAME, FILE_NAME))
                .thenReturn(getTestResource());

        function.gcsEvent().accept(gscEvent);

        verify(gcpStorageResourceLoader).loadResource(BUCKET_NAME, FILE_NAME);
    }

    private Resource getTestResource() {
        return context.getResource("classpath:bucket_test/file_name.csv");
    }
}
