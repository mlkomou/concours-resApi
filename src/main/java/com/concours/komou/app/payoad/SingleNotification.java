package com.concours.komou.app.payoad;

import java.util.ArrayList;
import java.util.List;

public class SingleNotification {
    private String app_id;
    private List<String> include_player_ids = new ArrayList<>();
    private PushDetail headings;
    private PushDetail contents;
    private PushDataDetail data;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }


    public List<String> getInclude_player_ids() {
        return include_player_ids;
    }

    public void setInclude_player_ids(List<String> include_player_ids) {
        this.include_player_ids = include_player_ids;
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
