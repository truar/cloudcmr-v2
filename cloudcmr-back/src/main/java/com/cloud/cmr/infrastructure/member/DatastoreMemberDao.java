package com.cloud.cmr.infrastructure.member;

import com.cloud.cmr.domain.member.Member;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.List;

public interface DatastoreMemberDao extends DatastoreRepository<Member, String> {
    List<Member> findAllByOrderByLastNameAscFirstNameAsc();
}
