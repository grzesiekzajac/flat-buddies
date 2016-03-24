package com.example.javaee.Views;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.eclipse.persistence.internal.sessions.remote.SequencingFunctionCall.GetNextValue;
import org.openqa.selenium.firefox.internal.NewProfileExtensionConnection;

import pl.smsapi.Client;
import pl.smsapi.api.SmsFactory;
import pl.smsapi.api.action.sms.SMSSend;
import pl.smsapi.api.response.MessageResponse;
import pl.smsapi.api.response.StatusResponse;

import com.example.javaee.JavaeeUI;
import com.example.javaee.Forms.LoginForm;
import com.example.javaee.Models.Flat;
import com.example.javaee.Models.Post;
import com.example.javaee.Models.User;
import com.example.javaee.Models.UserProfile;
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
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
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
public class BillView extends Panel implements View {

    public BillView() 
    {
    	setSizeUndefined();
    	setWidth("100%");
	
		VerticalLayout layout = new VerticalLayout();
		setContent(layout);
		layout.setSizeFull();
		layout.setMargin(true);
		
		
		layout.addComponent(new Header());
		
		layout.addComponent(new Menu(5));
		
		VerticalLayout vl = new BillForm();
		layout.addComponent(vl);
	
		
    }

    private class BillForm extends VerticalLayout {

		public BillForm()
		{
			setSizeFull();
	        Component taskForm = buildTaskForm();
	        addComponent(taskForm);
	        setComponentAlignment(taskForm, Alignment.MIDDLE_CENTER);
		}
		
		private Component buildTaskForm()
		{
			final VerticalLayout taskPanel = new VerticalLayout();
	        taskPanel.setSizeFull();
	        taskPanel.setSpacing(true);
	        Responsive.makeResponsive(taskPanel);
	        taskPanel.addStyleName("login-panel");
	        
	        taskPanel.addComponent(buildButton());
	        taskPanel.addComponent(buildTasks());
	        return taskPanel;
		}
		
		private Component buildTasks() 
		{ 
	        VerticalLayout root =  new VerticalLayout();
	        Panel panel;
	        
	        EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
	        
	        try{
	         	Query q = em.createQuery("SELECT f FROM Flat f WHERE f.flat_name = :uf");
	         	q.setParameter("uf", VaadinSession.getCurrent().getAttribute("flat_name"));
	         	Flat flat = (Flat)q.getSingleResult();
	         	
	         	try{
		         	q = em.createQuery("SELECT u FROM Post u WHERE u.flat = :up AND u.type = :ur ORDER BY u.add_date DESC");
		         	q.setParameter("up", flat);
		         	q.setParameter("ur", "rachunek");
		         	q.setMaxResults(20);
		         	List<Post> posts = (List<Post>)q.getResultList();
		         	
		         	if(posts.isEmpty())
		         	{
		         		Label info = new Label("Nie ma żadnych zadań");
			         	root.addComponent(info);
		         	}
		         	else
		         	{
		         		for(Post post : posts)
		         		{
	         				String tlitle = post.getTitle(); //tytul, ikone i link trzeba zrobic w switchu w zaleznosci od typy posta
	        	        	panel= new Panel(tlitle);
	        	            panel.setIcon(FontAwesome.MONEY); //tytul, ikone i link trzeba zrobic w switchu w zaleznosci od typy posta
	        	            panel.setWidth(40.0f, Unit.PERCENTAGE);        
	        	            root.addComponent(panel);
	        	            root.setComponentAlignment(panel, Alignment.TOP_CENTER);
	        	            
	        	            VerticalLayout content = new VerticalLayout();
	        	            String metadata = post.getAdd_date().toString() + " przez " + post.getUser().getUser_name() + ".\n";
	        	            Label label = new Label(metadata);
	        	            label.addStyleName(ValoTheme.LABEL_TINY);
	        	            content.addComponent(label);
	        	            
	        	            content.addComponent(new Label(post.getText(),ContentMode.HTML));
	        	            
	        	            Button button = new Button("Dokonaj wpłaty");
	        	            button.setIcon(FontAwesome.CREDIT_CARD);
	        	    		button.addClickListener(new Button.ClickListener() {
	        	    			public void buttonClick(ClickEvent event) {
	        	    				getUI().getPage().setLocation("https://sowafinansowa.pl/");
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
	         	}catch(NoResultException e){}
	        }catch(NoResultException e)
	        {
	        	Label info = new Label("Nie znaleziono żadnych mieszkań");
	         	root.addComponent(info);
	        }
	        
	        	        
	        return root;
	    }

	    private Component buildButton() 
	    {
	    	final VerticalLayout root = new VerticalLayout();
	    	final FormLayout formLayout = new FormLayout();
	    	
	    	
	    	Button more = new Button("Dodaj rachunek do rozliczenia");
	        more.setIcon(FontAwesome.PLUS);
			more.addClickListener(new Button.ClickListener() {
				public void buttonClick(ClickEvent event) {
					UI.getCurrent().addWindow(buildWindow());//chooseFlat.getValue().toString()));
				}
			});
			more.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			//more.addStyleName(ValoTheme.BUTTON_TINY);
			Label space = new Label("\n\n");
			root.addComponents(more,space);
			root.setComponentAlignment(more, Alignment.TOP_CENTER);
			
			return root;
	    }
	    
	    private Window buildWindow()
	    {
	    	final Window window = new Window("Utwórz nowe zadanie");
	        addStyleName("profile-window");
	        Responsive.makeResponsive(this);

	        window.setModal(true);
	        window.setCloseShortcut(KeyCode.ESCAPE, null);
	        window.setResizable(false);
	        window.setClosable(true);
	        //window.setWidthUndefined();
	        window.setHeightUndefined();
	        window.setWidth(30.0f, Unit.PERCENTAGE);
	        //window.setHeight(50.0f, Unit.PERCENTAGE);
	        
	        VerticalLayout a = new VerticalLayout();
	      
	        Component content = buildWindowForm(window);
;
	        a.addComponent(content);
	        a.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
	    	
	        window.setContent(a);
	        
	    	return window;
	    }
	    
	    private Component buildWindowForm(Window window)//String flatName) 
	    {
	        FormLayout fields = new FormLayout();
	        fields.setSpacing(true);   
	        
	        EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
	        
	        try{
	        	
	         	Query q = em.createQuery("SELECT f FROM Flat f WHERE f.flat_name = :uf");
	         	q.setParameter("uf", VaadinSession.getCurrent().getAttribute("flat_name"));
	         	Flat flat = (Flat)q.getSingleResult();
	         	
	         	q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :uf");
	         	q.setParameter("uf", VaadinSession.getCurrent().getAttribute("user"));
	         	User user = (User)q.getSingleResult();
	         	
         		q = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.flat = :uf");
		        q.setParameter("uf", flat);
		        Long persons = (Long)q.getSingleResult();
		        
		        Label productsLabel = new Label("Rachunek:");//"<b>Utwórz nowe zadanie: \n</b>", ContentMode.HTML);
		        TextField products = new TextField("");
		        Label sumLabel = new Label("Suma PLN:\n");
		        TextField sum = new TextField("");
		        sum.setNullRepresentation("0.00");
		        fields.addComponents(productsLabel, products, sumLabel, sum);

		        final Button addTask = new Button("Dodaj rachunek");
		        addTask.addStyleName(ValoTheme.BUTTON_SMALL);
		        addTask.setClickShortcut(KeyCode.ENTER);
		        addTask.focus();        
		        
		        fields.addComponent(addTask);
		        fields.setComponentAlignment(addTask, Alignment.BOTTOM_RIGHT);
		        
		        addTask.addClickListener(new Button.ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	String who = VaadinSession.getCurrent().getAttribute("user").toString();
		            	if (String.valueOf(products.getValue()) != "" && !Float.valueOf(sum.getValue()).equals(null) && !Float.valueOf(sum.getValue()).equals(0.00)){
		                	Notification.show("Rachunek za: "+ String.valueOf(products.getValue()) + "</br>Suma:"+ String.valueOf(sum.getValue()) + " PLN" + "</br>Na lokatora:"+ String.valueOf(new Float(sum.getValue())/persons) + " PLN", Type.TRAY_NOTIFICATION);
		                	UI.getCurrent().removeWindow(window);
		                	EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
		        	        
		            		Post post = new Post();
		            		post.setFlat(flat);
		            		post.setType("rachunek");
		            		post.setTitle(products.getValue());
		            		post.setUser(user);
		            		post.setText("Rachunek za: "+ String.valueOf(products.getValue()) + "</br>Suma:"+ String.valueOf(sum.getValue()) + " PLN" + "</br>Na lokatora:"+ String.valueOf(new Float(sum.getValue())/persons) + " PLN");
		            		post.setAdd_date(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		            		
		            		em.getTransaction().begin();
		            		em.persist(post);
		            		em.getTransaction().commit();
		            		
		            		Query q = em.createQuery("SELECT f FROM Flat f WHERE f.flat_name = :uf");
		    	         	q.setParameter("uf", VaadinSession.getCurrent().getAttribute("flat_name"));
		    	         	Flat flat = (Flat)q.getSingleResult();
		    	         	
		    	         	q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :uf");
		    	         	q.setParameter("uf", VaadinSession.getCurrent().getAttribute("user"));
		    	         	User user = (User)q.getSingleResult();
		    	         	
		             		q = em.createQuery("SELECT u FROM User u WHERE u.flat = :uf");
		    		        q.setParameter("uf", flat);
		    		        List<User> flatmates = (List<User>)q.getResultList();
		    		        
		    		        for(User u : flatmates)
		    		        {
		    		        	q = em.createQuery("SELECT u FROM UserProfile u WHERE u.user = :uf");
			    	         	q.setParameter("uf", u);
			    	         	UserProfile userProfile = (UserProfile)q.getSingleResult();
			    	         	if(userProfile.getPhone() != null)
			    	         	{
			    	         		try {

			    	            	    Client client = new Client("grzez.magik@gmail.com");
			    	            	    client.setPasswordHash("baba6b0565a01138b7a63dab17bca5ae");

			    	            	    SmsFactory smsApi = new SmsFactory(client);
			    	            	    SMSSend action = smsApi.actionSend()
			    	            	            .setText("Witaj " + u.getUser_name() + "! Dodano nowy rachunek w portalu mamkwadrat.pl. Tytul:" + post.getTitle() + ". Kwota: " + sum.getValue() + " PLN")
			    	            	            .setTo(userProfile.getPhone())
			    	            	            .setSender("ECO"); //Pole nadawcy lub typ wiadomość 'ECO', '2Way'

			    	            	    StatusResponse result = action.execute();

			    	            	    for(MessageResponse status : result.getList() ) {

			    	            	        System.out.println( status.getNumber() + " " + status.getStatus() );
			    	            	    }
			    	            	} catch( Exception f) {
			    	            	    
			    	            	}
			    	         	}
		    		        }
		            		
		            		UI.getCurrent().removeWindow(window);
			            	UI.getCurrent().getNavigator().addView("bill", new BillView());
			            	UI.getCurrent().getNavigator().navigateTo("bill");
		        	       
		                	} 
	                	else{
		                		Notification.show("Aby dodać zakupy musisz wpisać produkty oraz kwotę.", Type.ERROR_MESSAGE);
		                	}
		            }
		        });
			    
	        
	        }catch(NoResultException e)
	        {
	        	window.close();
	        }
	        
	        
	        return fields;
	    }
    }
    
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
   


	 



}
