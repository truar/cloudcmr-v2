package com.cloud.cmr.infrastructure.member;

import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.domain.member.MemberRepository;
import com.cloud.cmr.domain.common.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import java.util.UUID;

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
    public Page<Member> findWithFilter(Integer page, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageRequest = PageRequest.of(page - 1, pageSize, Direction.valueOf(sortOrder), sortBy);
        org.springframework.data.domain.Page<Member> pagedMembers = datastoreMemberDao.findAll(pageRequest);
        return new Page<>(pagedMembers.getTotalElements(), pagedMembers.get().collect(toList()));
    }
}
