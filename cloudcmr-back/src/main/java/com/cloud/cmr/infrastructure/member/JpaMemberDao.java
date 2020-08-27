package com.cloud.cmr.infrastructure.member;

import com.cloud.cmr.domain.member.Member;
import org.springframework.data.repository.CrudRepository;

public interface JpaMemberDao extends CrudRepository<Member, String> {
}
