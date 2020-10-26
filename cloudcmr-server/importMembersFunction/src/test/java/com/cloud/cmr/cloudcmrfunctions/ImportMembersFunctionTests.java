package com.cloud.cmr.cloudcmrfunctions;

import com.cloud.cmr.domain.member.Gender;
import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.domain.member.MemberRepository;
import com.cloud.cmr.domain.member.PhoneNumber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.refEq;
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

    @MockBean
    private Clock clock;

    @MockBean
    private MemberRepository memberRepository;

    // Be careful, this test is based on a resource presents in the test/resources folder
    @Test
    void test_basic() {
        when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
        when(memberRepository.nextId()).thenReturn("1");
        GscEvent gscEvent = new GscEvent();
        gscEvent.setBucket(BUCKET_NAME);
        gscEvent.setName(FILE_NAME);
        when(gcpStorageResourceLoader.loadResource(BUCKET_NAME, FILE_NAME))
                .thenReturn(getTestResource());

        function.gcsEvent().accept(gscEvent);

        verify(gcpStorageResourceLoader).loadResource(BUCKET_NAME, FILE_NAME);
        verify(memberRepository).nextId();
        verify(memberRepository).save(refEq(new Member("1",
                "DOE",
                "John",
                "john.doe@mail.fr",
                LocalDate.of(1967, 1, 1),
                Gender.MALE,
                new PhoneNumber(""),
                new PhoneNumber(""),
                "ImportMembersService",
                clock.instant())));
    }

    private Resource getTestResource() {
        return context.getResource("classpath:bucket_test/file_name.csv");
    }
}
