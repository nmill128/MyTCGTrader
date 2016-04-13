/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.entitypackage;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 */
@Entity
@Table(name = "wants")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Wants.findAll", query = "SELECT w FROM Wants w"),
    @NamedQuery(name = "Wants.findById", query = "SELECT w FROM Wants w WHERE w.id = :id"),
    @NamedQuery(name = "Wants.findByCardName", query = "SELECT w FROM Wants w WHERE w.cardName = :cardName"),
    @NamedQuery(name = "Wants.findByCardCondition", query = "SELECT w FROM Wants w WHERE w.cardCondition = :cardCondition"),
    @NamedQuery(name = "Wants.findWantsByUserId", query = "SELECT w FROM Wants w WHERE w.userId.id = :userId"),
    @NamedQuery(name = "Wants.findByCardValue", query = "SELECT w FROM Wants w WHERE w.cardValue = :cardValue")})
public class Wants implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
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
    private int cardValue;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users userId;

    public Wants() {
    }

    public Wants(Integer id) {
        this.id = id;
    }

    public Wants(Integer id, String cardName, String cardCondition, int cardValue) {
        this.id = id;
        this.cardName = cardName;
        this.cardCondition = cardCondition;
        this.cardValue = cardValue;
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

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }

    public Users getUserId() {
        return userId;
    }

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
