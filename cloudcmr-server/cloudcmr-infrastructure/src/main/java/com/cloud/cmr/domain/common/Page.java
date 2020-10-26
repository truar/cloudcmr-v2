package com.cloud.cmr.domain.common;

import java.util.List;

public class Page<T> {
    public final long total;
    public final List<T> elements;

    public Page(long total, List<T> elements) {
        this.total = total;
        this.elements = elements;
    }
}
