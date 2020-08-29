package com.cloud.cmr.infrastructure.member;

import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.domain.member.MemberRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

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
        return StreamSupport.stream(datastoreMemberDao.findAll(Sort.by(Sort.Order.asc("lastName"))).spliterator(), false)
                .collect(toList());
    }
}
