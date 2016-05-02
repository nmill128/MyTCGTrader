/*
 * Created by Nicholas Miller on 2016.04.12  * 
 * Copyright © 2016 Nicholas Miller. All rights reserved. * 
 */
package com.mycompany.entitypackage;

import com.mycompany.managers.Constants;
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
 * Generated entity class for CardPhotos The general CardPhoto object code
 *
 * @author nmiller
 */
@Entity
@Table(name = "CardPhotos")
@XmlRootElement
@NamedQueries({ //queries 

    @NamedQuery(name = "CardPhotos.findAll", query = "SELECT c FROM CardPhotos c"),
    @NamedQuery(name = "CardPhotos.findById", query = "SELECT c FROM CardPhotos c WHERE c.id = :id"),
    @NamedQuery(name = "CardPhotos.findPhotosByCardId", query = "SELECT c FROM CardPhotos c WHERE c.cardId.id = :cardId"),
    @NamedQuery(name = "CardPhotos.findByExtension", query = "SELECT c FROM CardPhotos c WHERE c.extension = :extension")})
public class CardPhotos implements Serializable {

    //private fields and their database connections
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

    /**
     * Constructor
     */
    public CardPhotos() {
    }

    /**
     * Constructor
     *
     * @param id to be created with
     */
    public CardPhotos(Integer id) {
        this.id = id;
    }

    /**
     * Constructor
     *
     * @param id to be created with
     * @param extension to be created with
     */
    public CardPhotos(Integer id, String extension) {
        this.id = id;
        this.extension = extension;
    }

    /**
     * Constructor
     *
     * @param id to be created with
     * @param extension to be created with
     */
    public CardPhotos(String extension, Cards id) {
        this.extension = extension;
        cardId = id;
    }

    /**
     * returns the id 
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * set id
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * gets the extension
     * @return extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * sets the extension
     * @param extension 
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * gets the card id
     * @return id
     */
    public Cards getCardId() {
        return cardId;
    }

    /**
     * sets the card id
     * @param cardId 
     */
    public void setCardId(Cards cardId) {
        this.cardId = cardId;
    }

    /**
     * hashs object
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * equals between objects
     * @param object
     * @return 
     */
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

    /**
     * to string
     * @return string
     */
    @Override
    public String toString() {
        return "com.mycompany.entitypackage.CardPhotos[ id=" + id + " ]";
    }

    /**
     * gives the file path of the file
     * @return string
     */
    public String getFilePath() {
        return Constants.ROOT_DIRECTORY + getFilename();
    }

    /**
     * get file name
     * @return string
     */
    public String getFilename() {
        return getId() + "." + getExtension();
    }

    /**
     * gets the filename of the thumbnail
     * @return string
     */
    public String getThumbnailName() {
        return getId() + "_thumbnail." + getExtension();
    }

    /**
     * returns the file path of the thumbnail
     * @return string
     */
    public String getThumbnailFilePath() {
        return Constants.ROOT_DIRECTORY + getThumbnailName();
    }

}
