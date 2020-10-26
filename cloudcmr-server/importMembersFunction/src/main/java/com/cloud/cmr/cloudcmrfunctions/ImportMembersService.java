package com.cloud.cmr.cloudcmrfunctions;

import com.cloud.cmr.domain.member.Gender;
import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.domain.member.MemberRepository;
import com.cloud.cmr.domain.member.PhoneNumber;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ImportMembersService {

    private static final DateTimeFormatter FRENCH_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final GcpStorageResourceLoader resourceLoader;
    private final MemberRepository memberRepository;
    private final Clock clock;

    public ImportMembersService(GcpStorageResourceLoader resourceLoader, MemberRepository memberRepository, Clock clock) {
        this.resourceLoader = resourceLoader;
        this.memberRepository = memberRepository;
        this.clock = clock;
    }

    public void importMemberFromGcpStorage(String bucket, String name) {
        Resource resource = resourceLoader.loadResource(bucket, name);
        try (InputStream inputStream = resource.getInputStream();
             InputStreamReader isr = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(isr)) {

            reader.lines()
                    .skip(1)
                    .forEach(line -> {
                        Member member = parseData(line);
                        memberRepository.save(member);
                    });
        } catch (IOException e) {
            throw new RuntimeException("Unable to read the file [" + resource.getFilename() + "]", e);
        }
    }

    private Member parseData(String line) {
        String[] data = line.split(";");
        String id = memberRepository.nextId();
        String lastName = data[2];
        String firstName = data[3];
        String email = data[15];
        LocalDate birthDate = LocalDate.parse(data[6], FRENCH_FORMAT);
        Gender gender = "M".equals(data[4]) ? Gender.MALE : Gender.FEMALE;
        PhoneNumber phone = new PhoneNumber(data[13]);
        PhoneNumber mobile = new PhoneNumber(data[14]);
        String creator = "ImportMembersService";
        Instant createdAt = clock.instant();
        Member member = new Member(id,
                lastName,
                firstName,
                email,
                birthDate,
                gender,
                phone,
                mobile,
                creator,
                createdAt);
        return member;
    }
}
