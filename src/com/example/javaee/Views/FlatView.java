package com.example.javaee.Views;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.eclipse.persistence.internal.sessions.remote.SequencingFunctionCall.GetNextValue;
import org.openqa.selenium.firefox.internal.NewProfileExtensionConnection;
import org.vaadin.dialogs.ConfirmDialog;

import com.example.javaee.JavaeeUI;
import com.example.javaee.Forms.LoginForm;
import com.example.javaee.Models.Flat;
import com.example.javaee.Models.User;
import com.google.gwt.layout.client.Layout;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;


@SuppressWarnings("serial")
public class FlatView extends Panel implements View {

    public FlatView() 
    {
    	setSizeUndefined();
    	setWidth("100%");
	
		VerticalLayout layout = new VerticalLayout();
		setContent(layout);
		layout.setSizeFull(); 
		layout.setMargin(true);
		
		
		layout.addComponent(new Header());
		
		layout.addComponent(new Menu(1));
		
		layout.addComponent(buildFlatForm());
		
    }
    
    private Component buildFlatForm()
	{
		TabSheet tabSheet = new TabSheet();
        tabSheet.setHeight(60.0f, Unit.PERCENTAGE);
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
 		
		FormLayout flatTabJoin = new FormLayout();
		FormLayout flatTabCreate = new FormLayout();
		FormLayout flatTabLeave = new FormLayout();
				
        flatTabJoin.setSizeFull();
        flatTabJoin.setSpacing(true);
        Responsive.makeResponsive(flatTabJoin);
        flatTabJoin.addStyleName("login-panel");
        
        flatTabCreate.setSizeUndefined();
        flatTabCreate.setSpacing(true);
        Responsive.makeResponsive(flatTabCreate);
        flatTabCreate.addStyleName("login-panel");
        
        flatTabLeave.setSizeUndefined();
        flatTabLeave.setSpacing(true);
        Responsive.makeResponsive(flatTabLeave);
        flatTabLeave.addStyleName("login-panel");        
        
        flatTabJoin.addComponent(buildLabelJoin());
        Component a = buildFieldsJoin();
        flatTabJoin.addComponent(a);
        flatTabJoin.setComponentAlignment(a, Alignment.MIDDLE_CENTER);
        
        flatTabCreate.addComponent(buildLabelCreate());
        flatTabCreate.addComponent(buildFieldsCreate());
        flatTabLeave.addComponent(buildLabelLeave());
        flatTabLeave.addComponent(buildFieldsLeave());
        
        //tabSheet.setComponentAlignment(flatTabJoin, Alignment.MIDDLE_CENTER);
        //tabSheet.setComponentAlignment(flatTabCreate, Alignment.MIDDLE_CENTER);
        
        tabSheet.addTab(flatTabJoin, "Dołącz", FontAwesome.CHILD);
        tabSheet.addTab(flatTabCreate, "Utwórz", FontAwesome.PLUS);
        tabSheet.addTab(flatTabLeave, "Opuść", FontAwesome.SIGN_OUT);
        
        return tabSheet;
	}
	
	private Component buildFieldsJoin() 
	{
        FormLayout fields = new FormLayout();
        fields.setSpacing(true);

        final TextField username = new TextField("Nazwa");
        username.setIcon(FontAwesome.HOME);
        //username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField password = new PasswordField("Hasło");
        password.setIcon(FontAwesome.LOCK);
        //password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button join = new Button("Dołącz");
        join.addStyleName(ValoTheme.BUTTON_PRIMARY);
        join.setClickShortcut(KeyCode.ENTER);
        join.focus();
        
        fields.addComponents(username, password, join);
        fields.setComponentAlignment(join, Alignment.BOTTOM_CENTER);

        join.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	
            	if(username.isEmpty())
            	{
            		Notification notification = new Notification("Uwaga");
 	 		        notification.setDescription("<span>Uzupełnij wymagane pola</span> ");
 	 		        notification.setHtmlContentAllowed(true);
 	 		        notification.setStyleName("warning failure small closable login-help");
 	 		        notification.setPosition(Position.TOP_CENTER);
 	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
 	 		        notification.show(Page.getCurrent());
 	 		        
 	 		        username.focus();
 	 		        return;
            	}
            	
            	if(password.isEmpty())
            	{
            		Notification notification = new Notification("Uwaga");
 	 		        notification.setDescription("<span>Uzupełnij wymagane pola</span> ");
 	 		        notification.setHtmlContentAllowed(true);
 	 		        notification.setStyleName("warning failure small closable login-help");
 	 		        notification.setPosition(Position.TOP_CENTER);
 	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
 	 		        notification.show(Page.getCurrent());
 	 		        
 	 		        password.focus();
            	}
            	
            	
            	EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
    	        
    	        try{
    	         	Query q = em.createQuery("SELECT u FROM Flat u WHERE u.flat_name = :un");
    	         	q.setParameter("un", username.getValue());
    	         	Flat flat = (Flat)q.getSingleResult();
    	         	
    	         	if(password.getValue().equals(flat.getFlat_password()))
    	         	{
    	         		q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :un");
        	         	q.setParameter("un", VaadinSession.getCurrent().getAttribute("user"));
        	         	User user = (User)q.getSingleResult();
        	         	
        	         	Notification notification = new Notification("Sukces");
     	 		        notification.setDescription("<span>Dodano do mieszkania poprawnie</span> ");
     	 		        notification.setHtmlContentAllowed(true);
     	 		        notification.setStyleName("tray dark small closable login-help");
     	 		        notification.setPosition(Position.TOP_CENTER);
     	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
     	 		        notification.show(Page.getCurrent());
        	         	
        	         	em.getTransaction().begin();
        	         	user.setFlat(flat);
        	         	em.getTransaction().commit();
        	         	
        	         	VaadinSession.getCurrent().setAttribute("flat_name", username.getValue());
    	         	}
    	         	else
    	         	{
    	         		Notification notification = new Notification("Bład");
     	 		        notification.setDescription("<span>Błędne hasło dla mieszkania <b>" + username.getValue() + "</b></span> ");
     	 		        notification.setHtmlContentAllowed(true);
     	 		        notification.setStyleName("warning failure small closable login-help");
     	 		        notification.setPosition(Position.TOP_CENTER);
     	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
     	 		        notification.show(Page.getCurrent());
    	         	}
    	         	
    	        }
    	        catch(NoResultException e)
    	        {
    	        	Notification notification = new Notification("Bład");
 	 		        notification.setDescription("<span>Nie znaleziono mieszkania <b>" + username.getValue() + "</b></span> ");
 	 		        notification.setHtmlContentAllowed(true);
 	 		        notification.setStyleName("warning failure small closable login-help");
 	 		        notification.setPosition(Position.TOP_CENTER);
 	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
 	 		        notification.show(Page.getCurrent());
    	        }
            }});
        
        return fields;
    }

    private Component buildLabelJoin() 
    {
        CssLayout label = new CssLayout();
        label.addStyleName("label");

        Label join = new Label("Dołącz do mieszkania");
        join.setSizeUndefined();
        join.addStyleName(ValoTheme.LABEL_H4);
        join.addStyleName(ValoTheme.LABEL_COLORED);
        label.addComponent(join);

        return label;
    }
    
    private Component buildFieldsCreate() 
	{
        FormLayout fields = new FormLayout();
        fields.setSpacing(true);

        final TextField username = new TextField("Nazwa");
        username.setIcon(FontAwesome.HOME);
        //username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField password = new PasswordField("Hasło");
        password.setIcon(FontAwesome.LOCK);
        //password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        
        final PasswordField password2 = new PasswordField("Powtórz hasło");
        password2.setIcon(FontAwesome.LOCK);

        final Button create = new Button("Utwórz mieszkanie");
        create.addStyleName(ValoTheme.BUTTON_PRIMARY);
        create.setClickShortcut(KeyCode.ENTER);
        create.focus();
        
        fields.addComponents(username, password, password2, create);
        fields.setComponentAlignment(create, Alignment.BOTTOM_CENTER);

        create.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	
            	if(username.isEmpty())
            	{
            		Notification notification = new Notification("Uwaga");
 	 		        notification.setDescription("<span>Uzupełnij wymagane pola</span> ");
 	 		        notification.setHtmlContentAllowed(true);
 	 		        notification.setStyleName("warning failure small closable login-help");
 	 		        notification.setPosition(Position.TOP_CENTER);
 	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
 	 		        notification.show(Page.getCurrent());
 	 		        
 	 		        username.focus();
 	 		        return;
            	}
            	
            	if(password.isEmpty())
            	{
            		Notification notification = new Notification("Uwaga");
 	 		        notification.setDescription("<span>Uzupełnij wymagane pola</span> ");
 	 		        notification.setHtmlContentAllowed(true);
 	 		        notification.setStyleName("warning failure small closable login-help");
 	 		        notification.setPosition(Position.TOP_CENTER);
 	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
 	 		        notification.show(Page.getCurrent());
 	 		        
 	 		        password.focus();
 	 		        return;
            	}
            	
            	if(password2.isEmpty())
            	{
            		Notification notification = new Notification("Uwaga");
 	 		        notification.setDescription("<span>Uzupełnij wymagane pola</span> ");
 	 		        notification.setHtmlContentAllowed(true);
 	 		        notification.setStyleName("warning failure small closable login-help");
 	 		        notification.setPosition(Position.TOP_CENTER);
 	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
 	 		        notification.show(Page.getCurrent());
 	 		        
 	 		        password2.focus();
 	 		        return;
            	}
            	
            	EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
    	        
    	        try{
    	         	Query q = em.createQuery("SELECT u FROM Flat u WHERE u.flat_name = :un");
    	         	q.setParameter("un", username.getValue());
    	         	Flat flat = (Flat)q.getSingleResult();
    	         	
    	         	Notification notification = new Notification("Bład");
 	 		        notification.setDescription("<span>Istnieje już mieszkanie <b>" + username.getValue() + "</b></span> ");
 	 		        notification.setHtmlContentAllowed(true);
 	 		        notification.setStyleName("warning failure small closable login-help");
 	 		        notification.setPosition(Position.TOP_CENTER);
 	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
 	 		        notification.show(Page.getCurrent());
    	         	
    	         	
    	        }
    	        catch(NoResultException e)
    	        {
    	        	if(password.getValue().equals(password2.getValue()))
    	         	{
    	         		Flat flat = new Flat();
    	         		flat.setFlat_name(username.getValue());
    	         		flat.setFlat_password(password.getValue());
        	         	
    	         		em.getTransaction().begin();
        	         	em.persist(flat);
        	         	em.getTransaction().commit();
        	         	
        	         	
        	         	Notification notification = new Notification("Sukces");
     	 		        notification.setDescription("<span>Dodano nowe mieszkanie</span> ");
     	 		        notification.setHtmlContentAllowed(true);
     	 		        notification.setStyleName("tray dark small closable login-help");
     	 		        notification.setPosition(Position.TOP_CENTER);
     	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
     	 		        notification.show(Page.getCurrent());
     	 		        
     	 		      Query q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :un");
     	 		      q.setParameter("un", VaadinSession.getCurrent().getAttribute("user"));
     	 		      User user = (User)q.getSingleResult();
      	         	
	      	         	em.getTransaction().begin();
	      	         	user.setFlat(flat);
	      	         	em.getTransaction().commit();

        	         	VaadinSession.getCurrent().setAttribute("flat_name", username.getValue());
    	         	}
    	         	else
    	         	{
    	         		Notification notification = new Notification("Bład");
     	 		        notification.setDescription("<span>Podane hasła różnią się</span> ");
     	 		        notification.setHtmlContentAllowed(true);
     	 		        notification.setStyleName("warning failure small closable login-help");
     	 		        notification.setPosition(Position.TOP_CENTER);
     	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
     	 		        notification.show(Page.getCurrent());
     	 		        
     	 		        password.focus();
    	         	}
    	        	
    	        	
    	        }
            }
        });
        
        return fields;
    }

    private Component buildLabelCreate() 
    {
        CssLayout label = new CssLayout();
        label.addStyleName("label");

        Label join = new Label("Utwórz nowe mieszkanie");
        join.setSizeUndefined();
        join.addStyleName(ValoTheme.LABEL_H4);
        join.addStyleName(ValoTheme.LABEL_COLORED);
        label.addComponent(join);

        return label;
    }
    
    private Component buildFieldsLeave() 
	{
        FormLayout fields = new FormLayout();
        fields.setSpacing(true);

        final Label a = new Label("Opuszczenie mieszkania sprawi, ze nie bedziesz przypisany do żadnego mieszkania!");     
        final Button leave = new Button("Opuść mieszkanie");
        
        fields.addComponent(a);
        fields.addComponent(leave);
        leave.addStyleName(ValoTheme.BUTTON_PRIMARY);
        leave.setClickShortcut(KeyCode.ENTER);
        leave.focus();

        leave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
    	        
    	        try{
    	        Query q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :un");
   	 		      q.setParameter("un", VaadinSession.getCurrent().getAttribute("user"));
   	 		      User user = (User)q.getSingleResult();
    	        
   	 		      if(user.getFlat() == null)
   	 		      {
   	 		    	Notification notification = new Notification("Bład");
 	 		        notification.setDescription("<span>Nie masz aktualnie żadnego mieszkania</span> ");
 	 		        notification.setHtmlContentAllowed(true);
 	 		        notification.setStyleName("warning failure small closable login-help");
 	 		        notification.setPosition(Position.TOP_CENTER);
 	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
 	 		        notification.show(Page.getCurrent());
 	 		        
 	 		        return;
   	 		      }
   	 		      
   	 		    em.getTransaction().begin();
	         	user.setFlat(null);
	         	em.getTransaction().commit();
	         	
	         	Notification notification = new Notification("Sukces");
	 		        notification.setDescription("<span>Opuszczono obecne mieszkanie</span> ");
	 		        notification.setHtmlContentAllowed(true);
	 		        notification.setStyleName("tray dark small closable login-help");
	 		        notification.setPosition(Position.TOP_CENTER);
	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
	 		        notification.show(Page.getCurrent());
    	        
	         	VaadinSession.getCurrent().setAttribute("flat_name", null);
    	        }
    	        catch(NoResultException e)
    	        {}
            }
        });
        
        return fields;
    }

    private Component buildLabelLeave() 
    {
        CssLayout label = new CssLayout();
        label.addStyleName("label");

        Label join = new Label("Opuść mieszkanie");
        join.setSizeUndefined();
        join.addStyleName(ValoTheme.LABEL_H4);
        join.addStyleName(ValoTheme.LABEL_COLORED);
        label.addComponent(join);

        return label;
    }

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
   


	 



}
