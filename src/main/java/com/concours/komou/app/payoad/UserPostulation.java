package com.concours.komou.app.payoad;

import com.concours.komou.app.entity.Postulation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UserPostulation {
   public Long postulantId;
   public Long concoursId;
   public String moyen;
   public String identifiant;

    public Long getPostulantId() {
        return postulantId;
    }

    public void setPostulantId(Long postulantId) {
        this.postulantId = postulantId;
    }

    public Long getConcoursId() {
        return concoursId;
    }

    public void setConcoursId(Long concoursId) {
        this.concoursId = concoursId;
    }

    public String getMoyen() {
        return moyen;
    }

    public void setMoyen(String moyen) {
        this.moyen = moyen;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }
}
