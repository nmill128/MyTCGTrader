/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.jsfpackage;

import com.mycompany.entitypackage.UserPhotos;
import com.mycompany.jsfpackage.util.JsfUtil;
import com.mycompany.jsfpackage.util.PaginationHelper;
import com.mycompany.sessionBeanPackage.UserPhotosFacade;

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
@Named("userPhotosController")
@SessionScoped
public class UserPhotosController implements Serializable {

    private UserPhotos current;
    private DataModel items = null;
    @EJB
    private com.mycompany.sessionBeanPackage.UserPhotosFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    /**
     * Empty constructor for user photos
     */
    public UserPhotosController() {
    }

    /**
     * Get the selected userphoto
     * @return 
     */
    public UserPhotos getSelected() {
        if (current == null) {
            current = new UserPhotos();
            selectedItemIndex = -1;
        }
        return current;
    }

    /**
     * get the userphoto facade
     * @return the facade associated with userphotos
     */
    private UserPhotosFacade getFacade() {
        return ejbFacade;
    }

    /**
     * get the pagination helper generated for user photos
     * @return the pagination helper for userphotos
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
     * Prepare to display the list of userphotos 
     * @return the xhtml file's name
     */
    public String prepareList() {
        recreateModel();
        return "List";
    }

    /**
     * Prepare to display a view of one user photo
     * @return the xhtml file's name
     */
    public String prepareView() {
        current = (UserPhotos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    /**
     * Prepare to display the screen for creation of a userphoto
     * @return the xhtml file's name
     */
    public String prepareCreate() {
        current = new UserPhotos();
        selectedItemIndex = -1;
        return "Create";
    }

    /**
     * Create a userphoto in the DB
     * @return the xhtml file's name if successful, null if not
     */
    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserPhotosCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Prepare to display the screen for editing a user photo
     * @return the xhtml file's name
     */
    public String prepareEdit() {
        current = (UserPhotos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    /**
     * UPdate the userphoto in the DB
     * @return the xhtml file's name if it worked, null if not
     */
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserPhotosUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Destroy the user photo object
     * @return the xhtml file's name if its successful, null if not
     */
    public String destroy() {
        current = (UserPhotos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    /**
     * Destroy the user photo object but view the next in a selected list if one exists
     * @return the xhtml file's name selected by the remaining selected items
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
     * Actually destroy the userphoto in the DB
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserPhotosDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    /**
     * Update a userphoto in the DB
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
     * Get the items on a page
     * @return the datamodel of items in a page
     */
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    /**
     * Drop the current data model
     */
    private void recreateModel() {
        items = null;
    }

    /**
     * Drop the pagination helper
     */
    private void recreatePagination() {
        pagination = null;
    }

    /**
     * Get the next page for the list view
     * @return the xhtml file's name
     */
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    /**
     * Get the previous page of the list view
     * @return the xhtml file's name
     */
    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    /**
     * Get all of the user photos in the db
     * @return an array of all user photos
     */
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    /**
     * Get the first user photo in the DB
     * @return an array of one user photo
     */
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    /**
     * Get the user photo with the specified ID
     * @param id is the id we're looking for
     * @return the user photo with the id ID
     */
    public UserPhotos getUserPhotos(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    /**
     * Convert this class into an actual controller to be used.
     */
    @FacesConverter(forClass = UserPhotos.class)
    public static class UserPhotosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserPhotosController controller = (UserPhotosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userPhotosController");
            return controller.getUserPhotos(getKey(value));
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
            if (object instanceof UserPhotos) {
                UserPhotos o = (UserPhotos) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + UserPhotos.class.getName());
            }
        }

    }

}
