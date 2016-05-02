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
 * generated entity class for user photos
 * @author nmiller
 */
@Entity
@Table(name = "UserPhotos")
@XmlRootElement
@NamedQueries({ //queries
    @NamedQuery(name = "UserPhotos.findAll", query = "SELECT u FROM UserPhotos u"),
    @NamedQuery(name = "UserPhotos.findById", query = "SELECT u FROM UserPhotos u WHERE u.id = :id"),
    @NamedQuery(name = "UserPhotos.findPhotosByUserId", query = "SELECT u FROM UserPhotos u WHERE u.userId.id = :userId"),
    @NamedQuery(name = "UserPhotos.findByExtension", query = "SELECT u FROM UserPhotos u WHERE u.extension = :extension")})
public class UserPhotos implements Serializable {
    //private variables and the db counter parts
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
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private Users userId;

    /**
     * Constructor
     */
    public UserPhotos() {
    }

    /**
     * constructs with id
     * @param id
     */
    public UserPhotos(Integer id) {
        this.id = id;
    }

    /**
     * constructs with params
     * @param id
     * @param extension
     */
    public UserPhotos(Integer id, String extension) {
        this.id = id;
        this.extension = extension;
    }
    
    /**
     * constructs with params
     * @param extension
     * @param id
     */
    public UserPhotos(String extension, Users id) {
        this.extension = extension;
        userId = id;
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
     * gets extension
     * @return
     */
    public String getExtension() {
        return extension;
    }

    /**
     * sets extension
     * @param extension
     */
    public void setExtension(String extension) {
        this.extension = extension;
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
        if (!(object instanceof UserPhotos)) {
            return false;
        }
        UserPhotos other = (UserPhotos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entitypackage.UserPhotos[ id=" + id + " ]";
    }
    
    /**
     * gets filepath of photo
     * @return
     */
    public String getFilePath() {
        return Constants.ROOT_DIRECTORY + getFilename();
    }

    /**
     *gets file name of photo
     * @return
     */
    public String getFilename() {
        return "userPhotos/" + getId() + "." + getExtension();
    }
    
    /**
     * gets thumbnail name of photo
     * @return
     */
    public String getThumbnailName() {
        return "userPhotos/" + getId() + "_thumbnail." + getExtension();
    }
    
    /**
     * gets thumbnail file path
     * @return
     */
    public String getThumbnailFilePath() {
        return Constants.ROOT_DIRECTORY + getThumbnailName();
    }
    
}
