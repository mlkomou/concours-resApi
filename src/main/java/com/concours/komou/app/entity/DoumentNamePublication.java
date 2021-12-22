package com.concours.komou.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Table(name = "document_name")
@Access(AccessType.FIELD)
public class DoumentNamePublication extends Generality {
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "participation")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnoreProperties(value = {"concours"}, allowSetters = true)
    private Concours concours;

    public Concours getConcours() {
        return concours;
    }

    public void setConcours(Concours concours) {
        this.concours = concours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
