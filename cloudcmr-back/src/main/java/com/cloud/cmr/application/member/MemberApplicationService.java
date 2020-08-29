package com.cloud.cmr.application.member;

import com.cloud.cmr.domain.member.Gender;
import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.domain.member.MemberRepository;
import com.cloud.cmr.domain.member.PhoneNumber;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;

@Service
public class MemberApplicationService {

    private MemberRepository memberRepository;
    private Clock clock;

    public MemberApplicationService(MemberRepository memberRepository, Clock clock) {
        this.memberRepository = memberRepository;
        this.clock = clock;
    }

    public String create(String lastName, String firstName, String email, String gender, String phone, String mobile, String creator) {
        String id = memberRepository.nextId();
        Member member = new Member(id, lastName, firstName, email, Gender.valueOf(gender), new PhoneNumber(phone), new PhoneNumber(mobile), creator, clock.instant());
        memberRepository.save(member);
        return id;
    }

    public Member memberOfId(String memberId) {
        return memberRepository.findById(memberId);
    }

    public List<Member> allMembers() {
        return memberRepository.findAll();
    }
}
