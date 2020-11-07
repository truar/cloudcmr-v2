package com.cloud.cmr.exposition.member;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MemberResourcesAdvice {

    @ExceptionHandler(ResponseStatusException.class)
    public Map<String, String> responseStatusException(HttpServletResponse response,
                                                       ResponseStatusException exception) {
        response.setStatus(exception.getStatus().value());
        HashMap<String, String> responseBody = new HashMap<>();
        responseBody.put("status", String.valueOf(response.getStatus()));
        responseBody.put("message", exception.getReason());
        return responseBody;
    }

}
