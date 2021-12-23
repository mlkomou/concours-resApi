package com.concours.komou.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "postulant_resultat")
@Access(AccessType.FIELD)
public class PostulantResultat extends Generality {
    @ManyToOne()
    @JoinColumn(name = "resultat")
    private Resultat resultat;

    @ManyToOne
    @JoinColumn(name = "postulant")
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
