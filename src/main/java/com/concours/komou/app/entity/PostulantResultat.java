package com.concours.komou.app.entity;

import javax.persistence.*;

@Entity
@Table(name = "postulant_resultat")
@Access(AccessType.FIELD)
public class PostulantResultat extends Generality {
    @ManyToOne()
    @JoinColumn(name = "resultat")
    private Resultat resultat;

    @OneToOne(mappedBy = "postulantResultat")
    private Postulant postulant;

    @Column(name = "statut")
    private String statut;

    public Resultat getResultat() {
        return resultat;
    }

    public void setResultat(Resultat resultat) {
        this.resultat = resultat;
    }

    public Postulant getPostulant() {
        return postulant;
    }

    public void setPostulant(Postulant postulant) {
        this.postulant = postulant;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

}
