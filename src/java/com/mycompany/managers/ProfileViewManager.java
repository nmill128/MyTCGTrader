/*
 * Created by Osman Balci on 2016.02.14  * 
 * Copyright © 2016 Osman Balci. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entitypackage.Users;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "profileViewManager")
@SessionScoped
/**
 *
 * @author Balci
 */
public class ProfileViewManager implements Serializable {

    // Instance Variable (Property)
    private Users user;
    
  /**
   * The instance variable 'userFacade' is annotated with the @EJB annotation.
   * This means that the GlassFish application server, at runtime, will inject in
   * this instance variable a reference to the @Stateless session bean UserFacade.
   */  
  @EJB
  private com.mycompany.sessionBeanPackage.UsersFacade userFacade;

  public ProfileViewManager() {

  }

  public String viewProfile() {
    return "Profile";
  }

  /**
   * @return the user
   */
  public Users getUser() {
    return user;
  }

  public Users getLoggedInUser() {
    return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
  }

  /**
   * @param user the user to set
   */
  public void setUser(Users user) {
    this.user = user;
  }

}