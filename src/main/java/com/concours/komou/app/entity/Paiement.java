package com.concours.komou.app.entity;

import javax.persistence.*;

@Entity
@Table(name = "paiement")
@Access(AccessType.FIELD)
public class Paiement extends Generality {
    @OneToOne(mappedBy = "paiement")
    private Postulation postulation;

    @Column(name = "moyen")
    private String moyen;

    //supllemnt gagné par la plateforme
    @Column(name = "pourcentage")
    private String pourcentage;

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

    public String getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(String pourcentage) {
        this.pourcentage = pourcentage;
    }


}
