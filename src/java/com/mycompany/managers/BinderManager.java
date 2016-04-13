/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.managers;

import com.mycompany.entitypackage.Users;
import com.mycompany.entitypackage.Wants;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.List;

@Named(value = "binderManager")
@SessionScoped
/**
 *
 * @author Erik
 */
public class BinderManager implements Serializable{
    
    private Users user;
    List<Wants> wants;
    
    
      /**
    * The instance variable 'userFacade' is annotated with the @EJB annotation.
    * This means that the GlassFish application server, at runtime, will inject in
    * this instance variable a reference to the @Stateless session bean UserFacade.
    */  
    @EJB
    private com.mycompany.sessionBeanPackage.UsersFacade userFacade;
    
          /**
    * The instance variable 'userFacade' is annotated with the @EJB annotation.
    * This means that the GlassFish application server, at runtime, will inject in
    * this instance variable a reference to the @Stateless session bean UserFacade.
    */  
    @EJB
    private com.mycompany.sessionBeanPackage.WantsFacade wantsFacade;
    
    public BinderManager(){
        
    }
    
      /**
    * @return the user
    */
    public Users getUser() {
      return user;
    }

    public Users getLoggedInUser() {
      return user = userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
    }

    /**
     * @param user the user to set
     */
    public void setUser(Users user) {
      this.user = user;
    }
    
    public List<Wants> getWants(){
        return wantsFacade.findPhotosByUserID(user.getId());
    }
    
    public void setWants(){
        this.wants  = wantsFacade.findPhotosByUserID(user.getId());;
    }
    
    public String viewMyBinder(){
        user = getLoggedInUser();
        wants = wantsFacade.findPhotosByUserID(user.getId());
        return "MyBinder";
    }

    
}
