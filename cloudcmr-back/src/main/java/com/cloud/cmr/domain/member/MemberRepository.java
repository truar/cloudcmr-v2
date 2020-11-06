package com.cloud.cmr.domain.member;

import com.cloud.cmr.domain.common.Page;

import java.time.LocalDate;
import java.util.Optional;

public interface MemberRepository {
    void save(Member member);

    String nextId();

    Member findById(String memberId);

    Page<Member> find(Integer page, Integer pageSize, String sortBy, String sortOrder);

    Optional<Member> findByLastNameIgnoreCaseAndFirstNameIgnoreCaseAndBirthDate(String lastName, String firstName, LocalDate birthDate);
}
