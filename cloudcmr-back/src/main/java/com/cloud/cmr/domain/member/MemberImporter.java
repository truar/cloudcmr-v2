package com.cloud.cmr.domain.member;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class MemberImporter {
    public static final String IMPORT_MEMBER_SERVICE = "importMemberService";
    private MemberRepository memberRepository;
    private Clock clock;

    public MemberImporter(MemberRepository memberRepository, Clock clock) {
        this.memberRepository = memberRepository;
        this.clock = clock;
    }

    public Member importMemberFromExternalSource(String licenceNumber, String lastName, String firstName, String email,
                                                 LocalDate birthDate, String gender, String phone, String mobile,
                                                 String line1, String line2, String line3, String zipCode, String city) {
        Member member = memberRepository.findByLastNameAndFirstNameAndBirthDate(lastName, firstName, birthDate)
                .orElseGet(() -> createNewMemberFromExternalData(licenceNumber, lastName, firstName, email, birthDate,
                        gender, phone, mobile, line1, line2, line3, zipCode, city));

        member.updateExternalData(new MemberExternalData(
                licenceNumber, lastName, firstName, email, birthDate, gender, phone, mobile, line1, line2, line3, zipCode, city
        ));

        return member;
    }

    private Member createNewMemberFromExternalData(String licenceNumber, String lastName, String firstName, String email, LocalDate birthDate, String gender, String phone, String mobile, String line1, String line2, String line3, String zipCode, String city) {
        Member newMember = new Member(memberRepository.nextId(), lastName, firstName, email, birthDate, Gender.valueOf(gender),
                new PhoneNumber(phone), new PhoneNumber(mobile), IMPORT_MEMBER_SERVICE, clock.instant());
        newMember.changeAddress(line1, line2, line3, city, zipCode);
        newMember.updateExternalData(new MemberExternalData(
                licenceNumber, lastName, firstName, email, birthDate, gender, phone, mobile, line1, line2, line3, zipCode, city
        ));
        return newMember;
    }
}
