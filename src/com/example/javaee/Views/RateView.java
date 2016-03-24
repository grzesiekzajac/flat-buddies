package com.example.javaee.Views;

import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.rmi.CORBA.UtilDelegate;

import org.apache.commons.collections.map.HashedMap;
import org.apache.http.client.utils.DateUtils;
import org.eclipse.persistence.internal.sessions.remote.SequencingFunctionCall.GetNextValue;
import org.openqa.selenium.firefox.internal.NewProfileExtensionConnection;

import com.example.javaee.JavaeeUI;
import com.example.javaee.Forms.LoginForm;
import com.example.javaee.Models.MessageHeader;
import com.example.javaee.Models.Notes;
import com.example.javaee.Models.User;
import com.example.javaee.Models.UserProfile;
import com.example.javaee.Views.MessageView.MessageWindow;
import com.google.gwt.layout.client.Layout;
import com.invient.vaadin.charts.InvientCharts;
import com.invient.vaadin.charts.InvientCharts.DecimalPoint;
import com.invient.vaadin.charts.InvientCharts.XYSeries;
import com.invient.vaadin.charts.InvientChartsConfig;
import com.invient.vaadin.charts.InvientCharts.SeriesType;
import com.invient.vaadin.charts.InvientChartsConfig.PieConfig;
import com.invient.vaadin.charts.InvientChartsConfig.PieDataLabel;
import com.invient.vaadin.charts.InvientChartsConfig.PointConfig;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.client.ui.calendar.schedule.DateUtil;
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
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;




@SuppressWarnings("serial")
public class RateView extends Panel implements View {
	
	private Tree searchResult;
	private Component stats;
	
    public RateView() 
    {
    	setSizeUndefined();
    	setWidth("100%");
	
		VerticalLayout layout = new VerticalLayout();
		setContent(layout);
		layout.setSizeFull();
		layout.setMargin(true);
		
		layout.addComponent(new Header());
		
		layout.addComponent(new Menu(7));
		
		VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setWidth("80%");
        content.setSpacing(true);
        layout.addComponent(content);
        layout.setComponentAlignment(content, Alignment.TOP_CENTER);
        
        Label titleTab = new Label("Dodaj ocenę");
        titleTab.addStyleName(ValoTheme.LABEL_H4);
        titleTab.addStyleName(ValoTheme.LABEL_COLORED);
        content.addComponent(titleTab);
        
        HorizontalLayout searchPanel = new HorizontalLayout();
        searchPanel.setSizeUndefined();
        searchPanel.setSpacing(true);
        content.addComponent(searchPanel);
        
        Label searchLabel = new Label("Szukaj uzytkownika: ");
        searchPanel.addComponent(searchLabel);
        
        TextField searchField = new TextField();
        searchPanel.addComponent(searchField);
        
        searchResult = new Tree();
        content.addComponent(searchResult);
        searchResult.setVisible(false);
        
        stats = buildStats();
        content.addComponent(stats);
        
        searchField.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if(searchField.getValue().isEmpty()) return;
				
				EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
		        
		        try{
		     	   	Query q = em.createQuery("SELECT u FROM User u WHERE u.user_name LIKE :un");
		         	q.setParameter("un", "%"+searchField.getValue()+"%");
		         	List<User> users = q.getResultList();
		         	
		         	Tree tempTree;
		         	
		         	if(users.isEmpty())
		         	{
		         		tempTree = new Tree();
		         		tempTree.addItem("Nie znalezino wyników dla: " + searchField.getValue());
		         		tempTree.setParent(null, "Nie znalezino wyników dla: " + searchField.getValue());
		         		tempTree.setImmediate(true);
			        	tempTree.setNullSelectionAllowed(false);
			        	tempTree.setValue("Nie znalezino wyników dla: " + searchField.getValue());
		         		content.replaceComponent(searchResult, tempTree);
		         		searchResult = tempTree;
		         	}
		         	else
		         	{
		         		tempTree = new Tree();
		         		tempTree.addItem("Wyniki wyszukiwania dla: " + searchField.getValue());
		         		tempTree.setImmediate(true);
			        	tempTree.setNullSelectionAllowed(false);
			        	tempTree.setValue("Wyniki wyszukiwania dla: " + searchField.getValue());
			        	
			        	tempTree.addValueChangeListener(new ValueChangeListener() {
							
							@Override
							public void valueChange(ValueChangeEvent event) {
								
								if(tempTree.hasChildren(tempTree.getValue()))
								{
									return;
								}
								else
								{
									Window w = new SearchRateWindow(tempTree.getValue().toString());
							        UI.getCurrent().addWindow(w);
							        w.focus();
							        
							        tempTree.setValue("Wyniki wyszukiwania dla: " + searchField.getValue());
							        
							        w.addCloseListener(new CloseListener() {
										
										@Override
										public void windowClose(CloseEvent e) {
											Component temp = buildStats();
							         		content.replaceComponent(stats, temp);
							         		stats = temp;	
										}
									});
							        
							        
								}
								
							}
						});
			        	
		         		for(User user : users )
		         		{
		         			tempTree.addItem(user.getUser_name());
		         	    	tempTree.setChildrenAllowed(user.getUser_name(), false);
		         	    	tempTree.setParent(user.getUser_name(), "Wyniki wyszukiwania dla: " + searchField.getValue());
		         		}
		         		
		         		content.replaceComponent(searchResult, tempTree);
		         		searchResult = tempTree;	
		         	}
				
		        }
		        catch(NoResultException e){}
			}
		});

		Label footer = new Label("Stopka - mamkwadrat.pl");
		layout.addComponent(footer);	
    }
    
    private Component buildStats()
    {
    	VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setWidth("80%");
        content.setSpacing(true);
        
        Label titleTab = new Label("Moje statystyki");
        titleTab.addStyleName(ValoTheme.LABEL_H4);
        titleTab.addStyleName(ValoTheme.LABEL_COLORED);
        content.addComponent(titleTab);
        
        EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
        
        try{
     	   	Query q = em.createQuery("SELECT u FROM User u WHERE u.id = :un");
         	q.setParameter("un", VaadinSession.getCurrent().getAttribute("user_id"));
         	User usr = (User)q.getSingleResult();
         	
         	q = em.createQuery("SELECT u FROM Notes u WHERE u.reciver = :un ORDER BY u.date DESC");
         	q.setParameter("un", usr);
         	List<Notes> ret = (List<Notes>)q.getResultList();
         	
         	if(ret.isEmpty())
         	{
         		ProgressBar pb = new ProgressBar();
         		pb.setValue(new Float(0));
         		Label opis = new Label("0 % pozytywnych opini");
         		content.addComponent(opis);
         		content.addComponent(pb);
         	}
         	else
         	{
         		int positive = 0;
         		int negative = 0;
         		int stop = ret.size();
         		
         		if(stop > 5) stop = 5;
         		
         		for(Notes note : ret)
         		{
         			if(note.getNote_type().equals("Pozytywny")) positive++;
         			else negative++;
         		}
         		
         		ProgressBar pb = new ProgressBar();
         		Float result = (float) (((float)positive)/((float)(positive+negative)));
         		pb.setValue(result);
         		pb.setImmediate(true);
         		pb.setSizeFull();
         		Label opis = new Label(result*100 + " % pozytywnych z " + (positive+negative) + "opini");
         		content.addComponent(opis);
         		content.addComponent(pb);
         		
         		IndexedContainer ic = new IndexedContainer();
         		ic.addContainerProperty("Od", String.class, "");
         		ic.addContainerProperty("Ocena", String.class, "");
         		ic.addContainerProperty("Opis", String.class, "");
         		
         		for (int i = 0; i < stop; i++) {
         			Notes note = ret.get(i);
					Item item = ic.addItem(note);
					item.getItemProperty("Od").setValue(note.getSender().getUser_name());
					item.getItemProperty("Ocena").setValue(note.getNote_type());
					item.getItemProperty("Opis").setValue(note.getDescrbe());
					
				}
         		Table table = new Table("Ostatnie opinie");
         		table.setContainerDataSource(ic);
         		table.setPageLength(table.getItemIds().size());
         		table.setWidth("80%");
         		table.setImmediate(true);
         		content.addComponent(table);	
         	}      	
         }catch(NoResultException e){}
        
        return content;
    }
    
    @Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	class SearchRateWindow extends Window {

		private BeanFieldGroup<Notes> fieldGroup;
		
	    @PropertyId("note_type")
	    private ComboBox noteTypeField;
	    @PropertyId("descrbe")
	    private TextArea describeField;
	    
	    private Window window;
	    
	    private SearchRateWindow(String value) {
	    	window=this;
	        addStyleName("profile-window");
	        Responsive.makeResponsive(this);

	        setModal(true);
	        setCloseShortcut(KeyCode.ESCAPE, null);
	        setResizable(false);
	        setClosable(true);
	        setHeight(50.0f, Unit.PERCENTAGE);
	        setWidth("30%");

	        VerticalLayout content = new VerticalLayout();
	        content.setSizeFull();
	        content.setMargin(true);
	        setContent(content);
	        
	        FormLayout details = new FormLayout();
	        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
	        content.addComponent(details);
	        content.setExpandRatio(details, 1);
	        details.setSpacing(true);

	        Label section = new Label("Ocenianie użytkownika: " + value);
	        section.addStyleName(ValoTheme.LABEL_H4);
	        section.addStyleName(ValoTheme.LABEL_COLORED);
	        details.addComponent(section);
	        
	        noteTypeField = new ComboBox("Ocena");
	        noteTypeField.addItem("Pozytywny");
	        noteTypeField.addItem("Negatywny");
	        details.addComponent(noteTypeField);
	        
	        describeField = new TextArea("Ocena opisowa");
	        details.addComponent(describeField);
	        
	        Button btn = new Button("Dodaj");
	        details.addComponent(btn);
	        
	        EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
	        
	        try{
	         	Query q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :un");
	         	q.setParameter("un", value);
	         	User reciver = (User)q.getSingleResult();
	         	
	         	q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :un");
	         	q.setParameter("un", VaadinSession.getCurrent().getAttribute("user"));
	         	User sender = (User)q.getSingleResult();

	         	q = em.createQuery("SELECT u FROM Notes u WHERE u.sender = :se AND u.reciver = :re");
	         	q.setParameter("se", sender);
	         	q.setParameter("re", reciver);
	         	Notes notes = (Notes)q.getSingleResult();
	         	
	         	fieldGroup = new BeanFieldGroup<Notes>(Notes.class);
		        fieldGroup.bindMemberFields(this);
		        fieldGroup.setItemDataSource(notes);
		        
		        btn.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						
						if(noteTypeField.isEmpty())
						{
							noteTypeField.focus();
							Notification notification = new Notification("Pomoc");
			 		        notification.setDescription("<span>Dodaj ocenę</span> ");
			 		        notification.setHtmlContentAllowed(true);
			 		        notification.setStyleName("tray dark small closable login-help");
			 		        notification.setPosition(Position.TOP_CENTER);
			 		        notification.setDelayMsec(3000); //zniknie po 3 sek
			 		        notification.show(Page.getCurrent());
						}
						else
						{
							//EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
					        
					        try{
					        	Query q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :un");
					         	q.setParameter("un", value);
					         	User reciver = (User)q.getSingleResult();
					         	
					         	q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :un");
					         	q.setParameter("un", VaadinSession.getCurrent().getAttribute("user"));
					         	User sender = (User)q.getSingleResult();

					         	em.getTransaction().begin();
					         	notes.setReciver(reciver);
					         	notes.setSender(sender);
					         	notes.setDescrbe(describeField.getValue());
					         	notes.setDate(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
					         	notes.setNote_type(noteTypeField.getValue().toString());
					         	em.getTransaction().commit();
					         	
					         	UI.getCurrent().removeWindow(window);
					         	
					         	Notification notification = new Notification("Sukces");
				 		        notification.setDescription("<span>Dane zostały zauktualizowane</span> ");
				 		        notification.setHtmlContentAllowed(true);
				 		        notification.setStyleName("tray dark small closable login-help");
				 		        notification.setPosition(Position.TOP_CENTER);
				 		        notification.setDelayMsec(3000); //zniknie po 3 sek
				 		        notification.show(Page.getCurrent());
					         	
					         }catch(NoResultException e){}	
						}
					}
				});
		        
	        }catch(NoResultException e){
	        	btn.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						
						if(noteTypeField.isEmpty())
						{
							noteTypeField.focus();
							Notification notification = new Notification("Pomoc");
			 		        notification.setDescription("<span>Dodaj ocenę</span> ");
			 		        notification.setHtmlContentAllowed(true);
			 		        notification.setStyleName("tray dark small closable login-help");
			 		        notification.setPosition(Position.TOP_CENTER);
			 		        notification.setDelayMsec(3000); //zniknie po 3 sek
			 		        notification.show(Page.getCurrent());
						}
						else
						{
							//EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
					        
					        try{
					        	Query q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :un");
					         	q.setParameter("un", value);
					         	User reciver = (User)q.getSingleResult();
					         	
					         	q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :un");
					         	q.setParameter("un", VaadinSession.getCurrent().getAttribute("user"));
					         	User sender = (User)q.getSingleResult();
					        	
					         	Notes notesTemp = new Notes();
					         	notesTemp.setReciver(reciver);
					         	notesTemp.setSender(sender);
					         	notesTemp.setDescrbe(describeField.getValue());
					         	notesTemp.setDate(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
					         	notesTemp.setNote_type(noteTypeField.getValue().toString());
					         	
					         	em.getTransaction().begin();
					         	em.persist(notesTemp);
					         	em.getTransaction().commit();
					         	
					         	UI.getCurrent().removeWindow(window);
					         	
					         	Notification notification = new Notification("Sukces");
				 		        notification.setDescription("<span>Dane zostały zauktualizowane</span> ");
				 		        notification.setHtmlContentAllowed(true);
				 		        notification.setStyleName("tray dark small closable login-help");
				 		        notification.setPosition(Position.TOP_CENTER);
				 		        notification.setDelayMsec(3000); //zniknie po 3 sek
				 		        notification.show(Page.getCurrent());
					         	
					         }catch(NoResultException e){}	
						}
					}
				});
	        	
	        }
	        
	        
	     
	    }  
	}
}

