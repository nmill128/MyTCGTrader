/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.entitypackage;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Erik
 * 
 * Cards generated entity class
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
    @NamedQuery(name = "Cards.findCardsByUserId", query = "SELECT c FROM Cards c WHERE c.userId.id = :userId"),
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
    private float cardValue;
    @Size(max = 255)
    @Column(name = "notes")
    private String notes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "date_added")
    private String dateAdded;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users userId;

    /**
     * Constructor
     */
    public Cards() {
    }

    /**
     * Creates with id
     * @param id
     */
    public Cards(Integer id) {
        this.id = id;
    }

    /**
     * Creates with params
     * @param id
     * @param cardName
     * @param cardCondition
     * @param edition
     * @param cardValue
     * @param dateAdded
     */
    public Cards(Integer id, String cardName, String cardCondition, String edition, int cardValue, String dateAdded) {
        this.id = id;
        this.cardName = cardName;
        this.cardCondition = cardCondition;
        this.edition = edition;
        this.cardValue = cardValue;
        this.dateAdded = dateAdded;
    }

    /**
     * gets id
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     * sets id
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * gets card name
     * @return
     */
    public String getCardName() {
        return cardName;
    }

    /**
     *sets card name
     * @param cardName
     */
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    /**
     * gets card condition
     * @return
     */
    public String getCardCondition() {
        return cardCondition;
    }

    /**
     * sets card condition
     * @param cardCondition
     */
    public void setCardCondition(String cardCondition) {
        this.cardCondition = cardCondition;
    }

    /**
     *gets card edition
     * @return
     */
    public String getEdition() {
        return edition;
    }

    /**
     * sets card edition
     * @param edition
     */
    public void setEdition(String edition) {
        this.edition = edition;
    }

    /**
     * gets card value
     * @return
     */
    public float getCardValue() {
        return cardValue;
    }

    /**
     * sets card value
     * @param cardValue
     */
    public void setCardValue(float cardValue) {
        this.cardValue = cardValue;
    }

    /**
     * get notes
     * @return
     */
    public String getNotes() {
        return notes;
    }

    /**
     * set notes
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * gets date added
     * @return
     */
    public String getDateAdded() {
        return dateAdded;
    }

    /**
     * sets date added
     * @param dateAdded
     */
    public void setDateAdded(String dateAdded) {
        this.dateAdded = (dateAdded);
    }

    /**
     * gets user id
     * @return
     */
    public Users getUserId() {
        return userId;
    }

    /**
     * sets user id
     * @param userId
     */
    public void setUserId(Users userId) {
        this.userId = userId;
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
