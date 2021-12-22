package com.concours.komou.app.entity;

import javax.persistence.*;

@Entity
@Table(name = "postulation_doc")
@Access(AccessType.FIELD)
public class PostulationDoc extends Generality {

    @Column(name = "path")
    private String path;

    @Column(name = "type")
    private String type;

    @Column(name = "accepted")
    private boolean accepted;

    @ManyToOne
    @JoinColumn(name="postulation")
    private Postulation postulation;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Postulation getPostulation() {
        return postulation;
    }

    public void setPostulation(Postulation postulation) {
        this.postulation = postulation;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
