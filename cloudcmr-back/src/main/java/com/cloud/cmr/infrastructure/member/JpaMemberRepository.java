package com.cloud.cmr.infrastructure.member;

import com.cloud.cmr.domain.member.Member;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface JpaMemberRepository extends PagingAndSortingRepository<Member, Long> {
}
