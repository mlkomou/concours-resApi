package com.concours.komou.app.entity;

import javax.persistence.*;

@Entity
@Table(name = "postulion")
@Access(AccessType.FIELD)
public class Postulation extends Generality {
 @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="postulant", referencedColumnName = "id")
    private Postulant postulant;

 @OneToOne(cascade = CascadeType.ALL)
 @JoinColumn(name="concours", referencedColumnName = "id")
 private Concours concours;

 @OneToOne(cascade = CascadeType.ALL)
 @JoinColumn(name = "paiement", referencedColumnName = "id")
 private Paiement paiement;

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
}
