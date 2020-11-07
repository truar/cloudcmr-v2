package com.cloud.cmr.application.member;

import com.cloud.cmr.domain.member.*;
import com.cloud.cmr.domain.common.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class MemberManager {

    private final MemberRepository memberRepository;
    private final Clock clock;
    private MemberImporter memberImporter;

    public MemberManager(MemberRepository memberRepository, Clock clock, MemberImporter memberImporter) {
        this.memberRepository = memberRepository;
        this.clock = clock;
        this.memberImporter = memberImporter;
    }

    @Transactional
    public String createMember(String lastName, String firstName, String email, String gender, LocalDate birthDate, String phone, String mobile, String creator) {
        String id = memberRepository.nextId();
        Member member = new Member(id, lastName, firstName, email, birthDate, Gender.valueOf(gender), new PhoneNumber(phone), new PhoneNumber(mobile), creator, clock.instant());
        memberRepository.save(member);
        return id;
    }

    @Transactional
    public void changeMemberContactInformation(String memberId, String lastName, String firstName, String email, String gender, LocalDate birthDate, String phone, String mobile) {
        Member member = memberRepository.findById(memberId);
        member.changeContactInformation(lastName, firstName, email, birthDate, Gender.valueOf(gender), new PhoneNumber(phone), new PhoneNumber(mobile));
        memberRepository.save(member);
    }

    @Transactional
    public void changeMemberAddress(String memberId, String line1, String line2, String line3, String city, String zipCode) {
        Member member = memberRepository.findById(memberId);
        member.changeAddress(line1, line2, line3, city, zipCode);
        memberRepository.save(member);
    }

    @Transactional
    public void importMember(ImportMemberCommand importMemberCommand) {
        Member member = memberImporter.importMemberFromExternalSource(
                importMemberCommand.getLicenceNumber(), importMemberCommand.getLastName(),
                importMemberCommand.getFirstName(),
                importMemberCommand.getEmail(),
                importMemberCommand.getBirthDate(),
                importMemberCommand.getGender(),
                importMemberCommand.getPhone(),
                importMemberCommand.getMobile(),
                importMemberCommand.getLine1(),
                importMemberCommand.getLine2(),
                importMemberCommand.getLine3(),
                importMemberCommand.getZipCode(),
                importMemberCommand.getCity());
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member memberOfId(String memberId) {
        return memberRepository.findById(memberId);
    }

    @Transactional(readOnly = true)
    public Page<Member> findMembers(Integer page, Integer pageSize, String sortBy, String sortOrder) {
        return memberRepository.find(page, pageSize, sortBy, sortOrder);
    }
}
