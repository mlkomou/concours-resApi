package com.concours.komou.app.entity;

import java.util.HashMap;
import java.util.Map;

public class Response {
    public static Map<String, Object> success(Object object, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 100);
        response.put("message", message);
        response.put("response", object);
        return response;
    }

    public static Map<String, Object> error(Object object, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", message);
        response.put("response", object);
        return response;
    }
}
