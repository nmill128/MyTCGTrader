/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.jsfpackage;

import com.mycompany.entitypackage.Users;
import com.mycompany.jsfpackage.util.JsfUtil;
import com.mycompany.jsfpackage.util.PaginationHelper;
import com.mycompany.sessionBeanPackage.UsersFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author Erik
 * 
 * CardPhotos generated controller class with additional methods to help with
 * viewing and creating from the mybinder page
 */
@Named("usersController")
@SessionScoped
public class UsersController implements Serializable {

    private Users current;
    private DataModel items = null;
    @EJB
    private com.mycompany.sessionBeanPackage.UsersFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    /**
     * Empty Constructor
     */
    public UsersController() {
    }
    
    /**
    * Get the selected Users
    * @return all of the selected users
    */
    public Users getSelected() {
        if (current == null) {
            current = new Users();
            selectedItemIndex = -1;
        }
        return current;
    }

    /**
     * GEt the users facade so that it can be connected
     * @return the usersfacade
     */
    private UsersFacade getFacade() {
        return ejbFacade;
    }

    /**
     * Get the pagination helper for users
     * @return the users pagination helper
     */
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    /**
     * Prepare to list out all the users
     * @return the xhtml file's name
     */
    public String prepareList() {
        recreateModel();
        return "List";
    }

    /**
     * Prepare to view one specific user
     * @return the xhtml file's name
     */
    public String prepareView() {
        current = (Users) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    /**
     * Get ready to create one new user
     * @return the xhtml file's name
     */
    public String prepareCreate() {
        current = new Users();
        selectedItemIndex = -1;
        return "Create";
    }

    /**
     * Create a new user using the facade
     * @return the xhtml file's name if it works, null if not
     */
    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsersCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Prepare to edit one user
     * @return the xhtml file's name
     */
    public String prepareEdit() {
        current = (Users) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    /**
     * Update a user using the facade
     * @return the xhtml file's name if it works, null if not
     */
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsersUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Destroy a user using the facade
     * @return the xhtml file's name
     */
    public String destroy() {
        current = (Users) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    /**
     * Destroy a user using the facade and view the next one if another one
     * exists
     * @return the xhtml file's name 
     */
    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    /**
     * Actually destroy a user from the database
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsersDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    /**
     * Edit the current user in the database
     */
    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    /**
     * Get the datamodel of the item we're using
     * @return 
     */
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    /**
     * Drop the datamodel we have so a new one can be gotten
     */
    private void recreateModel() {
        items = null;
    }

    /**
     * Drop the pagination helper so we can get a new one
     */
    private void recreatePagination() {
        pagination = null;
    }

    /**
     * Get the next page of pagination
     * @return the xhtml file's name
     */
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    /**
     * Get the previuos page of pagination
     * @return the xhtml file's name
     */
    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    /**
     * Get every user we have
     * @return an array with every user
     */
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    /**
     * Get the first user there is
     * @return an array with the first user
     */
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    /**
     * Get the user with a specific ID
     * @param id is the id we're looking for
     * @return the user with id id if there is one
     */
    public Users getUsers(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    /**
     * Make this class into a controller specifically for the users class
     */
    @FacesConverter(forClass = Users.class)
    public static class UsersControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsersController controller = (UsersController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usersController");
            return controller.getUsers(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Users) {
                Users o = (Users) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Users.class.getName());
            }
        }

    }

}
