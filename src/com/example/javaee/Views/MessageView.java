package com.example.javaee.Views;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.tools.ant.taskdefs.Property;
import org.eclipse.persistence.internal.sessions.remote.SequencingFunctionCall.GetNextValue;
import org.openqa.selenium.firefox.internal.NewProfileExtensionConnection;

import com.example.javaee.JavaeeUI;
import com.example.javaee.Forms.LoginForm;
import com.example.javaee.Models.MessageContent;
import com.example.javaee.Models.MessageHeader;
import com.example.javaee.Models.Notes;
import com.example.javaee.Models.User;
import com.example.javaee.Models.UserProfile;
import com.example.javaee.Views.RateView.SearchRateWindow;
import com.google.gwt.layout.client.Layout;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
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
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;


@SuppressWarnings("serial")
public class MessageView extends Panel implements View {
	
	private Component rightSide;
	
    public MessageView() 
    {
    	setSizeUndefined();
    	setWidth("100%");
	
		VerticalLayout layout = new VerticalLayout();
		setContent(layout);
		layout.setSizeFull();
		layout.setMargin(true);
		
		
		layout.addComponent(new Header());
		
		layout.addComponent(new Menu(6));
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
        hl.setSpacing(true);
        layout.addComponent(hl);
        
        Tree tree = new Tree("Wiadomości");
        tree.setSizeUndefined();
        
    	// Create the tree nodes
    	//tree.addItem("Wiadomości");
    	tree.addItem("Utwórz wiadomość");
    	tree.addItem("Odebrane");
    	tree.addItem("Wysłane");
    	
    	// Set the hierarchy
    	//tree.setParent("Utwórz wiadomość", "Wiadomości");
    	//tree.setParent("Odebrane", "Wiadomości");
    	//tree.setParent("Wysłane", "Wiadomości");

    	// Disallow children for leaves
    	tree.setChildrenAllowed("Utwórz wiadomość", false);
    	tree.setChildrenAllowed("Odebrane", false);
    	tree.setChildrenAllowed("Wysłane", false);
    	
    	tree.setImmediate(true);
    	tree.setNullSelectionAllowed(false);
    	tree.setValue("Odebrane");
    	tree.select("Odebrane");
    	
        hl.addComponent(tree);
        
        VerticalLayout vl = new VerticalLayout();
        vl.setImmediate(true);
        hl.addComponent(vl);
        
        
        rightSide = buildInbox();
        
        vl.addComponent(rightSide);
        vl.setWidth("100%");
        hl.setExpandRatio(tree, 2);
        
        hl.setExpandRatio(vl, 8);
        
        tree.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event == null) return;
				
				if(tree.getValue().equals("Utwórz wiadomość"))
				{
					Component newComp = buildCreateMessage();
					vl.replaceComponent(rightSide, newComp);
					rightSide = newComp;
				}
				else if(tree.getValue().equals("Odebrane"))
				{
					Component newComp = buildInbox();
					vl.replaceComponent(rightSide, newComp);
					rightSide = newComp;
				}
				else if(tree.getValue().equals("Wysłane"))
				{
					Component newComp = buildSentBox();
					vl.replaceComponent(rightSide, newComp);
					rightSide = newComp;
				}
				else
				{
					
				}
				
			}
		});
    	
		
		Label footer = new Label("Stopka - mamkwadrat.pl");
		layout.addComponent(footer);
	
		
    }

    private Component buildCreateMessage()
    {
    	VerticalLayout root = new VerticalLayout();
    	
    	FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);
        details.setSpacing(true);

        Label section = new Label("Nowa wiadomość");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);
        
        TextField toField = new TextField("Do");
        details.addComponent(toField);
        
        TextField subjectField = new TextField("Temat");
        details.addComponent(subjectField);
        
        TextArea messageField = new TextArea("Treść");
        details.addComponent(messageField);
        
        Button but = new Button("Wyślij");
        but.addStyleName(ValoTheme.BUTTON_PRIMARY);
        but.setClickShortcut(KeyCode.ENTER);
        details.addComponent(but);
        
        but.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				String to = toField.getValue();
				String subject = subjectField.getValue();
				String message = messageField.getValue();
				
				EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
		        
		        try{
		     	   	Query q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :un");
		         	q.setParameter("un", to);
		         	User toUsr = (User)q.getSingleResult();
		         	
		         	q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :un");
		         	q.setParameter("un", VaadinSession.getCurrent().getAttribute("user"));
		         	User fromUsr = (User)q.getSingleResult();
		         	
		         	MessageHeader mh = new MessageHeader();
		         	mh.setFrom(fromUsr);
		         	mh.setTo(toUsr);
		         	mh.setSubject(subject);
		         	mh.setDate(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		         	
		         	MessageContent mc = new MessageContent();
		         	mc.setHeader(mh);
		         	mc.setContent(message);
		         	
		         	em.getTransaction().begin();
		         	em.persist(mh);
		         	em.persist(mc);
		         	em.getTransaction().commit();
		         	
		         	Notification notification = new Notification("Sukces");
 	 		        notification.setDescription("<span>Wiadomość wysłana poprawnie</span> ");
 	 		        notification.setHtmlContentAllowed(true);
 	 		        notification.setStyleName("tray dark small closable login-help");
 	 		        notification.setPosition(Position.TOP_CENTER);
 	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
 	 		        notification.show(Page.getCurrent());
 	 		        
 	 		        UI.getCurrent().getNavigator().addView("message", new MessageView());
 	 		        UI.getCurrent().getNavigator().navigateTo("message");
		         	
		         
		         	
		         
		         }catch(NoResultException e){
		        	
		        	Notification notification = new Notification("Bład");
 	 		        notification.setDescription("<span>Nie znaleziono użytkownika <b>" + to + "</b></span> ");
 	 		        notification.setHtmlContentAllowed(true);
 	 		        notification.setStyleName("warning failure small closable login-help");
 	 		        notification.setPosition(Position.TOP_CENTER);
 	 		        notification.setDelayMsec(3000); //zniknie po 3 sek
 	 		        notification.show(Page.getCurrent());
 	 		        
 	 		        toField.focus();
 	 		        toField.clear();
		         }
				
			}
		});
    	
    	return root;
    }
    
    private Component buildInbox()
    {
    	VerticalLayout root = new VerticalLayout();
    	
    	EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
        
        try{
     	   	Query q = em.createQuery("SELECT u FROM User u WHERE u.id = :un");
         	q.setParameter("un", VaadinSession.getCurrent().getAttribute("user_id"));
         	User usr = (User)q.getSingleResult();
         	
         	q = em.createQuery("SELECT u FROM MessageHeader u WHERE u.to = :un ORDER BY u.date DESC");
         	q.setParameter("un", usr);
         	List<MessageHeader> ret = (List<MessageHeader>)q.getResultList();
         	
         	if(ret.isEmpty())
         	{
         		Label a = new Label("Nie ma zadnych wiadomosci");
         		root.addComponent(a);
         	}
         	else
         	{
         		IndexedContainer ic = new IndexedContainer();
         		ic.addContainerProperty("Od", String.class, "");
         		ic.addContainerProperty("Temat", String.class, "");
         		ic.addContainerProperty("Data", String.class, "");
         		ic.addContainerProperty("Id", Long.class, 0);
         		
         		for (MessageHeader messageHeader : ret) {
					Item item = ic.addItem(messageHeader);
					item.getItemProperty("Od").setValue(messageHeader.getFrom().getUser_name());
					item.getItemProperty("Temat").setValue(messageHeader.getSubject());
					item.getItemProperty("Data").setValue(messageHeader.getDate().toString());
					item.getItemProperty("Id").setValue(messageHeader.getId());
					
				}
         		Table table = new Table("Skrzynka odbiorcza");
         		table.setContainerDataSource(ic);
         		table.setPageLength(table.getItemIds().size());
         		table.setWidth("80%");
         		table.setImmediate(true);
         		table.setSelectable(true);
         		table.setNullSelectionAllowed(false);
         		table.setVisibleColumns("Od", "Temat", "Data");
         		
         		table.addItemClickListener(new ItemClickListener() {
					
					@Override
					public void itemClick(ItemClickEvent event) {
						Object row = event.getItemId();
						Long id = (Long) table.getContainerProperty(row, "Id").getValue();
						
						Window w = new MessageWindow(id);
				        UI.getCurrent().addWindow(w);
				        w.focus();
						
					}
				});
         		
         		root.addComponent(table);
         	}
         }catch(NoResultException e){}
    	
    	return root;
    }
    
    private Component buildSentBox()
    {
    	VerticalLayout root = new VerticalLayout();
    	
EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
        
        try{
     	   	Query q = em.createQuery("SELECT u FROM User u WHERE u.id = :un");
         	q.setParameter("un", VaadinSession.getCurrent().getAttribute("user_id"));
         	User usr = (User)q.getSingleResult();
         	
         	q = em.createQuery("SELECT u FROM MessageHeader u WHERE u.from = :un ORDER BY u.date DESC");
         	q.setParameter("un", usr);
         	List<MessageHeader> ret = (List<MessageHeader>)q.getResultList();
         	
         	if(ret.isEmpty())
         	{
         		Label a = new Label("Nie ma zadnych wiadomosci");
         		root.addComponent(a);
         	}
         	else
         	{
         		IndexedContainer ic = new IndexedContainer();
         		ic.addContainerProperty("Do", String.class, "");
         		ic.addContainerProperty("Temat", String.class, "");
         		ic.addContainerProperty("Data", String.class, "");
         		ic.addContainerProperty("Id", Long.class, 0);
         		
         		for (MessageHeader messageHeader : ret) {
         			
					Item item = ic.addItem(messageHeader);
					item.getItemProperty("Do").setValue(messageHeader.getTo().getUser_name());
					item.getItemProperty("Temat").setValue(messageHeader.getSubject());
					item.getItemProperty("Data").setValue(messageHeader.getDate().toString());
					item.getItemProperty("Id").setValue(messageHeader.getId());
					
				}
         		Table table = new Table("Skrzynka nadawcza");
         		table.setContainerDataSource(ic);
         		table.setPageLength(table.getItemIds().size());
         		table.setWidth("80%");
         		table.setImmediate(true);
         		table.setSelectable(true);
         		table.setNullSelectionAllowed(false);
         		table.setVisibleColumns("Do", "Temat", "Data");
         		
         		table.addItemClickListener(new ItemClickListener() {
					
					@Override
					public void itemClick(ItemClickEvent event) {
						Object row = event.getItemId();
						Long id = (Long) table.getContainerProperty(row, "Id").getValue();
						
						Window w = new MessageWindow(id);
				        UI.getCurrent().addWindow(w);
				        w.focus();
						
					}
				});
         		
         		root.addComponent(table);
         	}
         }catch(NoResultException e){}
    	
    	return root;
    }
    
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	class MessageWindow extends Window {

		private BeanFieldGroup<Notes> fieldGroup;
		
	    @PropertyId("note_type")
	    private ComboBox noteTypeField;
	    @PropertyId("describe")
	    private TextArea describeField;
	    
	    //private User sender, reciver;
	    
	    private MessageWindow(Long id) {
	        addStyleName("profile-window");
	        Responsive.makeResponsive(this);

	        setModal(true);
	        setCloseShortcut(KeyCode.ESCAPE, null);
	        setResizable(false);
	        setClosable(true);
	        setHeight(70.0f, Unit.PERCENTAGE);
	        setWidth("60%");

	        VerticalLayout content = new VerticalLayout();
	        //content.setSizeFull();
	        content.setMargin(true);
	        setContent(content);
	        
	        
	        EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
	        
	        try{
	         	Query q = em.createQuery("SELECT u FROM MessageHeader u WHERE u.id = :un");
	         	q.setParameter("un", id);
	         	MessageHeader messageHeader = (MessageHeader)q.getSingleResult();
	         	
	         	q = em.createQuery("SELECT u FROM MessageContent u WHERE u.header = :un");
	         	q.setParameter("un", messageHeader);
	         	MessageContent messageContent = (MessageContent)q.getSingleResult();

	         	Label from = new Label("<b>Od: </b>" + messageHeader.getFrom().getUser_name(), ContentMode.HTML);
	         	content.addComponent(from);
		        
	         	Label to = new Label("<b>Do: </b>" + messageHeader.getTo().getUser_name(), ContentMode.HTML);
	         	content.addComponent(to);
	         	
	         	Label date = new Label("<b>Data: </b>" + messageHeader.getDate().toString(), ContentMode.HTML);
	         	content.addComponent(date);
	         	
	         	Label subject = new Label("<b>Temat: </b>" + messageHeader.getSubject() + "</br></br>", ContentMode.HTML);
	         	content.addComponent(subject);
	         	
	         	Label message = new Label(messageContent.getContent(), ContentMode.HTML);
	         	content.addComponent(message);
	         	
	         	
	        }catch(NoResultException e){};
	        
	       
	     
	    }  
	}
   


	 



}
