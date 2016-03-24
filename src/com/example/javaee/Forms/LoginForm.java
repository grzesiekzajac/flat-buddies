package com.example.javaee.Forms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tools.ant.taskdefs.Filter;
import org.openqa.selenium.remote.server.handler.GetPageSource;

import pl.smsapi.Client;
import pl.smsapi.api.SmsFactory;
import pl.smsapi.api.action.sms.SMSSend;
import pl.smsapi.api.response.MessageResponse;
import pl.smsapi.api.response.StatusResponse;

import com.example.javaee.JavaeeUI;
import com.example.javaee.Models.Flat;
import com.example.javaee.Models.User;
import com.example.javaee.Views.MainView;
import com.vaadin.addon.jpacontainer.EntityManagerProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.client.data.DataSource;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class LoginForm extends VerticalLayout {

	public LoginForm()
	{
		setSizeFull();
        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
	}
	
	private Component buildLoginForm()
	{
		final FormLayout loginPanel = new FormLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabel());
        loginPanel.addComponent(buildFields());
        return loginPanel;
	}
	
	private Component buildFields() 
	{
        FormLayout fields = new FormLayout();
        fields.setSpacing(true);

        final TextField username = new TextField("Login");
        username.setIcon(FontAwesome.USER);
        //username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField password = new PasswordField("Hasło");
        password.setIcon(FontAwesome.LOCK);
        //password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Zaloguj się");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();
        
        //final CheckBox remember = new CheckBox("Zapamiętaj mnie", true);

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new ClickListener() {
        	
        	
        	
            @SuppressWarnings("deprecation")
			@Override
            public void buttonClick(final ClickEvent event) {
            	
            	 
            	
            	if (!username.isValid() || !password.isValid()) {
		            return;
		        }
            	
		        String user = username.getValue();
		        String pass = password.getValue();
		        
		        EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
		        User us;
		        
		        try{
		        	Query q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :un");
	            	q.setParameter("un", user);
	            	us = (User)q.getSingleResult();
		        }catch(NoResultException e)
		        {
		        	Notification notification = new Notification("Wystąpił błąd",
		        			"Nie znaleziono użytkownika <strong>" + user + "</strong>",
		        			Notification.TYPE_ERROR_MESSAGE);
		        	notification.setHtmlContentAllowed(true);
		        	notification.setStyleName("tray dark small closable login-help");
		            notification.setPosition(Position.BOTTOM_CENTER);
		            notification.setDelayMsec(5000); //zniknie po 20 sek
		        	notification.show(Page.getCurrent());
		        	return;
		        }
		        //
		        // Validate username and password with database here. For examples sake
		        // I use a dummy username and password.
		        
		        /*try {
					MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
					messageDigest.update(pass.getBytes());
					pass = new String(messageDigest.digest());
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
		        
		        //pass = DigestUtils.md5Hex(pass);
		        
		        boolean isValid = pass.equals(us.getPassword());

		        if (isValid) {

		            // Store the current user in the service session
		            getSession().setAttribute("user", us.getUser_name());
		            getSession().setAttribute("user_id", us.getId());
		           
		            
		            try{
		             	
		             	if(us.getFlat() != null)
		             	{
		    	         	Query q = em.createQuery("SELECT f FROM Flat f WHERE f.id = :uf");
		    	         	q.setParameter("uf", us.getFlat().getId());
		    	         	Flat flat = (Flat)q.getSingleResult();
		    	         	getSession().setAttribute("flat_name", flat.getFlat_name());

		             	}
		             	else
		             	{
		             		getSession().setAttribute("flat_name", null);
		             	}
		            }
		            catch(NoResultException e){}

		            // Navigate to main view
		            getUI().getNavigator().addView("main", new MainView());
		            getUI().getNavigator().navigateTo("main");//

		        } else {

		            // Wrong password clear the password field and refocuses it
		            password.setValue("");
		            password.focus();
		            Notification notification = new Notification("Uwaga");
 	 		        notification.setDescription("<span>Bledne haslo</span> ");
 	 		        notification.setHtmlContentAllowed(true);
 	 		        notification.setStyleName("warning failure small closable login-help");
 	 		        notification.setPosition(Position.TOP_CENTER);
 	 		        notification.setDelayMsec(5000); //zniknie po 5 sek
 	 		        notification.show(Page.getCurrent());

		        }
            }
        });
        
        return fields;
    }

    private Component buildLabel() 
    {
        CssLayout label = new CssLayout();
        label.addStyleName("label");

        Label welcome = new Label("Logowanie");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        label.addComponent(welcome);

        return label;
    }
}
