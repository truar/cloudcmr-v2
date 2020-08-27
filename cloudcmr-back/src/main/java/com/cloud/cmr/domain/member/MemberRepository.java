package com.cloud.cmr.domain.member;

public interface MemberRepository {
    void save(Member member);

    String nextId();

    Member findById(String memberId);
}
