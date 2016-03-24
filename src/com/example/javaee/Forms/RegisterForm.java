package com.example.javaee.Forms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.rmi.CORBA.UtilDelegate;

import org.apache.commons.codec.digest.DigestUtils;

import com.example.javaee.Models.User;
import com.example.javaee.Models.UserProfile;
import com.example.javaee.Views.StartView;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class RegisterForm extends VerticalLayout {

	private Notification notification2;
	
	public RegisterForm()
	{
		setSizeFull();
        Component registerForm = buildRegisterForm();
        addComponent(registerForm);
        setComponentAlignment(registerForm, Alignment.MIDDLE_CENTER);
	}
	
	private Component buildRegisterForm()
	{
		final FormLayout registerPanel = new FormLayout();
        registerPanel.setSizeUndefined();
        registerPanel.setSpacing(true);
        Responsive.makeResponsive(registerPanel);
        registerPanel.addStyleName("login-panel");

        registerPanel.addComponent(buildLabel());
        registerPanel.addComponent(buildFields());
        return registerPanel;
	}
	
	private Component buildFields() 
	{
        FormLayout fields = new FormLayout();
        fields.setSpacing(true);
 

        final TextField username = new TextField("Login");
        username.setIcon(FontAwesome.USER);
        username.setDescription("Nazwa użytkownika, dzięki której możesz się zalogować do portalu mamkwadrat.");
        username.setInputPrompt("Np. wojtek92");

        final PasswordField password = new PasswordField("Hasło");
        password.setIcon(FontAwesome.LOCK);
        password.setDescription("Hasło powinno składać się z dużych i małych liter oraz znaków specjalnych.");
        password.setInputPrompt("aaaaa");
        
        final TextField email = new TextField("Email");
        email.setIcon(FontAwesome.ENVELOPE);
        email.addValidator(new EmailValidator("Podaj poprawny email!"));
        email.setDescription("Podaj poprawny email, aby otrzymywać wiadomości o nowościach w portalu.");
        email.setInputPrompt("example@example.com");
                
        final PasswordField passwordRepeat = new PasswordField("Powtórz hasło");
        passwordRepeat.setIcon(FontAwesome.LOCK);
        passwordRepeat.setDescription("Podaj hasło ponownie, aby być pewnym, że się nie pomyliłeś!");
        passwordRepeat.setInputPrompt("aaaaa");

        
        final TextField phone = new TextField("Telefon");
        phone.setIcon(FontAwesome.PHONE);
        //passwordRepeat.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        phone.setInputPrompt("500123456");
        phone.setDescription("Podaj swój numer telefonu, aby otrzymywać powiadomienia o nowych zdarzeniach w Twoim mieszkaniu.");
        
        final Button register = new Button("Rejestracja");
        register.addStyleName(ValoTheme.BUTTON_PRIMARY);
        register.setClickShortcut(KeyCode.ENTER);
        register.focus();
        
        username.setRequired(true);
        password.setRequired(true);
        email.setRequired(true);
        passwordRepeat.setRequired(true);

        fields.addComponents(username, email, password, passwordRepeat, phone, register);
        fields.setComponentAlignment(register, Alignment.BOTTOM_LEFT);

        register.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	
            	
              
            	String user = username.getValue();
            	String pass = password.getValue();
            	String passRepeat = passwordRepeat.getValue();
            	String emailS = email.getValue();
            	
            	EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
		        User us;
		        
		        try{
		        	Query q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :un");
	            	q.setParameter("un", user);
	            	us = (User)q.getSingleResult();
	            	
	            	Notification notification = new Notification("Uwaga");
 	 		        notification.setDescription("<span>Użytkownik <b> "+ us.getUser_name() + "</b> istnieje</span> ");
 	 		        notification.setHtmlContentAllowed(true);
 	 		        notification.setStyleName("warning failure small closable login-help");
 	 		        notification.setPosition(Position.TOP_CENTER);
 	 		        notification.setDelayMsec(5000); //zniknie po 5 sek
 	 		        notification.show(Page.getCurrent());
 	 		        
 	 		        username.clear();
 	 		        username.focus();
	            	
		        }catch(NoResultException e)
		        {
		        	if(!pass.equals(passRepeat))
	            	{
	            		password.focus();
	            		password.setValue("");
	            		passwordRepeat.setValue("");
	            		return;
	            	}
	            	
	            	/*try {
						MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
						messageDigest.update(pass.getBytes());
						pass = new String(messageDigest.digest());
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
		        	
		        	//pass = DigestUtils.md5Hex(pass);
	            	
	            	UserProfile up = new UserProfile();
	            	
	            	User userObj = new User();
	            	userObj.setUser_name(user);
	            	userObj.setPassword(pass);
	            	System.out.println(pass);
	            	userObj.setEmail(emailS);
	            	userObj.setActive(true);
	            	userObj.setRegister_dateTime(new Date(Calendar.getInstance().getTimeInMillis()));
	            	
	            	up.setUser(userObj);
	            	up.setPhone(phone.getValue());
	            	up.setCity("");
	            	up.setFirstname("");
	            	up.setFlat_number("");
	            	up.setLastname("");
	            	up.setPost_code("");
	            	up.setStreet("");
	            	
			        //EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");

			        try{
			        	em.getTransaction().begin();
			        	em.persist(userObj);
			        	em.persist(up);
			        	em.getTransaction().commit();
			        	

			        	
			        	Notification notification = new Notification("Rejestracja zakończona",
			        			"Możesz się zalogować",
			        			Notification.TYPE_ERROR_MESSAGE);
			        	notification.setHtmlContentAllowed(true);
			        	notification.setStyleName("tray dark small closable login-help");
			            notification.setPosition(Position.MIDDLE_CENTER);
			            notification.setDelayMsec(5000); //zniknie po 20 sek
			        	notification.show(Page.getCurrent());
			        	
			        	getUI().getNavigator().addView("", new StartView());
			        	getUI().getNavigator().navigateTo("");
			        	
			        }catch(Exception f)
			        {
			        	Notification notification = new Notification("Uwaga");
	 	 		        notification.setDescription("<span>Rejestracja nie powiodła sie. Spróbuj później</span> ");
	 	 		        notification.setHtmlContentAllowed(true);
	 	 		        notification.setStyleName("warning failure small closable login-help");
	 	 		        notification.setPosition(Position.TOP_CENTER);
	 	 		        notification.setDelayMsec(5000); //zniknie po 5 sek
	 	 		        notification.show(Page.getCurrent());
			        	return;
			        }
		        }
            }
        });
        
        return fields;
    }

    private Component buildLabel() 
    {
        CssLayout label = new CssLayout();
        label.addStyleName("label");

        Label welcome = new Label("Rejestracja");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        label.addComponent(welcome);

        return label;
    }
}
