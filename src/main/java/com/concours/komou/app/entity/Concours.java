package com.concours.komou.app.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "participation")
@Access(AccessType.FIELD)
public class Concours extends Generality {
    @Column(name = "name")
    private String name;
    private String path;

    @OneToOne(mappedBy = "concours")
    private Postulation postulation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resultat", referencedColumnName = "id")
    private Resultat resultat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Resultat getResultat() {
        return resultat;
    }

    public void setResultat(Resultat resultat) {
        this.resultat = resultat;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Postulation getPostulation() {
        return postulation;
    }

    public void setPostulation(Postulation postulation) {
        this.postulation = postulation;
    }
}
