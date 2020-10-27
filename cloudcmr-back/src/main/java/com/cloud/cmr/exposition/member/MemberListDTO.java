package com.cloud.cmr.exposition.member;

import java.util.List;

public class MemberListDTO {
    public final long total;
    public final List<MemberOverviewDTO> members;

    public MemberListDTO(List<MemberOverviewDTO> membersDTO, long total) {
        this.members = membersDTO;
        this.total = total;
    }
}
