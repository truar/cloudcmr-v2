package com.cloud.cmr.application.member;

import com.cloud.cmr.domain.member.*;
import com.cloud.cmr.domain.common.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class MemberApplicationService {

    private MemberRepository memberRepository;
    private Clock clock;

    public MemberApplicationService(MemberRepository memberRepository, Clock clock) {
        this.memberRepository = memberRepository;
        this.clock = clock;
    }

    @Transactional
    public String create(String lastName, String firstName, String email, String gender, LocalDate birthDate, String phone, String mobile, String creator) {
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

    @Transactional(readOnly = true)
    public Member memberOfId(String memberId) {
        return memberRepository.findById(memberId);
    }

    @Transactional(readOnly = true)
    public Page<Member> findMembers(Integer page, Integer pageSize, String sortBy, String sortOrder) {
        return memberRepository.find(page, pageSize, sortBy, sortOrder);
    }
}
