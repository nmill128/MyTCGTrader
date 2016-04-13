/*
 * Created by Nicholas Miller on 2016.04.12  * 
 * Copyright © 2016 Nicholas Miller. All rights reserved. * 
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
 * @author nmiller
 */
@Entity
@Table(name = "CardPhotos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CardPhotos.findAll", query = "SELECT c FROM CardPhotos c"),
    @NamedQuery(name = "CardPhotos.findById", query = "SELECT c FROM CardPhotos c WHERE c.id = :id"),
    @NamedQuery(name = "CardPhotos.findByExtension", query = "SELECT c FROM CardPhotos c WHERE c.extension = :extension")})
public class CardPhotos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "extension")
    private String extension;
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    @ManyToOne
    private Cards cardId;

    public CardPhotos() {
    }

    public CardPhotos(Integer id) {
        this.id = id;
    }

    public CardPhotos(Integer id, String extension) {
        this.id = id;
        this.extension = extension;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Cards getCardId() {
        return cardId;
    }

    public void setCardId(Cards cardId) {
        this.cardId = cardId;
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
        if (!(object instanceof CardPhotos)) {
            return false;
        }
        CardPhotos other = (CardPhotos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entitypackage.CardPhotos[ id=" + id + " ]";
    }
    
}
