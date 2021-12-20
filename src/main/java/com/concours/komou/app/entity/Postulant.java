package com.concours.komou.app.entity;

import com.concours.komou.entity.ApplicationUser;

import javax.persistence.*;

@Entity
@Table(name = "postulant")
@Access(AccessType.FIELD)
public class Postulant extends Generality {
    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "telephone")
    private String telephone;

    @OneToOne(mappedBy = "postulant")
    private ApplicationUser user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "postulantResultat", referencedColumnName = "id")
    private PostulantResultat postulantResultat;

    @OneToOne(mappedBy = "postulant")
    private Postulation postulation;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public Postulation getPostulation() {
        return postulation;
    }

    public void setPostulation(Postulation postulation) {
        this.postulation = postulation;
    }

    public PostulantResultat getPostulantResultat() {
        return postulantResultat;
    }

    public void setPostulantResultat(PostulantResultat postulantResultat) {
        this.postulantResultat = postulantResultat;
    }
}
