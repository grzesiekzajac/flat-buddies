package com.example.javaee.Views;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.eclipse.persistence.internal.sessions.remote.SequencingFunctionCall.GetNextValue;
import org.openqa.selenium.firefox.internal.NewProfileExtensionConnection;

import com.example.javaee.JavaeeUI;
import com.example.javaee.Forms.LoginForm;
import com.example.javaee.Models.Flat;
import com.example.javaee.Models.MessageContent;
import com.example.javaee.Models.MessageHeader;
import com.example.javaee.Models.Post;
import com.example.javaee.Models.User;
import com.google.gwt.layout.client.Layout;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;


@SuppressWarnings("serial")
public class MainView extends Panel implements View {

    public MainView() 
    {
    	setSizeUndefined();
    	setWidth("100%");
	
		VerticalLayout layout = new VerticalLayout();
		setContent(layout);
		layout.setSizeFull();
		layout.setMargin(true);
		
		layout.addComponent(new Header());
		
		layout.addComponent(new Menu(0));
		
		VerticalLayout vl = new MainForm();
		layout.addComponent(vl);
	
		
    }

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	private class MainForm extends VerticalLayout {

		public MainForm()
		{
			setSizeFull();
	        Component mainForm = buildMainForm();
	        addComponent(mainForm);
	        setComponentAlignment(mainForm, Alignment.MIDDLE_CENTER);
		}
		
		private Component buildMainForm()
		{
			final VerticalLayout mainPanel = new VerticalLayout();
	        mainPanel.setSizeFull();
	        mainPanel.setSpacing(true);
	        Responsive.makeResponsive(mainPanel);
	        mainPanel.addStyleName("login-panel");

	        mainPanel.addComponent(buildPanels());
	        //mainPanel.addComponent(buildButton());
	        return mainPanel;
		}
		
		private Component buildPanels() 
		{
			VerticalLayout root =  new VerticalLayout();
	        Panel panel;
	        
			EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
	        
	        try{
	         	Query q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :un");
	         	q.setParameter("un", VaadinSession.getCurrent().getAttribute("user"));
	         	User usr = (User)q.getSingleResult();
	         	
	         	if(usr.getFlat() != null)
	         	{
		         	q = em.createQuery("SELECT f FROM Flat f WHERE f.id = :uf");
		         	q.setParameter("uf", usr.getFlat().getId());
		         	System.out.println(usr.getFlat().getId());
		         	Flat flat = (Flat)q.getSingleResult();
		         	
		         	try{
			         	q = em.createQuery("SELECT u FROM Post u WHERE u.flat = :up ORDER BY u.add_date DESC");
			         	q.setParameter("up", flat);
			         	q.setMaxResults(20);
			         	List<Post> posts = (List<Post>)q.getResultList();
			         	
			         	if(posts.isEmpty())
			         	{
			         		Label info = new Label("Nie ma żadnych postów do wyświetlenia");
				         	root.addComponent(info);
			         	}
			         	else
			         	{
			         		for(Post post : posts)
			         		{
			         			if(post.getType().equals("harmonogram"))
			         			{
			         				String tlitle = "Dodano harmonogram: " + post.getTitle(); //tytul, ikone i link trzeba zrobic w switchu w zaleznosci od typy posta
			        	        	panel= new Panel(tlitle);
			        	            panel.setIcon(FontAwesome.CALENDAR); //tytul, ikone i link trzeba zrobic w switchu w zaleznosci od typy posta
			        	            panel.setWidth(40.0f, Unit.PERCENTAGE);        
			        	            root.addComponent(panel);
			        	            root.setComponentAlignment(panel, Alignment.TOP_CENTER);
			        	            
			        	            VerticalLayout content = new VerticalLayout();
			        	            String metadata = post.getAdd_date().toString() + " przez " + post.getUser().getUser_name() + ".\n";
			        	            Label label = new Label(metadata);
			        	            label.addStyleName(ValoTheme.LABEL_TINY);
			        	            content.addComponent(label);
			        	            
			        	            content.addComponent(new Label(post.getText()));
			        	            
			        	            Button button = new Button("Zobacz wiecej");
			        	            button.setIcon(FontAwesome.EYE);
			        	    		button.addClickListener(new Button.ClickListener() {
			        	    			public void buttonClick(ClickEvent event) {
			        	    				getUI().getNavigator().addView("timetable", new TimetableView());
			        	    				getUI().getNavigator().navigateTo("timetable"); //tutaj view harmonogramow
			        	    			}
			        	    		});
			        	    		button.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			        	    		button.addStyleName(ValoTheme.BUTTON_TINY);
			        	    		content.addComponent(button);
			        	    		
			        	            content.setSizeUndefined(); // Shrink to fit        
			        	            content.setMargin(true);
			        	            panel.setContent(content);
			        	            root.addComponent(new Label("\n"));
			         			}
			         			else if(post.getType().equals("zadanie"))
			         			{
			         				String tlitle = "Dodano zadanie: " + post.getTitle(); //tytul, ikone i link trzeba zrobic w switchu w zaleznosci od typy posta
			        	        	panel= new Panel(tlitle);
			        	            panel.setIcon(FontAwesome.CHECK_SQUARE); //tytul, ikone i link trzeba zrobic w switchu w zaleznosci od typy posta
			        	            panel.setWidth(40.0f, Unit.PERCENTAGE);        
			        	            root.addComponent(panel);
			        	            root.setComponentAlignment(panel, Alignment.TOP_CENTER);
			        	            
			        	            VerticalLayout content = new VerticalLayout();
			        	            String metadata = post.getAdd_date().toString() + " przez " + post.getUser().getUser_name() + ".\n";
			        	            Label label = new Label(metadata);
			        	            label.addStyleName(ValoTheme.LABEL_TINY);
			        	            content.addComponent(label);
			        	            
			        	            content.addComponent(new Label(post.getText(), ContentMode.HTML));
			        	            
			        	            Button button = new Button("Zobacz wiecej");
			        	            button.setIcon(FontAwesome.EYE);
			        	    		button.addClickListener(new Button.ClickListener() {
			        	    			public void buttonClick(ClickEvent event) {
			        	    				getUI().getNavigator().addView("task", new TaskView());
			        	    				getUI().getNavigator().navigateTo("task"); //tutaj view harmonogramow
			        	    			}
			        	    		});
			        	    		button.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			        	    		button.addStyleName(ValoTheme.BUTTON_TINY);
			        	    		content.addComponent(button);
			        	    		
			        	            content.setSizeUndefined(); // Shrink to fit        
			        	            content.setMargin(true);
			        	            panel.setContent(content);
			        	            root.addComponent(new Label("\n"));
			         			}
			         			else if(post.getType().equals("zakupy"))
			         			{
			         				String tlitle = "Dodano zakupy: " + post.getTitle(); //tytul, ikone i link trzeba zrobic w switchu w zaleznosci od typy posta
			        	        	panel= new Panel(tlitle);
			        	            panel.setIcon(FontAwesome.SHOPPING_CART); //tytul, ikone i link trzeba zrobic w switchu w zaleznosci od typy posta
			        	            panel.setWidth(40.0f, Unit.PERCENTAGE);        
			        	            root.addComponent(panel);
			        	            root.setComponentAlignment(panel, Alignment.TOP_CENTER);
			        	            
			        	            VerticalLayout content = new VerticalLayout();
			        	            String metadata = post.getAdd_date().toString() + " przez " + post.getUser().getUser_name() + ".\n";
			        	            Label label = new Label(metadata);
			        	            label.addStyleName(ValoTheme.LABEL_TINY);
			        	            content.addComponent(label);
			        	            
			        	            content.addComponent(new Label(post.getText(), ContentMode.HTML));
			        	            
			        	            Button button = new Button("Zobacz wiecej");
			        	            button.setIcon(FontAwesome.EYE);
			        	    		button.addClickListener(new Button.ClickListener() {
			        	    			public void buttonClick(ClickEvent event) {
			        	    				getUI().getNavigator().addView("shopping", new ShoppingView());
			        	    				getUI().getNavigator().navigateTo("shopping"); //tutaj view harmonogramow
			        	    			}
			        	    		});
			        	    		button.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			        	    		button.addStyleName(ValoTheme.BUTTON_TINY);
			        	    		content.addComponent(button);
			        	    		
			        	            content.setSizeUndefined(); // Shrink to fit        
			        	            content.setMargin(true);
			        	            panel.setContent(content);
			        	            root.addComponent(new Label("\n"));
			         			}
			         			else if(post.getType().equals("rachunek"))
			         			{
			         				String tlitle = "Dodano rachunek: " + post.getTitle(); //tytul, ikone i link trzeba zrobic w switchu w zaleznosci od typy posta
			        	        	panel= new Panel(tlitle);
			        	            panel.setIcon(FontAwesome.CREDIT_CARD); //tytul, ikone i link trzeba zrobic w switchu w zaleznosci od typy posta
			        	            panel.setWidth(40.0f, Unit.PERCENTAGE);        
			        	            root.addComponent(panel);
			        	            root.setComponentAlignment(panel, Alignment.TOP_CENTER);
			        	            
			        	            VerticalLayout content = new VerticalLayout();
			        	            String metadata = post.getAdd_date().toString() + " przez " + post.getUser().getUser_name() + ".\n";
			        	            Label label = new Label(metadata);
			        	            label.addStyleName(ValoTheme.LABEL_TINY);
			        	            content.addComponent(label);
			        	            
			        	            content.addComponent(new Label(post.getText(), ContentMode.HTML));
			        	            
			        	            Button button = new Button("Zobacz wiecej");
			        	            button.setIcon(FontAwesome.EYE);
			        	    		button.addClickListener(new Button.ClickListener() {
			        	    			public void buttonClick(ClickEvent event) {
			        	    				getUI().getNavigator().addView("bill", new BillView());
			        	    				getUI().getNavigator().navigateTo("bill"); //tutaj view harmonogramow
			        	    			}
			        	    		});
			        	    		button.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			        	    		button.addStyleName(ValoTheme.BUTTON_TINY);
			        	    		content.addComponent(button);
			        	    		
			        	            content.setSizeUndefined(); // Shrink to fit        
			        	            content.setMargin(true);
			        	            panel.setContent(content);
			        	            root.addComponent(new Label("\n"));
			         			
			         			}
			         		}
			         		
			         		root.addComponent(buildButton());
			         	}
			         	
			         	
		         	}catch(NoResultException e){}
	         		}
	         		else
	         		{
	         			Label info = new Label("Nie znaleziono mieszkań");
			         	root.addComponent(info);
	         		}
		         }catch(NoResultException e){}
	        
	        return root;
	    }

	    private Component buildButton() 
	    {
	    	final VerticalLayout root = new VerticalLayout();
	    	
	    	Button more = new Button("Załaduj więcej");
	        more.setIcon(FontAwesome.PLUS);
			more.addClickListener(new Button.ClickListener() {
				public void buttonClick(ClickEvent event) {
					//tutaj metoda, ktora pobiera starsze posty
				}
			});
			more.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			more.addStyleName(ValoTheme.BUTTON_TINY);
			root.addComponent(more);
			root.setComponentAlignment(more, Alignment.TOP_CENTER);
			
			return root;
	    }
	}
   


	 



}
