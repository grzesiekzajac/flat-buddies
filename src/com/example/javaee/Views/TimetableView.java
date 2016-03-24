package com.example.javaee.Views;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.javaee.Models.TimeTable;
import com.example.javaee.Models.User;
import com.google.gwt.layout.client.Layout;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.BeanItemContainer;
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
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
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
import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.themes.ValoTheme;


/*
 * 
 * try {

            	    Client client = new Client("grzez.magik@gmail.com");
            	    client.setPasswordHash("baba6b0565a01138b7a63dab17bca5ae");

            	    SmsFactory smsApi = new SmsFactory(client);
            	    SMSSend action = smsApi.actionSend()
            	            .setText("Siemanoo")
            	            .setTo("500751183")
            	            .setSender("ECO"); //Pole nadawcy lub typ wiadomość 'ECO', '2Way'

            	    StatusResponse result = action.execute();

            	    for(MessageResponse status : result.getList() ) {

            	        System.out.println( status.getNumber() + " " + status.getStatus() );
            	    }
            	} catch( Exception f) {
            	    
            	}
 */

@SuppressWarnings("serial")
public class TimetableView extends Panel implements View {

    public TimetableView() 
    {
    	setSizeUndefined();
    	setWidth("100%");
	
		VerticalLayout layout = new VerticalLayout();
		setContent(layout);
		layout.setSizeFull();
		layout.setMargin(true);
		
		
		layout.addComponent(new Header());
		
		layout.addComponent(new Menu(2));
		
		VerticalLayout vl = new TimeTableForm();
		layout.addComponent(vl);
	
		
    }

    public class TimeTableForm extends VerticalLayout {

    	public TimeTableForm()
    	{
    		setSizeFull();
            Component timeTableForm = buildTaskForm();
            addComponent(timeTableForm);
            setComponentAlignment(timeTableForm, Alignment.MIDDLE_CENTER);
            //this.setHeight(100.0f, Unit.PERCENTAGE); 
            //this.setHeightUndefined(); 
            this.setWidth(100.0f, Unit.PERCENTAGE); 
            this.setHeight(100.0f, Unit.PERCENTAGE);
    	}
    	
    	private Component buildTaskForm()
    	{
    		final VerticalLayout taskPanel = new VerticalLayout();
            //taskPanel.setSizeFull();
    		taskPanel.setWidth(100.0f, Unit.PERCENTAGE); 
    		taskPanel.setHeight(100.0f, Unit.PERCENTAGE);
            //taskPanel.setSpacing(true);
            Responsive.makeResponsive(taskPanel);
            //taskPanel.addStyleName("login-panel");
            
            taskPanel.addComponent(buildButton());
            taskPanel.addComponent(buildTasks());
            return taskPanel;
    	}
    	
    	private Component buildTasks() 
    	{
    		           
            VerticalLayout root =  new VerticalLayout();
            Panel panel;

            HorizontalLayout timeFrameLayout = new HorizontalLayout();
            root.addComponent(timeFrameLayout);
            root.setComponentAlignment(timeFrameLayout, Alignment.TOP_CENTER);

            
            
        	panel= new Panel("Kalendarz harmonogramów");
            panel.setIcon(FontAwesome.CALENDAR); //tytul, ikone i link trzeba zrobic w switchu w zaleznosci od typy posta
            panel.setWidth(90.0f, Unit.PERCENTAGE); 
 
            panel.setHeightUndefined();
            root.addComponent(panel);
            root.setComponentAlignment(panel, Alignment.TOP_CENTER);            
            
            VerticalLayout content = new VerticalLayout();
            panel.setContent(content);
            content.setWidth(100.0f, Unit.PERCENTAGE); 
            content.setHeight(900.0f, Unit.PIXELS);   
             
            
            Calendar cal = new Calendar("");
            cal.setWidth(100.0f, Unit.PERCENTAGE);
            cal.setHeight(92.0f, Unit.PERCENTAGE);            
            content.addComponent(cal);
            
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            
            final int rollAmount = gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH) - 1;
            gregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);
            Date currentMonthsFirstDate = gregorianCalendar.getTime();
            cal.setStartDate(currentMonthsFirstDate);
            gregorianCalendar.add(GregorianCalendar.MONTH, 1);
            gregorianCalendar.add(GregorianCalendar.DATE, -1);
            cal.setEndDate(gregorianCalendar.getTime());
            
            Button day = new Button("Dzień");
            day.addClickListener(new Button.ClickListener() {
    			public void buttonClick(ClickEvent event) {
    				cal.setStartDate(new Date());
    	            cal.setEndDate(new Date());
    			}
    		});
            day.addStyleName(ValoTheme.BUTTON_BORDERLESS);
            
            Button  week = new Button("Tydzień");
            week.addClickListener(new Button.ClickListener() {
    			public void buttonClick(ClickEvent event) {
    				GregorianCalendar gregorianCalendar = new GregorianCalendar();
    				final int rollAmount = gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) - 1;
    	            gregorianCalendar.add(GregorianCalendar.DAY_OF_WEEK, -rollAmount);
    	            Date currentWeeksFirstDate = gregorianCalendar.getTime();
    	            cal.setStartDate(currentWeeksFirstDate);
    	            gregorianCalendar.add(GregorianCalendar.WEEK_OF_YEAR, 1);
    	            gregorianCalendar.add(GregorianCalendar.DATE, -1);
    	            cal.setEndDate(gregorianCalendar.getTime());
    			}
    		});
            week.addStyleName(ValoTheme.BUTTON_BORDERLESS);
            
            Button  month = new Button("Miesiąc");
            //more.setIcon(FontAwesome.PLUS);
            month.addClickListener(new Button.ClickListener() {
    			public void buttonClick(ClickEvent event) {
    				GregorianCalendar gregorianCalendar = new GregorianCalendar();
    				final int rollAmount = gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH) - 1;
    		        gregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);
    		        //resetTime(false);
    		        Date currentMonthsFirstDate = gregorianCalendar.getTime();
    		        cal.setStartDate(currentMonthsFirstDate);
    		        gregorianCalendar.add(GregorianCalendar.MONTH, 1);
    		        gregorianCalendar.add(GregorianCalendar.DATE, -1);
    		        cal.setEndDate(gregorianCalendar.getTime());
    			}
    		});
            month.addStyleName(ValoTheme.BUTTON_BORDERLESS);
            
            Button  next = new Button("");
            next.setIcon(FontAwesome.FORWARD);
            next.addClickListener(new Button.ClickListener() {
    			public void buttonClick(ClickEvent event) {
    				//cal.handleNextButtonClick();
    			}
    		});
            next.addStyleName(ValoTheme.BUTTON_BORDERLESS);
            
            Button  previous = new Button("");
            previous.setIcon(FontAwesome.BACKWARD);
            previous.addClickListener(new Button.ClickListener() {
    			public void buttonClick(ClickEvent event) {
    				//handleNextButtonClick();
    			}
    		});
            previous.addStyleName(ValoTheme.BUTTON_BORDERLESS);
            previous.addStyleName(ValoTheme.BUTTON_TINY);
            day.addStyleName(ValoTheme.BUTTON_TINY);
            week.addStyleName(ValoTheme.BUTTON_TINY);
            month.addStyleName(ValoTheme.BUTTON_TINY);
            next.addStyleName(ValoTheme.BUTTON_TINY);
                
    		
            timeFrameLayout.addComponents(previous, day, week, month, next);
            
            EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
            
            try{
             	Query q = em.createQuery("SELECT u FROM User u WHERE u.id = :un");
             	q.setParameter("un", VaadinSession.getCurrent().getAttribute("user_id"));
             	User us = (User)q.getSingleResult();
             	
             	q = em.createQuery("SELECT u FROM Flat u WHERE u.flat_name = :un");
             	q.setParameter("un", VaadinSession.getCurrent().getAttribute("flat_name"));
             	Flat flat = (Flat)q.getSingleResult();
             	
             	q = em.createQuery("SELECT u FROM TimeTable u WHERE u.flat = :uf");
             	q.setParameter("uf", flat);
             	List<TimeTable> ttList = (List<TimeTable>)q.getResultList();
            

            final BeanItemContainer<BasicEvent> eventContainer = new BeanItemContainer<BasicEvent>(BasicEvent.class);
            
            for(TimeTable tt : ttList)
            {
            	eventContainer.addBean(new BasicEvent(tt.getTitle(), 
						  tt.getTitle(),
						  tt.getBegin(),
						  tt.getEnd()));
            }
            
            eventContainer.sort(new Object[]{"start"}, new boolean[]{true});
            
            
            cal.setContainerDataSource(eventContainer, "caption", "description", "start", "end", "styleName");
            
            
            }catch(NoResultException e){}
           

            return root;
        }

        private Component buildButton() 
        {
        	final VerticalLayout root = new VerticalLayout();
        	final FormLayout formLayout = new FormLayout();
        	
        	        	
        	Button more = new Button("Dodaj nowy harmonogram");
            more.setIcon(FontAwesome.PLUS);
    		more.addClickListener(new Button.ClickListener() {
    			public void buttonClick(ClickEvent event) {
    				UI.getCurrent().addWindow(buildWindow());
    			}
    		});
    		more.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    		Label space = new Label("\n\n");
    		root.addComponents(more,space);
    		root.setComponentAlignment(more, Alignment.TOP_CENTER);
    		
    		return root;
        }
        
        private Window buildWindow()//String flatName) 
        {
        	final Window window = new Window("Nowy harmonogram");
            addStyleName("profile-window");
            Responsive.makeResponsive(this);

            window.setModal(true);
            window.setCloseShortcut(KeyCode.ESCAPE, null);
            window.setResizable(false);
            window.setClosable(true);
            window.setWidth(40.0f, Unit.PERCENTAGE);
            window.setHeight(90.0f, Unit.PERCENTAGE);
                    
            final FormLayout content = new FormLayout();
            window.setContent(buildWindowForm(window));//flatName));
        	
        	return window;
        }
        
        private Component buildWindowForm(Window window)//String flatName) 
        {
            FormLayout fields = new FormLayout();
            fields.setSpacing(true);        
            
            Label tlitle = new Label("Nazwa harmonogramu:");
            TextField taskName = new TextField("");
            Label description = new Label("Opis harmonogramu:");
            TextField taskDescription = new TextField("");
            taskDescription.setWidth(96.0f,  Unit.PERCENTAGE);
            Label fromLabel = new Label("Data rozpoczęciau:");
            PopupDateField from = new PopupDateField();
            Label stepLabel = new Label("Co ile dni:");
            NativeSelect step = new NativeSelect("");
            for (int i = 1; i < 31; i++) { 
            	step.addItem(i);
            	if(i == 1) step.setItemCaption(i, "Co 1 dzień");
            	else step.setItemCaption(i, "Co " + i + " dni");
            }
            Label toLabel = new Label("Data zakończenia:");
            PopupDateField to = new PopupDateField();
            Label chooseFlatmates = new Label("Wybierz lokatorów:\n");
            fields.addComponents(tlitle, taskName, description, taskDescription, fromLabel, from, stepLabel, step, toLabel, to, chooseFlatmates);

            Map<CheckBox, String> flatMatesChecked = new HashMap();		

            EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
	        
	        try{
	        	
	         	Query q = em.createQuery("SELECT f FROM Flat f WHERE f.flat_name = :uf");
	         	q.setParameter("uf", VaadinSession.getCurrent().getAttribute("flat_name"));
	         	Flat flat = (Flat)q.getSingleResult();
	         	
	         	q = em.createQuery("SELECT u FROM User u WHERE u.user_name = :uf");
	         	q.setParameter("uf", VaadinSession.getCurrent().getAttribute("user"));
	         	User user = (User)q.getSingleResult();
	         	
         		q = em.createQuery("SELECT u FROM User u WHERE u.flat = :uf");
		        q.setParameter("uf", flat);
		        List<User> flatmates = (List<User>)q.getResultList();
            
            for (User name: flatmates) {
            	CheckBox flatmateCheckBox = new CheckBox(name.getUser_name(), false);
            	flatmateCheckBox.addStyleName(ValoTheme.CHECKBOX_SMALL);
            	fields.addComponent(flatmateCheckBox);
            	flatMatesChecked.put(flatmateCheckBox, name.getUser_name());
            }
            
	        }catch(NoResultException e){}
            
            final Button addTask = new Button("Dodaj zadanie");
            addTask.addStyleName(ValoTheme.BUTTON_SMALL);
            addTask.setClickShortcut(KeyCode.ENTER);
            addTask.focus();    
                    
            fields.addComponent(addTask);
            fields.setComponentAlignment(addTask, Alignment.BOTTOM_CENTER);
            
            List<String> flatMatesList = new ArrayList();	        

            addTask.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                	String checkedFlatMatesList = "";
                	for(CheckBox key: flatMatesChecked.keySet()){
                		if (key.getValue()){
                			checkedFlatMatesList += (flatMatesChecked.get(key) + ", ");
                			flatMatesList.add(flatMatesChecked.get(key));
                		}
                	}
                	if (flatMatesList.size() > 0 && 
                		String.valueOf(taskName.getValue()) != null && 
                		String.valueOf(taskName.getValue()) != "" && 
                		String.valueOf(taskDescription.getValue()) != null && 
                		String.valueOf(taskDescription.getValue()) != "" &&
                		from.getValue() != null && 
                		to.getValue() != null &&
                		to.getValue().after(from.getValue())
                		){
                	Notification.show("Dodales harmonogram: " + String.valueOf(taskName.getValue()),"Opis: " + String.valueOf(taskDescription.getValue())+" Od: " + String.valueOf(from.getValue())+" Co: " + String.valueOf(step.getValue())+" dni Do: " + String.valueOf(to.getValue())+ "Dla lokatorow: " + checkedFlatMatesList, Type.TRAY_NOTIFICATION);
                	UI.getCurrent().removeWindow(window);
                	
                	Date fromDate = from.getValue();            	
                	Date toDate = to.getValue();
                	int stepDate = (Integer) step.getValue();
                	java.util.Calendar c = java.util.Calendar.getInstance();
                	int repetition = 0;
                	int listSize = flatMatesList.size();
                	
                	//tutaj petla for idzie po kolejnych datach od from co step do to i dodaje eventy
                	while(!fromDate.after(toDate)){
                		String lokator = flatMatesList.get(repetition%listSize);
                		String komunikat = "Dodajemy event" + String.valueOf(taskName.getValue()) + ": " + fromDate + " dla osoby: "+ lokator;
                		
                		EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit("JavaEE");
            	        
            	        try{
            	        	
            	         	Query q = em.createQuery("SELECT f FROM User f WHERE f.user_name = :uf");
            	         	q.setParameter("uf", lokator);
            	         	User user = (User)q.getSingleResult();
            	         	
            	         	q = em.createQuery("SELECT f FROM Flat f WHERE f.flat_name = :uf");
            	         	q.setParameter("uf", VaadinSession.getCurrent().getAttribute("flat_name"));
            	         	Flat flat = (Flat)q.getSingleResult();
                		
	                		TimeTable tt = new TimeTable();
	                		tt.setUser(user);
	                		tt.setTitle(user.getUser_name() + " : " + taskName.getValue());
	                		tt.setFlat(flat);
	                		tt.setBegin(fromDate);
	                		java.util.Calendar c2 = java.util.Calendar.getInstance();
	                		c2.setTime(fromDate); 
	                		c2.add(java.util.Calendar.HOUR, 12);            		
	                		fromDate = c.getTime();
	                		c2.add(java.util.Calendar.HOUR, 1);            		
	                		Date smallTo = c.getTime();
	                		c2.add(java.util.Calendar.HOUR, -12);            		
	                		fromDate = c.getTime();
	                		tt.setEnd(smallTo);
	                		
	                		em.getTransaction().begin();
	                		em.persist(tt);
	                		em.getTransaction().commit();
	                		
            	        }catch(NoResultException e){}
	                		
                		System.out.println(komunikat);
                		c.setTime(fromDate); 
                		c.add(java.util.Calendar.DATE, stepDate);            		
                		fromDate = c.getTime();
                		repetition++;
                	}
                	
                	getUI().getNavigator().addView("timetable", new TimetableView());
                	getUI().getNavigator().navigateTo("timetable");
                	
                	} 
                	else{
                		Notification.show("Aby dodać zadanie musisz wypełnić wszystkie pola i wybrać conajmniej jednego lokatora, a data zakończenia musi być po dacie rozpoczęcia.", Type.ERROR_MESSAGE);
                	}
                }
            });
            
            return fields;
        }
    }
    
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
   


	 



}
