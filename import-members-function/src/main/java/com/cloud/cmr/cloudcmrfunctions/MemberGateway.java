package com.cloud.cmr.cloudcmrfunctions;

import org.springframework.util.concurrent.ListenableFuture;

public interface MemberGateway {

    ListenableFuture<String> send(MemberDTO member);

}
