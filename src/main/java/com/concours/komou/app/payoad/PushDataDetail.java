package com.concours.komou.app.payoad;

import java.util.Map;

public class PushDataDetail {
    private Map<String, Object> tache;

    public PushDataDetail(Map<String, Object>  tache) {
        this.tache = tache;
    }

    public Map<String, Object>  getTache() {
        return tache;
    }

    public void setTache(Map<String, Object>  tache) {
        this.tache = tache;
    }
}
