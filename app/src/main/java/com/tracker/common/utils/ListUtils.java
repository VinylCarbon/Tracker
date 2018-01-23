package com.tracker.common.utils;

import java.util.List;

public class ListUtils {
    public static <Value> boolean isNotEmpty(List<Value> values) {
        return !values.isEmpty();
    }
}
