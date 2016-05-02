package com.mycompany.jsfpackage;

/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
import com.mycompany.entitypackage.CardPhotos;
import com.mycompany.jsfpackage.util.JsfUtil;
import com.mycompany.jsfpackage.util.PaginationHelper;
import com.mycompany.sessionBeanPackage.CardPhotosFacade;

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
@Named("cardPhotosController")
@SessionScoped
public class CardPhotosController implements Serializable {

    private CardPhotos current;
    private DataModel items = null;
    @EJB
    private com.mycompany.sessionBeanPackage.CardPhotosFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    /**
     * Controller empty constructor
     */
    public CardPhotosController() {
    }

       /**
        * Get the selected photos stored right now
        * @return the cardphotos that are selected 
        */
    public CardPhotos getSelected() {
        if (current == null) {
            current = new CardPhotos();
            selectedItemIndex = -1;
        }
        return current;
    }

    /**
     * Return the facade we're using for cardphotos
     * @return 
     */
    private CardPhotosFacade getFacade() {
        return ejbFacade;
    }

    /**
     * Pagination helper so that cardphotos are displayed in a reasonable quantity
     * @return the pagination helper object suited to the page sizes we need 
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
     * Prepare the list of cardphotos
     * @return the string value of the xhtml file we're showing next
     */
    public String prepareList() {
        recreateModel();
        return "List";
    }

    /**
     * Prepare the values we need to view one cardphoto object
     * @return the string value of the xhtml file we're showing next
     */
    public String prepareView() {
        current = (CardPhotos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    /**
     * Prepare the values we need for creating a cardphoto
     * @return the string value of the xhtml file we're showing next
     */
    public String prepareCreate() {
        current = new CardPhotos();
        selectedItemIndex = -1;
        return "Create";
    }
    
    /**
     * Create a new cardphoto using the facade and EJB
     * @return The string value from prepare create which should be that xhtml file
     */

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CardPhotosCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Prepares the values for editing a cardphoto
     * @return the string value of the xhtml file we're using
     */
    public String prepareEdit() {
        current = (CardPhotos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    /**
     * Updates the cardphotos and returns to the view
     * @return the xhtml name of the file we're going to redirect to
     */
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CardPhotosUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Destroys a cardphoto in the database
     * @return the xhtml name of the file we're redirecting to
     */
    public String destroy() {
        current = (CardPhotos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    /**
     * Destroys a cardphoto but returns to the view on success, not the list again
     * @return the xhtml name of the file we're redirecting to
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
     * This actually takes the facade and destroys the current cardphoto
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CardPhotosDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    /**
     * THis updates the current item to be the one we've selected.
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
     * This one will return the bean of the thing we're getting
     * @return the datamodel for cardphoto
     */
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    /**
     * When we need to reset the data model we're using
     */
    private void recreateModel() {
        items = null;
    }

    /**
     * For when you need to reset the pagination
     */
    private void recreatePagination() {
        pagination = null;
    }

    /**
     * Get the next page in a paginated table
     * @return the next page in the pagination
     */
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    /**
     * Get the previous page in a paginated table
     * @return the previous page in the pagination
     */
    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    /**
     * Get the available cardphotos but select all of them
     * @return an array of selected items that is all cardphotos
     */
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }
/**
 * get one available cardphoto
 * @return the cardphoto you got
 */
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    /**
     * Get the cardphoto with the established id
     * @param id is the id of the cardphoto we're looking for
     * @return the cardphoto with the id in the param
     */
    public CardPhotos getCardPhotos(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    /**
     * Establish a converter for the cardphotos controller so that the 
     * rest of the project can understand what it is
     */
    @FacesConverter(forClass = CardPhotos.class)
    public static class CardPhotosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CardPhotosController controller = (CardPhotosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cardPhotosController");
            return controller.getCardPhotos(getKey(value));
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
            if (object instanceof CardPhotos) {
                CardPhotos o = (CardPhotos) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + CardPhotos.class.getName());
            }
        }

    }

}
