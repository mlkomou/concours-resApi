package com.concours.komou.app.payoad;

public class NotificationPayload {
   public String titre;
   public String description;
   public String type;
   public Long concoursId;
   public Long postulantId;
   public Long postulantResultatId;
   public Long postulationDocId;
   public Long postulationId;

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getConcoursId() {
        return concoursId;
    }

    public void setConcoursId(Long concoursId) {
        this.concoursId = concoursId;
    }

    public Long getPostulantId() {
        return postulantId;
    }

    public void setPostulantId(Long postulantId) {
        this.postulantId = postulantId;
    }

    public Long getPostulantResultatId() {
        return postulantResultatId;
    }

    public void setPostulantResultatId(Long postulantResultatId) {
        this.postulantResultatId = postulantResultatId;
    }

    public Long getPostulationDocId() {
        return postulationDocId;
    }

    public void setPostulationDocId(Long postulationDocId) {
        this.postulationDocId = postulationDocId;
    }

    public Long getPostulationId() {
        return postulationId;
    }

    public void setPostulationId(Long postulationId) {
        this.postulationId = postulationId;
    }
}
