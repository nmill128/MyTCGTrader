/*
 * Created by Nicholas Miller on 2016.04.12  * 
 * Copyright © 2016 Nicholas Miller. All rights reserved. * 
 */
package com.mycompany.entitypackage;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author nmiller
 */
@Entity
@Table(name = "Cards")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cards.findAll", query = "SELECT c FROM Cards c"),
    @NamedQuery(name = "Cards.findById", query = "SELECT c FROM Cards c WHERE c.id = :id"),
    @NamedQuery(name = "Cards.findByCardName", query = "SELECT c FROM Cards c WHERE c.cardName = :cardName"),
    @NamedQuery(name = "Cards.findByCardCondition", query = "SELECT c FROM Cards c WHERE c.cardCondition = :cardCondition"),
    @NamedQuery(name = "Cards.findByEdition", query = "SELECT c FROM Cards c WHERE c.edition = :edition"),
    @NamedQuery(name = "Cards.findByCardValue", query = "SELECT c FROM Cards c WHERE c.cardValue = :cardValue"),
    @NamedQuery(name = "Cards.findByNotes", query = "SELECT c FROM Cards c WHERE c.notes = :notes"),
    @NamedQuery(name = "Cards.findByBinder", query = "SELECT c FROM Cards c WHERE c.binder = :binder"),
    @NamedQuery(name = "Cards.findByDateAdded", query = "SELECT c FROM Cards c WHERE c.dateAdded = :dateAdded")})
public class Cards implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "cardName")
    private String cardName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "cardCondition")
    private String cardCondition;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "edition")
    private String edition;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cardValue")
    private int cardValue;
    @Size(max = 255)
    @Column(name = "notes")
    private String notes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "binder")
    private int binder;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "date_added")
    private String dateAdded;
    @OneToMany(mappedBy = "cardId")
    private Collection<CardPhotos> cardPhotosCollection;

    public Cards() {
    }

    public Cards(Integer id) {
        this.id = id;
    }

    public Cards(Integer id, String cardName, String cardCondition, String edition, int cardValue, int binder, String dateAdded) {
        this.id = id;
        this.cardName = cardName;
        this.cardCondition = cardCondition;
        this.edition = edition;
        this.cardValue = cardValue;
        this.binder = binder;
        this.dateAdded = dateAdded;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardCondition() {
        return cardCondition;
    }

    public void setCardCondition(String cardCondition) {
        this.cardCondition = cardCondition;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getBinder() {
        return binder;
    }

    public void setBinder(int binder) {
        this.binder = binder;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    @XmlTransient
    public Collection<CardPhotos> getCardPhotosCollection() {
        return cardPhotosCollection;
    }

    public void setCardPhotosCollection(Collection<CardPhotos> cardPhotosCollection) {
        this.cardPhotosCollection = cardPhotosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cards)) {
            return false;
        }
        Cards other = (Cards) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entitypackage.Cards[ id=" + id + " ]";
    }
    
}
