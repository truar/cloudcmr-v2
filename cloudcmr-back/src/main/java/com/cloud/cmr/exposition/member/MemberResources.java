package com.cloud.cmr.exposition.member;

import com.cloud.cmr.application.member.ImportMemberCommand;
import com.cloud.cmr.application.member.MemberManager;
import com.cloud.cmr.domain.common.Page;
import com.cloud.cmr.domain.member.Address;
import com.cloud.cmr.domain.member.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/members")
public class MemberResources {

    private static Logger logger = LoggerFactory.getLogger(MemberResources.class);

    private final MemberManager memberManager;

    public MemberResources(MemberManager memberManager) {
        this.memberManager = memberManager;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createMember(@RequestBody CreateMemberRequest createMemberRequest, Principal principal) {
        String memberId = memberManager.createMember(createMemberRequest.lastName, createMemberRequest.firstName,
                createMemberRequest.email, createMemberRequest.gender, createMemberRequest.birthDate,
                createMemberRequest.phone, createMemberRequest.mobile, principal.getName());
        return ResponseEntity.created(buildMemberLocation(memberId)).build();
    }

    private URI buildMemberLocation(String memberId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/members/{memberId}")
                .buildAndExpand(memberId)
                .toUri();
    }

    @PostMapping("/import")
    @ResponseStatus(NO_CONTENT)
    public void importMember(@RequestBody Body body) {
        // Get PubSub message from request body.
        Body.Message message = body.getMessage();
        if (message == null) {
            String msg = "Bad Request: invalid Pub/Sub message format";
            System.out.println(msg);
            throw new ResponseStatusException(BAD_REQUEST, msg);
        }
        logger.debug("Receiving message : " + body.getMessage().getMessageId());
        String input = message.getData();
        logger.debug("Data encoded : " + input);
        String decodedData = new String(Base64.getDecoder().decode(input));
        logger.debug("Display the decoded data from the message : " + decodedData);
        MemberExternalDataRequest data = null;
        try {
            data = new ObjectMapper().readValue(decodedData, MemberExternalDataRequest.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(BAD_REQUEST, "Unable to parse the data to Java object", e);
        }
        logger.debug("Display the converted data : " + data);
        memberManager.importMember(new ImportMemberCommand(
                data.getLicenceNumber(),
                data.getLastName(),
                data.getFirstName(),
                data.getEmail(),
                data.getBirthDate(),
                data.getGender().equals("M") ? "MALE" : "FEMALE",
                data.getPhone(),
                data.getMobile(),
                data.getLine1(),
                data.getLine2(),
                data.getLine3(),
                data.getZipCode(),
                data.getCity()
        ));
        logger.info("End of member " + data.getLastName() + " " + data.getFirstName() + " import process");
    }

    @GetMapping("/{memberId}")
    public MemberDTO getMember(@PathVariable String memberId) {
        try {
            Member member = memberManager.memberOfId(memberId);
            return toMemberDTO(member);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(NOT_FOUND, "No member found with id: " + memberId, e);
        }
    }

    @PostMapping("/{memberId}/changeAddress")
    public void changeMemberAddress(@PathVariable String memberId, @RequestBody ChangeAddressRequest request) {
        try {
            memberManager.changeMemberAddress(memberId, request.line1, request.line2, request.line3,
                    request.city, request.zipCode);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(NOT_FOUND, "No member found with id: " + memberId, e);
        }
    }

    @PostMapping("/{memberId}/changeContactInformation")
    public void changeMemberContactInformation(@PathVariable String memberId,
                                               @RequestBody ChangeContactInformationRequest request) {
        try {
            memberManager.changeMemberContactInformation(memberId, request.lastName, request.firstName,
                    request.email, request.gender, request.birthDate, request.phone, request.mobile);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(NOT_FOUND, "No member found with id: " + memberId, e);
        }
    }

    @GetMapping
    public MemberListDTO getMembers(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "20") Integer pageSize,
                                    @RequestParam(defaultValue = "ASC") String sortOrder,
                                    @RequestParam(defaultValue = "lastName") String sortBy) {
        Page<Member> members = memberManager.findMembers(page, pageSize, sortBy, sortOrder);
        return toMemberListDTO(members, members.total);
    }

    private MemberListDTO toMemberListDTO(Page<Member> members, long total) {
        List<MemberOverviewDTO> membersDTO = members.elements.stream()
                .map(this::toMemberOverviewDTO)
                .collect(toList());
        return new MemberListDTO(membersDTO, total);
    }

    private MemberOverviewDTO toMemberOverviewDTO(Member member) {
        return new MemberOverviewDTO(member.getId(), member.getLastName(), member.getFirstName(), member.getEmail());
    }

    private MemberDTO toMemberDTO(Member member) {
        AddressDTO addressDTO = toAddressDTO(member.getAddress());
        return new MemberDTO(member.getLastName(), member.getFirstName(), member.getEmail(), member.getGender().name(),
                member.getBirthDate(), member.getPhone(), member.getMobile(), addressDTO, member.getCreatedAt(), member.getCreator());
    }

    private AddressDTO toAddressDTO(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressDTO(address.getLine1(), address.getLine2(), address.getLine3(), address.getCity(), address.getZipCode());
    }
}
