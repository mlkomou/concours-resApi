package com.concours.komou.app.payoad;

import java.util.ArrayList;
import java.util.List;

public class PushNotification {
    private String app_id;
    private List<String> included_segments = new ArrayList<>();
    private PushDetail headings;
    private PushDetail contents;
    private PushDataDetail data;

//    public PushNotification(String app_id, List<String> included_segments, PushDetail headings, PushDetail contents) {
//        this.app_id = app_id;
//        this.included_segments = included_segments;
//        this.headings = headings;
//        this.contents = contents;
//    }
//
//    public PushNotification(String app_id, List<String> included_segments, PushDetail headings, PushDetail contents, PushDataDetail data) {
//        this.app_id = app_id;
//        this.included_segments = included_segments;
//        this.headings = headings;
//        this.contents = contents;
//        this.data = data;
//    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public List<String> getIncluded_segments() {
        return included_segments;
    }

    public void setIncluded_segments(List<String> included_segments) {
        this.included_segments = included_segments;
    }

    public PushDetail getHeadings() {
        return headings;
    }

    public void setHeadings(PushDetail headings) {
        this.headings = headings;
    }

    public PushDetail getContents() {
        return contents;
    }

    public void setContents(PushDetail contents) {
        this.contents = contents;
    }

    public PushDataDetail getData() {
        return data;
    }

    public void setData(PushDataDetail data) {
        this.data = data;
    }

}
