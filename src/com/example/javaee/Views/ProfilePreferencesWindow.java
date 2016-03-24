package com.example.javaee.Views;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.example.javaee.Models.User;
import com.example.javaee.Models.UserProfile;
import com.google.common.base.Optional;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ProfilePreferencesWindow extends VerticalLayout{

	private BeanFieldGroup<UserProfile> fieldGroup;
    /*
     * Fields for editing the User object are defined here as class members.
     * They are later bound to a FieldGroup by calling
     * fieldGroup.bindMemberFields(this). The Fields' values don't need to be
     * explicitly set, calling fieldGroup.setItemDataSource(user) synchronizes
     * the fields with the user object.
     */
    @PropertyId("firstname")
    private TextField firstNameField;
    @PropertyId("lastname")
    private TextField lastNameField;

	public ProfilePreferencesWindow() {
	        addStyleName("profile-window");
	        //Responsive.makeResponsive(this);
	        setWidth(50.0f, Unit.PERCENTAGE);
	        

	        VerticalLayout content = new VerticalLayout();
	        content.setSizeFull();
	        content.setMargin(new MarginInfo(true, false, false, false));
	        addComponent(content);

	        TabSheet detailsWrapper = new TabSheet();
	        detailsWrapper.setSizeFull();
	        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
	        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
	        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
	        content.addComponent(detailsWrapper);
	        content.setExpandRatio(detailsWrapper, 1f);

	        detailsWrapper.addComponent(buildProfileTab());
	        detailsWrapper.addComponent(buildPasswordTab());
	        
	        
	        System.out.print("siema");
	        EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
	        
	       try{
	        	//em.getTransaction().begin();
	        	Query q = em.createQuery("SELECT u FROM UserProfile u WHERE u.firstname = :un");
            	q.setParameter("un", "Wojciech");
            	UserProfile us = (UserProfile)q.getSingleResult();
            	//em.getTransaction().commit();
            	fieldGroup = new BeanFieldGroup<UserProfile>(UserProfile.class);
    	        fieldGroup.bindMemberFields(this);
    	        fieldGroup.setItemDataSource(us);

    	        Button but = new Button("Zapisz");
    	        but.addClickListener(new Button.ClickListener() {
    	 		
    	 		@Override
    	 		public void buttonClick(ClickEvent event) {
    	 			
    	 			em.getTransaction().begin();
    	        	us.setLastname(lastNameField.getValue());
                	em.getTransaction().commit();
    	 		}
    	 	});
    	        
    	        content.addComponent(but);
	        }catch(NoResultException e)
	        {
	        	//us = new UserProfile();
	        }

	        
	    }

	    private Component buildPasswordTab() {
	        VerticalLayout root = new VerticalLayout();
	        root.setCaption("Zmień hasło");
	        root.setIcon(FontAwesome.COGS);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.setSizeFull();

	        Label message = new Label("Not implemented in this demo");
	        message.setSizeUndefined();
	        message.addStyleName(ValoTheme.LABEL_LIGHT);
	        root.addComponent(message);
	        root.setComponentAlignment(message, Alignment.MIDDLE_CENTER);

	        return root;
	    }

	    private Component buildProfileTab() {
	        HorizontalLayout root = new HorizontalLayout();
	        root.setCaption("Edytuj profil");
	        root.setIcon(FontAwesome.USER);
	        root.setWidth(100.0f, Unit.PERCENTAGE);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.addStyleName("profile-form");

	        VerticalLayout pic = new VerticalLayout();
	        pic.setSizeUndefined();
	        pic.setSpacing(true);


	        root.addComponent(pic);

	        FormLayout details = new FormLayout();
	        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
	        root.addComponent(details);
	        root.setExpandRatio(details, 1);

	        firstNameField = new TextField("First Name");
	        details.addComponent(firstNameField);
	        lastNameField = new TextField("Last Name");
	        details.addComponent(lastNameField);


	        return root;
	    }

	}
