package com.cloud.cmr.exposition.member;

import java.util.List;

public class MemberListDTO {
    public final List<MemberDTO> members;

    public MemberListDTO(List<MemberDTO> members) {
        this.members = members;
    }
}
