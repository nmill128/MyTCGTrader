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
 * Generated entity class for Want
 * @author Erik
 */
@Entity
@Table(name = "Wants")
@XmlRootElement
@NamedQueries({ //query
    @NamedQuery(name = "Wants.findAll", query = "SELECT w FROM Wants w"),
    @NamedQuery(name = "Wants.findById", query = "SELECT w FROM Wants w WHERE w.id = :id"),
    @NamedQuery(name = "Wants.findByCardName", query = "SELECT w FROM Wants w WHERE w.cardName = :cardName"),
    @NamedQuery(name = "Wants.findByCardCondition", query = "SELECT w FROM Wants w WHERE w.cardCondition = :cardCondition"),
    @NamedQuery(name = "Wants.findWantsByUserId", query = "SELECT w FROM Wants w WHERE w.userId.id = :userId"),
    @NamedQuery(name = "Wants.findByCardValue", query = "SELECT w FROM Wants w WHERE w.cardValue = :cardValue")})
public class Wants implements Serializable {
    //private variables and the db counter parts
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
    @Column(name = "cardValue")
    private float cardValue;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users userId;

    /**
     * Constructor
     */
    public Wants() {
    }

    /**
     * Constructs with id
     * @param id
     */
    public Wants(Integer id) {
        this.id = id;
    }

    /**
     * constructs with params
     * @param id
     * @param cardName
     * @param cardCondition
     * @param cardValue
     */
    public Wants(Integer id, String cardName, String cardCondition, int cardValue) {
        this.id = id;
        this.cardName = cardName;
        this.cardCondition = cardCondition;
        this.cardValue = cardValue;
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
     *gets card name
     * @return
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * sets card name
     * @param cardName
     */
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    /**
     * gets cards condition
     * @return
     */
    public String getCardCondition() {
        return cardCondition;
    }

    /**
     * sets cards condition
     * @param cardCondition
     */
    public void setCardCondition(String cardCondition) {
        this.cardCondition = cardCondition;
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
        if (!(object instanceof Wants)) {
            return false;
        }
        Wants other = (Wants) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entitypackage.Wants[ id=" + id + " ]";
    }
    
}
