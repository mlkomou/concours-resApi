package com.concours.komou.app.payoad;

public class ResponsePush {
    private String id;
    private Long recipients;
    private String external_id;

    public ResponsePush() {
    }

    public ResponsePush(String id, Long recipients, String external_id) {
        this.id = id;
        this.recipients = recipients;
        this.external_id = external_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getRecipients() {
        return recipients;
    }

    public void setRecipients(Long recipients) {
        this.recipients = recipients;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }
}
