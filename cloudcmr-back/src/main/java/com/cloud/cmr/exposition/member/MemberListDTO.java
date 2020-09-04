package com.cloud.cmr.exposition.member;

import java.util.List;

public class MemberListDTO {
    public long total;
    public List<MemberOverviewDTO> members;

    public MemberListDTO() {
    }

    public MemberListDTO(List<MemberOverviewDTO> membersDTO, long total) {
        this.members = membersDTO;
        this.total = total;
    }
}
