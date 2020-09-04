package com.cloud.cmr.domain.member;

import com.cloud.cmr.domain.common.Page;

public interface MemberRepository {
    void save(Member member);

    String nextId();

    Member findById(String memberId);

    Page<Member> findWithFilter(Integer page, Integer pageSize, String sortBy, String sortOrder);
}
