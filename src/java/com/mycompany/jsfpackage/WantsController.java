/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.jsfpackage;

import com.mycompany.entitypackage.Wants;
import com.mycompany.jsfpackage.util.JsfUtil;
import com.mycompany.jsfpackage.util.PaginationHelper;
import com.mycompany.sessionBeanPackage.UsersFacade;
import com.mycompany.sessionBeanPackage.WantsFacade;

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
@Named("wantsController")
@SessionScoped
public class WantsController implements Serializable {

    private Wants current;
    private DataModel items = null;
    @EJB
    private com.mycompany.sessionBeanPackage.WantsFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    @EJB
    private UsersFacade userFacade;

    /**
     * Empty constructor for wants controller
     */
    public WantsController() {
    }
    
    
    /**
     * Return the selected wants from the currently selected values
     * @return 
     */
    public Wants getSelected() {
        if (current == null) {
            current = new Wants();
            selectedItemIndex = -1;
        }
        return current;
    }

    /**
     * Get the facade associated with wants
     * @return 
     */
    private WantsFacade getFacade() {
        return ejbFacade;
    }

    /**
     * Get the pagination helper for wants
     * @return the pagination helper for wants lists
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
     * Get ready to display a list of the wants that exist
     * @return the xhtml file's name
     */
    public String prepareList() {
        recreateModel();
        return "List";
    }

    /**
     * Prepare to display a view of one specific want
     * @return the xhtml file's name
     */
    public String prepareView() {
        current = (Wants) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    /**
     * Prepare to create a want
     * @return the xhtml file's name
     */
    public String prepareCreate() {
        current = new Wants();
        selectedItemIndex = -1;
        return "CreateWant";
    }

    /**
     * Create a want from the facade
     * @return the xhtml file's name if it works and null if not
     */
    public String create() {
        try {
           
            current.setUserId(userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id")));
            getFacade().create(current);
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("WantsCreated"));
            return "MyBinder";
        } catch (Exception e) {
            //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Prepare to edit a single want
     * @return the xhtml file's name
     */
    public String prepareEdit() {
        current = (Wants) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    /**
     * edit a want from the binder with a specific ID in the wants
     * @param id is the id we're looking for
     * @return the xhtml file's name
     */
    public String binderEdit(int id) {
        current = (Wants) getFacade().find(id);
        return "EditWant";
    }

    /**
     * Update the want with the facade
     * @return the xhtml file's name if successful, null if not
     */
    public String update() {
        try {
            getFacade().edit(current);
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("WantsUpdated"));
            return "MyBinder";
        } catch (Exception e) {
            //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * destroy the want using the helper method
     * @return the xhtml file's name
     */
    public String destroy() {
        current = (Wants) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    /**
     * Destroy the want then go back to my binder
     * @param id is the id of the want we have
     * @return the xhtml file's name
     */
    public String binderDestroy(int id) {
        current = (Wants) getFacade().find(id);
        performDestroy();
        return "MyBinder";
    }

    /**
     * Destroy the want and go back to viewing the next selected item
     * @return the xhtml file's name view if tehre is more, list if not
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
     * Actually destroy the want in the DB
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("WantsDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    /**
     * Actually update the item in the DB
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
     * Get the datamodel for wants from the facade
     * @return the wants datamodel
     */
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    /**
     * drop the model to redo it
     */
    private void recreateModel() {
        items = null;
    }

    /**
     * Drop the pagination to redo it
     */
    private void recreatePagination() {
        pagination = null;
    }

    /**
     * Get the next page
     * @return the xhtml file's name
     */
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    /**
     * Get the previous page
     * @return the xhtml file's name
     */
    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    /**
     * Get every want in the DB
     * @return an array of all the wants in the DB
     */
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    /**
     * Get the first want in the DB
     * @return the first wnat in the DB
     */
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    /**
     * Return the want with the listed id 
     * @param id is the id we're looking for in the wants
     * @return the want with that id
     */
    public Wants getWants(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    /**
     * Convert this class into a controller specifically for the wants class
     * so that primefaces can use it
     */
    @FacesConverter(forClass = Wants.class)
    public static class WantsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            WantsController controller = (WantsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "wantsController");
            return controller.getWants(getKey(value));
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
            if (object instanceof Wants) {
                Wants o = (Wants) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Wants.class.getName());
            }
        }

    }

}
