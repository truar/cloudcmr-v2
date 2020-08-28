package com.cloud.cmr.infrastructure.member;

import com.cloud.cmr.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaMemberDao extends JpaRepository<Member, String> {
    List<Member> findAllByOrderByLastNameAscFirstNameAsc();
}
