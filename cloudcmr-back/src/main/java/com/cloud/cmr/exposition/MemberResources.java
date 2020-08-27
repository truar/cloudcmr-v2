package com.cloud.cmr.exposition;

import com.cloud.cmr.application.member.MemberApplicationService;
import com.cloud.cmr.domain.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/members")
public class MemberResources {

    private MemberApplicationService memberApplicationService;

    public MemberResources(MemberApplicationService memberApplicationService) {
        this.memberApplicationService = memberApplicationService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createMember(@RequestBody CreateMemberDTO createMemberDTO, Principal principal) {
        String memberId = memberApplicationService.create(createMemberDTO.lastName, createMemberDTO.firstName, createMemberDTO.email, principal.getName());
        return ResponseEntity.created(buildMemberLocation(memberId)).build();
    }

    private URI buildMemberLocation(String memberId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/members/{memberId}")
                .buildAndExpand(memberId)
                .toUri();
    }

    @GetMapping("/{memberId}")
    public MemberDTO getMember(@PathVariable String memberId) {
        Member member = memberApplicationService.memberOfId(memberId);
        return toMemberDTO(member);
    }

    private MemberDTO toMemberDTO(Member member) {
        return new MemberDTO(member.getLastName(), member.getFirstName(), member.getEmail(), member.getCreatedAt(), member.getCreator());
    }
}
