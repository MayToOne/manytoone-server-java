package com.manytooneserverjava.excpetion.object;

import lombok.Getter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
public class ExceptionResponses {
    private final Integer status;
    private final List<String> messages;

    public ExceptionResponses(Integer status, List<String> messages) {
        this.status = status;
        messages.sort(Comparator.naturalOrder());
        this.messages = messages;
    }
}
