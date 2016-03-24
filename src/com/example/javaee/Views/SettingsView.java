package com.example.javaee.Views;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang3.time.DateUtils;

import com.example.javaee.JavaeeUI;
import com.example.javaee.Forms.LoginForm;
import com.example.javaee.Forms.RegisterForm;
import com.example.javaee.Models.User;
import com.example.javaee.Models.UserProfile;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.client.ui.calendar.schedule.DateUtil;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.converter.DateToSqlDateConverter;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;


@SuppressWarnings("serial")
public class SettingsView extends Panel implements View {

	private BeanFieldGroup<UserProfile> fieldGroup;
	
    @PropertyId("firstname")
    private TextField firstNameField;
    @PropertyId("lastname")
    private TextField lastNameField;
    @PropertyId("birth_date")
    private DateField birthDateField;
    @PropertyId("street")
    private TextField streetField;
    @PropertyId("flat_number")
    private TextField flatNumbereField;
    @PropertyId("city")
    private TextField cityField;
    @PropertyId("post_code")
    private TextField postCodeField;
    @PropertyId("phone")
    private TextField phoneField;
    
    
    
    private Button but;
    private Button but2;
	
	public SettingsView() 
	{
		setSizeUndefined();
    	setWidth("100%");
	
		VerticalLayout layout = new VerticalLayout();
		setContent(layout);
		layout.setSizeFull();
		layout.setMargin(true);
		
		
		layout.addComponent(new Header());
		 
		layout.addComponent(new Menu(8));
		
		VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setWidth("50%");
        
        content.setMargin(new MarginInfo(true, false, false, false));
        layout.addComponent(content);
        layout.setComponentAlignment(content, Alignment.TOP_CENTER);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        but = new Button("Zapisz");
        but2 = new Button("Zmień hasło");
        
        detailsWrapper.addComponent(buildProfileTab());
        detailsWrapper.addComponent(buildPasswordTab());
       
        
        EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
        
       try{
    	   	Query q = em.createQuery("SELECT u FROM User u WHERE u.id = :un");
        	q.setParameter("un", VaadinSession.getCurrent().getAttribute("user_id"));
        	User usr = (User)q.getSingleResult();
        	
        	q = em.createQuery("SELECT u FROM UserProfile u WHERE u.user = :un");
        	q.setParameter("un", usr);
        	UserProfile us = (UserProfile)q.getSingleResult();
        	
        	fieldGroup = new BeanFieldGroup<UserProfile>(UserProfile.class);
	        fieldGroup.bindMemberFields(this);
	        fieldGroup.setItemDataSource(us);

	        
	        but.addClickListener(new Button.ClickListener() {
	 		
	 		@Override
	 		public void buttonClick(ClickEvent event) {
	 			
	 			
	 			em.getTransaction().begin();
	 			us.setFirstname(firstNameField.getValue());
	        	us.setLastname(lastNameField.getValue());
	        	us.setBirth_date(new Date(birthDateField.getValue().getTime()));
	        	us.setStreet(streetField.getValue());
	        	us.setFlat_number(flatNumbereField.getValue());
	        	us.setCity(cityField.getValue());
	        	us.setPost_code(postCodeField.getValue());
	        	us.setPhone(phoneField.getValue());
            	em.getTransaction().commit();
            	
            	Notification notification = new Notification("Sukces");
 		        notification.setDescription("<span>Dane zostały zauktualizowane</span> ");
 		        notification.setHtmlContentAllowed(true);
 		        notification.setStyleName("tray dark small closable login-help");
 		        notification.setPosition(Position.TOP_CENTER);
 		        notification.setDelayMsec(3000); //zniknie po 3 sek
 		        notification.show(Page.getCurrent());
	 		}
	 	});
	        
	       
        }catch(NoResultException e){}
		
		Label footer = new Label("Stopka - mamkwadrat.pl");
       	
		layout.addComponent(footer);
		
	}
	
	private Component buildPasswordTab() {
        VerticalLayout root = new VerticalLayout();
        root.setCaption("Zmień hasło");
        root.setIcon(FontAwesome.COGS);
        root.setSpacing(true);
        root.setMargin(true);
        root.setSizeFull();

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);
        details.setSpacing(true);

        Label section = new Label("Zmiana hasła");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);
        
        PasswordField oldPassword = new PasswordField("Stare haslo");
        details.addComponent(oldPassword);
        PasswordField newPassword = new PasswordField("Nowe haslo");
        details.addComponent(newPassword);
        PasswordField newPasswordAgain = new PasswordField("Powtorz haslo");
        details.addComponent(newPasswordAgain);
        
        details.addComponent(but2);
        but2.addStyleName(ValoTheme.BUTTON_PRIMARY);
        but2.setClickShortcut(KeyCode.ENTER);
        
        EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
        
        try{
         	Query q = em.createQuery("SELECT u FROM User u WHERE u.id = :un");
         	q.setParameter("un", VaadinSession.getCurrent().getAttribute("user_id"));
         	User us = (User)q.getSingleResult();

 	        
 	        but2.addClickListener(new Button.ClickListener() {
 	 		
 	 		@Override
 	 		public void buttonClick(ClickEvent event) {
 	 			
 	 			String pass = oldPassword.getValue();
					/* try {
						MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
						messageDigest.update(pass.getBytes());
						pass = new String(messageDigest.digest());
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
 	 			*/
 	 			if(us.getPassword().equals(pass))
 	 			{
 	 				if(newPassword.getValue().equals(newPasswordAgain.getValue()))
 	 				{
 	 					pass = newPassword.getValue();
 						/* try {
 							MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
 							messageDigest.update(pass.getBytes());
 							pass = new String(messageDigest.digest());
 						} catch (NoSuchAlgorithmException e1) {
 							// TODO Auto-generated catch block
 							e1.printStackTrace();
 						}*/
 	 					
 	 					em.getTransaction().begin();
 	 	 	 			us.setPassword(pass);
 	 	             	em.getTransaction().commit();
 	 	             	
 	 	             	oldPassword.clear();
 	 	             	newPassword.clear();
 	 	 				newPasswordAgain.clear();
 	 	 				Notification notification = new Notification("Sukces");
 	 	 		        notification.setDescription("<span>Haslo zostalo zmienione</span> ");
 	 	 		        notification.setHtmlContentAllowed(true);
 	 	 		        notification.setStyleName("tray dark small closable login-help");
 	 	 		        notification.setPosition(Position.TOP_CENTER);
 	 	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
 	 	 		        notification.show(Page.getCurrent());
 	 				}
 	 				else
 	 				{
 	 					newPassword.clear();
 	 	 				newPasswordAgain.clear();
 	 	 				Notification notification = new Notification("Ups.. wystapil blad");
 	 	 		        notification.setDescription("<span>Podane hasla roznia sie</span> ");
 	 	 		        notification.setHtmlContentAllowed(true);
 	 	 		        notification.setStyleName("warning failure small closable login-help");
 	 	 		        notification.setPosition(Position.TOP_CENTER);
 	 	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
 	 	 		        notification.show(Page.getCurrent());
 	 				}
 	 			}
 	 			else
 	 			{
 	 				oldPassword.focus();
 	 				newPassword.clear();
 	 				newPasswordAgain.clear();
 	 				Notification notification = new Notification("Ups.. wystapil blad");
 	 		        notification.setDescription("<span>Podales niepoprawne haslo</span> ");
 	 		        notification.setHtmlContentAllowed(true);
 	 		        notification.setStyleName("warning failure small closable login-help");
 	 		        notification.setPosition(Position.TOP_CENTER);
 	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
 	 		        notification.show(Page.getCurrent());
 	 			}
 	 		}
 	 	});
 	        
 	       
         }catch(NoResultException e){}
        
        return root;
    }

    private Component buildProfileTab() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Edytuj profil");
        root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);
        details.setSpacing(true);

        Label section = new Label("Dane personalne");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);
        
        firstNameField = new TextField("Imie");
        details.addComponent(firstNameField);
        lastNameField = new TextField("Nazwisko");
        details.addComponent(lastNameField);
        birthDateField = new DateField("Data urodzenia");
        details.addComponent(birthDateField);
        phoneField = new TextField("Telefon");
        details.addComponent(phoneField);
        
        section = new Label("Adres zamieszkania");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);

        streetField = new TextField("Ulica");
        details.addComponent(streetField);
        flatNumbereField = new TextField("Nr domu");
        details.addComponent(flatNumbereField);
        cityField = new TextField("Miasto");
        details.addComponent(cityField);
        postCodeField = new TextField("Kod pocztowy");
        details.addComponent(postCodeField);
        
        details.addComponent(but);
        but.addStyleName(ValoTheme.BUTTON_PRIMARY);
        but.setClickShortcut(KeyCode.ENTER); 
        


        return root;
    }
	
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
