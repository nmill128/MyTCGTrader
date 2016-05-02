/*
 * Created by Osman Balci on 2016.02.14  * 
 * Copyright © 2016 Osman Balci. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entitypackage.Users;
import com.mycompany.sessionBeanPackage.UsersFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;


@Named(value = "passwordResetManager")
@SessionScoped
/**
 *
 * @author Balci
 */
public class PasswordResetManager implements Serializable{
    
    // Instance Variables (Properties)
    private String username;
    private String message = "";
    private String answer;
    private String password;
    
    /**
     * The instance variable 'userFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject in
     * this instance variable a reference to the @Stateless session bean UserFacade.
     */
    @EJB
    private UsersFacade userFacade;

    /**
     * gets the username
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * sets the username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * gets message
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * sets message
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }
        
    /**
     * checks to see if username exists then moves to secuirtyQuestion
     * @return redirect
     */
    public String usernameSubmit() {
        Users user = userFacade.findByUsername(username);
        if (user == null) {
            message = "Entered username does not exist!";
            return "EnterUsername?faces-redirect=true";
        }
        else {
            message = "";
            return "SecurityQuestion?faces-redirect=true";
        }
    }
    
    /**
     * Checks to see if answer is correct
     * @return redirect
     */
    public String securityquestionSubmit() {
        Users user = userFacade.findByUsername(username);
        if (user.getSecurityAnswer().equals(answer)) {
            message = "";
            return "ResetPassword?faces-redirect=true";
        }
        else {
            message = "Answer incorrect";
            return "SecurityQuestion?faces-redirect=true";
        }
    }
    
    /**
     * gets security q
     * @return
     */
    public String getSecurityQuestion() {
        int question = userFacade.findByUsername(username).getSecurityQuestion();
        return Constants.QUESTIONS[question];
    }

    /**
     * gets answer
     * @return
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * sets answer 
     * @param answer
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * validates new password
     * @param event
     */
    public void validateInformation(ComponentSystemEvent event) {
        FacesContext fc = FacesContext.getCurrentInstance();

        UIComponent components = event.getComponent();
        // get password
        UIInput uiInputPassword = (UIInput) components.findComponent("password");
        String pwd = uiInputPassword.getLocalValue() == null ? ""
                : uiInputPassword.getLocalValue().toString();

        // get confirm password
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmPassword");
        String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? ""
                : uiInputConfirmPassword.getLocalValue().toString();

        if (pwd.isEmpty() || confirmPassword.isEmpty()) {
            // Do not take any action. 
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }

        if (!pwd.equals(confirmPassword)) {
            message = "Passwords must match!";
        } else {
            message = "";
        }   
    }   

    /**
     * get password
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * set password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * redirects and prepares reset password
     * @return
     */
    public String resetPassword() {
        if (message.equals("")) {
            message = "";
            Users user = userFacade.findByUsername(username);
            try {
                user.setPassword(password);
                userFacade.edit(user);
                username = answer = password = "";                
            } catch (EJBException e) {
                message = "Something went wrong editing your profile, please try again!";
                return "ResetPassword?faces-redirect=true";            
            }
            return "index?faces-redirect=true";            
        }
        else {
            return "ResetPassword?faces-redirect=true";            
        }
    }
            
}