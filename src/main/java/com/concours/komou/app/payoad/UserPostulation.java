package com.concours.komou.app.payoad;

import com.concours.komou.app.entity.Postulation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UserPostulation {
   public Long postulantId;
   public Postulation postulation;
   List<MultipartFile> docs;

    public Long getPostulantId() {
        return postulantId;
    }

    public void setPostulantId(Long postulantId) {
        this.postulantId = postulantId;
    }

    public Postulation getPostulation() {
        return postulation;
    }

    public void setPostulation(Postulation postulation) {
        this.postulation = postulation;
    }

    public List<MultipartFile> getDocs() {
        return docs;
    }

    public void setDocs(List<MultipartFile> docs) {
        this.docs = docs;
    }
}
