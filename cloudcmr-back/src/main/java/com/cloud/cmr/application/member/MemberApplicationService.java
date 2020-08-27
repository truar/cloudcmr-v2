package com.cloud.cmr.application.member;

import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.domain.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class MemberApplicationService {

    private MemberRepository memberRepository;
    private Clock clock;

    public MemberApplicationService(MemberRepository memberRepository, Clock clock) {
        this.memberRepository = memberRepository;
        this.clock = clock;
    }

    public String create(String lastName, String firstName, String email, String creator) {
        String id = memberRepository.nextId();
        Member member = new Member(id, lastName, firstName, email, creator, clock.instant());
        memberRepository.save(member);
        return id;
    }

    public Member memberOfId(String memberId) {
        return memberRepository.findById(memberId);
    }
}
