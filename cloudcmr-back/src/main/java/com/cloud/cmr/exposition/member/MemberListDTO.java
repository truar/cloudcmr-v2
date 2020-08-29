package com.cloud.cmr.exposition.member;

import java.util.List;

public class MemberListDTO {
    public List<MemberDTO> members;

    public MemberListDTO() {
    }

    public MemberListDTO(List<MemberDTO> members) {
        this.members = members;
    }
}
