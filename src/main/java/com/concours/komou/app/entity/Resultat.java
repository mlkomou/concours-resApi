package com.concours.komou.app.entity;

import javax.persistence.*;

@Entity
@Table(name = "resultat")
@Access(AccessType.FIELD)
public class Resultat extends Generality {

    @Column(name = "name")
    private String name;

    @Column(name = "visibily")
    private Boolean visibily;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "postulantResultat", referencedColumnName = "id")
//    private PostulantResultat postulantResultat;

    @OneToOne(mappedBy = "resultat")
    private Concours concours;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVisibily() {
        return visibily;
    }

    public void setVisibily(Boolean visibily) {
        this.visibily = visibily;
    }

    //    public PostulantResultat getPostulantResultat() {
//        return postulantResultat;
//    }
//
//    public void setPostulantResultat(PostulantResultat postulantResultat) {
//        this.postulantResultat = postulantResultat;
//    }


    public Concours getConcours() {
        return concours;
    }

    public void setConcours(Concours concours) {
        this.concours = concours;
    }
}
