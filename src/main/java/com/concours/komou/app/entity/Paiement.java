package com.concours.komou.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "paiement")
@Access(AccessType.FIELD)
public class Paiement extends Generality {

    @JsonBackReference(value = "postulation-paiement")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "postulation", referencedColumnName = "id")
    private Postulation postulation;

    @Column(name = "moyen")
    private String moyen;
    @Column(name = "indentifiant") //telephone effectuant le paiement
    private String identifiant;

    //supllemnt gagné par la plateforme
    @Column(name = "pourcentage")
    private Long pourcentage;

    public Postulation getPostulation() {
        return postulation;
    }

    public void setPostulation(Postulation postulation) {
        this.postulation = postulation;
    }

    public String getMoyen() {
        return moyen;
    }

    public void setMoyen(String moyen) {
        this.moyen = moyen;
    }

    public Long getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(Long pourcentage) {
        this.pourcentage = pourcentage;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }
}
