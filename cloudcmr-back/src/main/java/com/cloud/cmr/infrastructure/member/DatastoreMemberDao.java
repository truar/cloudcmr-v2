package com.cloud.cmr.infrastructure.member;

import com.cloud.cmr.domain.member.Member;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DatastoreMemberDao extends DatastoreRepository<Member, String> {
    Optional<Member> findByLastNameAndFirstNameAndBirthDate(String lastName, String firstName, LocalDate birthDate);
}
