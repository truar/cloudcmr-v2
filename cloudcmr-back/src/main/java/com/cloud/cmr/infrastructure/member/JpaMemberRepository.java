package com.cloud.cmr.infrastructure.member;

import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.domain.member.MemberRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class JpaMemberRepository implements MemberRepository {
    private JpaMemberDao jpaMemberDao;

    public JpaMemberRepository(JpaMemberDao jpaMemberDao) {
        this.jpaMemberDao = jpaMemberDao;
    }

    @Override
    public void save(Member member) {
        jpaMemberDao.save(member);
    }

    @Override
    public String nextId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Member findById(String memberId) {
        return jpaMemberDao.findById(memberId)
                .orElseThrow();
    }

    @Override
    public List<Member> findAll() {
        return jpaMemberDao.findAllByOrderByLastNameAscFirstNameAsc();
    }
}
