package com.cloud.cmr.exposition.member;

import com.cloud.cmr.application.member.MemberApplicationService;
import com.cloud.cmr.domain.member.Address;
import com.cloud.cmr.domain.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/members")
public class MemberResources {

    private MemberApplicationService memberApplicationService;

    public MemberResources(MemberApplicationService memberApplicationService) {
        this.memberApplicationService = memberApplicationService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createMember(@RequestBody CreateMemberRequest createMemberRequest, Principal principal) {
        String memberId = memberApplicationService.create(createMemberRequest.lastName, createMemberRequest.firstName,
                createMemberRequest.email, createMemberRequest.gender, createMemberRequest.phone,
                createMemberRequest.mobile, principal.getName());
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

    @PostMapping("/{memberId}/changeAddress")
    public void getMember(@PathVariable String memberId, @RequestBody ChangeAddressRequest changeAddressRequest) {
        memberApplicationService.changeAddressOfMember(memberId, changeAddressRequest.line1, changeAddressRequest.line2, changeAddressRequest.line3, changeAddressRequest.city, changeAddressRequest.zipCode);
    }

    @GetMapping
    public MemberListDTO getAllMembers() {
        List<Member> members = memberApplicationService.allMembers();
        return toMemberListDTO(members);
    }

    private MemberListDTO toMemberListDTO(List<Member> members) {
        List<MemberDTO> membersDTO = members.stream()
                .map(this::toMemberDTO)
                .collect(toList());
        return new MemberListDTO(membersDTO);
    }

    private MemberDTO toMemberDTO(Member member) {
        AddressDTO addressDTO = toAddressDTO(member.getAddress());
        return new MemberDTO(member.getLastName(), member.getFirstName(), member.getEmail(), member.getGender().name(),
                member.getPhone(), member.getMobile(), addressDTO, member.getCreatedAt(), member.getCreator());
    }

    private AddressDTO toAddressDTO(Address address) {
        if(address == null) {
            return null;
        }
        return new AddressDTO(address.getLine1(), address.getLine2(), address.getLine3(), address.getCity(), address.getZipCode());
    }
}
