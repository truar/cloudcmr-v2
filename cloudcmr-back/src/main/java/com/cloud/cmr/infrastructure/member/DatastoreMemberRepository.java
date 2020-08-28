package com.cloud.cmr.infrastructure.member;

import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.domain.member.MemberRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class DatastoreMemberRepository implements MemberRepository {
    private DatastoreMemberDao datastoreMemberDao;

    public DatastoreMemberRepository(DatastoreMemberDao datastoreMemberDao) {
        this.datastoreMemberDao = datastoreMemberDao;
    }

    @Override
    public void save(Member member) {
        datastoreMemberDao.save(member);
    }

    @Override
    public String nextId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Member findById(String memberId) {
        return datastoreMemberDao.findById(memberId)
                .orElseThrow();
    }

    @Override
    public List<Member> findAll() {
        return datastoreMemberDao.findAllByOrderByLastNameAscFirstNameAsc();
    }
}
