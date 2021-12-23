package com.concours.komou.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.print.attribute.standard.DocumentName;
import java.util.List;

@Entity
@Table(name = "participation")
@Access(AccessType.FIELD)
public class Concours extends Generality {
    @Column(name = "name")
    private String name;
    @Column(name = "frais")
    private Long frais;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "path")
    private String path;

//    @JsonManagedReference(value = "resultat-concours")
//    @OneToOne(mappedBy = "concours")
//    private Resultat resultat;

    @OneToMany(mappedBy = "concours")
    @JsonIgnoreProperties(value = {"concours"}, allowSetters = true)
    private List<DoumentNamePublication> docs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Resultat getResultat() {
//        return resultat;
//    }
//
//    public void setResultat(Resultat resultat) {
//        this.resultat = resultat;
//    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getFrais() {
        return frais;
    }

    public void setFrais(Long frais) {
        this.frais = frais;
    }

    public List<DoumentNamePublication> getDocs() {
        return docs;
    }

    public void setDocs(List<DoumentNamePublication> docs) {
        this.docs = docs;
    }
}
