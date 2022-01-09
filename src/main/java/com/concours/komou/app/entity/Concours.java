package com.concours.komou.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
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
    @Column(name = "dateLimite")
    private String dateLimite;

    @OneToMany(mappedBy = "concours")
    @JsonIgnoreProperties(value = {"concours"}, allowSetters = true)
    private List<DoumentNamePublication> docs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(String dateLimite) {
        this.dateLimite = dateLimite;
    }
}
