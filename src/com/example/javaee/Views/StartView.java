package com.example.javaee.Views;

import com.example.javaee.Forms.LoginForm;
import com.example.javaee.Forms.RegisterForm;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class StartView extends VerticalLayout implements View {

	public StartView()
	{
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		addComponent(new Header());
		
		TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        addComponent(detailsWrapper);
        setExpandRatio(detailsWrapper, 1f);
        
        detailsWrapper.addComponent(buildInfoTab());
        detailsWrapper.addComponent(buildLoginTab());
        detailsWrapper.addComponent(buildRegisterTab());
        detailsWrapper.addComponent(buildContactTab());
        
		//showWelcomeNotification();
		
	}
	
	private Component buildInfoTab() {
        VerticalLayout root = new VerticalLayout();
        root.setCaption("Info");
        root.setIcon(FontAwesome.INFO);
        root.setSpacing(true);
        root.setMargin(true);
        root.setSizeFull();
        
        root.addComponent(new Label("W budowie ;-)"));

        return root;
    }
	
	private Component buildLoginTab() {
        VerticalLayout root = new VerticalLayout();
        root.setCaption("Logowanie");
        root.setIcon(FontAwesome.USER);
        root.setSpacing(true);
        root.setMargin(true);
        root.setSizeFull();

        LoginForm loginForm = new LoginForm();
        root.addComponent(loginForm);
        root.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

        return root;
    }
	
	private Component buildRegisterTab() {
        VerticalLayout root = new VerticalLayout();
        root.setCaption("Rejestracja");
        root.setIcon(FontAwesome.PLUS);
        root.setSpacing(true);
        root.setMargin(true);
        root.setSizeFull();

        RegisterForm registerForm = new RegisterForm();
        root.addComponent(registerForm);
        root.setComponentAlignment(registerForm, Alignment.MIDDLE_CENTER);

        return root;
    }
	
	private Component buildContactTab() {
        VerticalLayout root = new VerticalLayout();
        root.setCaption("Kontakt");
        root.setIcon(FontAwesome.ENVELOPE);
        root.setSpacing(true);
        root.setMargin(true);
        root.setSizeFull();
        
        root.addComponent(new Label("W budowie ;-)"));


        return root;
    }
	
	
	private void showWelcomeNotification()
	{
		Notification notification = new Notification("Witaj na stronie mamkwardat.pl");
        notification.setDescription("<span>Aby rozpocząć pracę <b>zaloguj się</b> lub jeśli nie masz konta <b>zarejestruj się</b>. To nic nie kosztuje.</span> ");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(20000); //zniknie po 20 sek
        notification.show(Page.getCurrent());
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
