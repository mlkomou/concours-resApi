package com.concours.komou.app.payoad;

import java.util.List;

public class ResultatPostulant {
    public String resultatName;
    public Long concoursId;
    public List<Long> postulantIds;

    public String getResultatName() {
        return resultatName;
    }

    public void setResultatName(String resultatName) {
        this.resultatName = resultatName;
    }

    public List<Long> getPostulantIds() {
        return postulantIds;
    }

    public void setPostulantIds(List<Long> postulantIds) {
        this.postulantIds = postulantIds;
    }

    public Long getConcoursId() {
        return concoursId;
    }

    public void setConcoursId(Long concoursId) {
        this.concoursId = concoursId;
    }
}
