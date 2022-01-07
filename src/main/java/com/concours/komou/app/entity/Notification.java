package com.concours.komou.app.entity;

import javax.persistence.*;

@Entity
@Table(name = "notification")
@Access(AccessType.FIELD)
public class Notification extends Generality {
    @Column(name = "titre")
    private String titre;
    @Column(name = "description")
    private String description;
    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name="participation")
    private Concours concours;

    @ManyToOne
    @JoinColumn(name="postulant")
    private Postulant postulant;

    @ManyToOne
    @JoinColumn(name = "postulant_resultat")
    private PostulantResultat postulantResultat;

    @ManyToOne
    @JoinColumn(name = "postulation_doc")
    private PostulationDoc postulationDoc;

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Concours getConcours() {
        return concours;
    }

    public void setConcours(Concours concours) {
        this.concours = concours;
    }

    public Postulant getPostulant() {
        return postulant;
    }

    public void setPostulant(Postulant postulant) {
        this.postulant = postulant;
    }

    public PostulantResultat getPostulantResultat() {
        return postulantResultat;
    }

    public void setPostulantResultat(PostulantResultat postulantResultat) {
        this.postulantResultat = postulantResultat;
    }

    public PostulationDoc getPostulationDoc() {
        return postulationDoc;
    }

    public void setPostulationDoc(PostulationDoc postulationDoc) {
        this.postulationDoc = postulationDoc;
    }


}
