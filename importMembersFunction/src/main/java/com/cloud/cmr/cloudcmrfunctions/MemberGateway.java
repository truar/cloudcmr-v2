package com.cloud.cmr.cloudcmrfunctions;

import java.util.List;

public interface MemberGateway {

    void send(List<MemberRest> members);

}
