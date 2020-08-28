package com.cloud.cmr.domain.member;

import java.util.List;

public interface MemberRepository {
    void save(Member member);

    String nextId();

    Member findById(String memberId);

    List<Member> findAll();
}
