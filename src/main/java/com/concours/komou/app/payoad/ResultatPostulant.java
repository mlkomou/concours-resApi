package com.concours.komou.app.payoad;

import java.util.List;

public class ResultatPostulant {
    public String resultatName;
    public Long concoursId;
    public List<ResultatSingle> resultatSingles;

    public String getResultatName() {
        return resultatName;
    }

    public void setResultatName(String resultatName) {
        this.resultatName = resultatName;
    }

    public List<ResultatSingle> getResultatSingles() {
        return resultatSingles;
    }

    public void setResultatSingles(List<ResultatSingle> resultatSingles) {
        this.resultatSingles = resultatSingles;
    }

    public Long getConcoursId() {
        return concoursId;
    }

    public void setConcoursId(Long concoursId) {
        this.concoursId = concoursId;
    }
}
