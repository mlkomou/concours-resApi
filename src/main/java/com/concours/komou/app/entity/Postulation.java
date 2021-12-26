package com.concours.komou.app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "postulion")
@Access(AccessType.FIELD)
public class Postulation extends Generality {

 @ManyToOne
    @JoinColumn(name="postulant")
    private Postulant postulant;

 @ManyToOne
 @JoinColumn(name="concours")
 private Concours concours;

 @JsonManagedReference(value = "postulation-paiement")
 @OneToOne(mappedBy = "postulation")
 private Paiement paiement;

 @Column(name = "validation")
 private String validation = "ATTENTE"; // ATTENTE, VALIDÉ, REJETTÉ

 public Postulant getPostulant() {
  return postulant;
 }

 public void setPostulant(Postulant postulant) {
  this.postulant = postulant;
 }

 public Concours getConcours() {
  return concours;
 }

 public void setConcours(Concours concours) {
  this.concours = concours;
 }

 public Paiement getPaiement() {
  return paiement;
 }

 public void setPaiement(Paiement paiement) {
  this.paiement = paiement;
 }

 public String getValidation() {
  return validation;
 }

 public void setValidation(String validation) {
  this.validation = validation;
 }
}
