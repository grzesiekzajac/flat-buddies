package com.example.javaee.Views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.eclipse.persistence.internal.sessions.remote.SequencingFunctionCall.GetNextValue;

import com.example.javaee.Models.MessageContent;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.themes.ValoTheme;

public class Menu extends VerticalLayout {
	
	TabSheet detailsWrapper;
	Component defaultComponent;
	Component settingsComponent;
	Component st;
	
	public Menu(int position)
	{
		detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        addComponent(detailsWrapper);
        setExpandRatio(detailsWrapper, 1f);
        
        detailsWrapper.addComponent(buildBoardTab());
        detailsWrapper.addComponent(buildFlatTab());
        detailsWrapper.addComponent(buildTimetableTab());
        detailsWrapper.addComponent(buildTaskTab());
        detailsWrapper.addComponent(buildShopTab());
        detailsWrapper.addComponent(buildBillTab());
        detailsWrapper.addComponent(buildMessageTab());
        detailsWrapper.addComponent(buildRateTab());
        
        settingsComponent = buildSettingsTab();
        detailsWrapper.addComponent(settingsComponent);
        detailsWrapper.addComponent(buildLogoutTab()); 
        
        detailsWrapper.setSelectedTab(position);

        detailsWrapper.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				String tabName = event.getTabSheet().getSelectedTab().getCaption();
				
				if(tabName == "Wyloguj")
				{
					getSession().setAttribute("user", null);
					//Refresh this view, should redirect to login view
					getUI().getNavigator().addView("", new StartView());
		            getUI().getNavigator().navigateTo("");			            
				}
				else if(tabName == "Ustawienia")
				{
					getUI().getNavigator().addView("settings", new SettingsView());
			        getUI().getNavigator().navigateTo("settings");
				}
				else if(tabName == "Oceny")
				{
					getUI().getNavigator().addView("rate", new RateView());
			        getUI().getNavigator().navigateTo("rate");
				}
				else if(tabName == "Wiadomości")
				{
					getUI().getNavigator().addView("message", new MessageView());
			        getUI().getNavigator().navigateTo("message");
				}
				else if(tabName == "Tablica")
				{
					getUI().getNavigator().addView("main", new MainView());
			        getUI().getNavigator().navigateTo("main");
				}
				else if(tabName == "Mieszkanie")
				{
					getUI().getNavigator().addView("flat", new FlatView());
			        getUI().getNavigator().navigateTo("flat");
				}
				else if(tabName == "Harmonogram")
				{
					getUI().getNavigator().addView("timetable", new TimetableView());
			        getUI().getNavigator().navigateTo("timetable");
				}
				else if(tabName == "Zadania")
				{
					getUI().getNavigator().addView("task", new TaskView());
			        getUI().getNavigator().navigateTo("task");
				}
				else if(tabName == "Zakupy")
				{
					getUI().getNavigator().addView("shopping", new ShoppingView());
			        getUI().getNavigator().navigateTo("shopping");
				}
				else if(tabName == "Rachunki")
				{
					getUI().getNavigator().addView("bill", new BillView());
			        getUI().getNavigator().navigateTo("bill");
				}
			}
		});
	}
	
	  private Component buildBoardTab() {
	        VerticalLayout root = new VerticalLayout();
	        root.setCaption("Tablica");
	        root.setIcon(FontAwesome.BULLHORN);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.setSizeFull();  

	        return root;
	    }
		
		private Component buildFlatTab() {
	        VerticalLayout root = new VerticalLayout();
	        root.setCaption("Mieszkanie");
	        root.setIcon(FontAwesome.HOME);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.setSizeFull();
	        
	        return root;
	    }
		
		private Component buildTimetableTab() {
	        VerticalLayout root = new VerticalLayout();
	        root.setCaption("Harmonogram");
	        root.setIcon(FontAwesome.CALENDAR);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.setSizeFull();

	        return root;
	    }
		
		private Component buildTaskTab() {
	        VerticalLayout root = new VerticalLayout();
	        root.setCaption("Zadania");
	        root.setIcon(FontAwesome.CHECK_SQUARE);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.setSizeFull();

	        return root;
	    }
		
		private Component buildShopTab() {
	        VerticalLayout root = new VerticalLayout();
	        root.setCaption("Zakupy");
	        root.setIcon(FontAwesome.SHOPPING_CART);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.setSizeFull();

	        return root;
	    }
		
		private Component buildBillTab() {
	        VerticalLayout root = new VerticalLayout();
	        root.setCaption("Rachunki");
	        root.setIcon(FontAwesome.CREDIT_CARD);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.setSizeFull();

	        return root;
	    }
		
		private Component buildMessageTab() {
	        VerticalLayout root = new VerticalLayout();
	        root.setCaption("Wiadomości");
	        root.setIcon(FontAwesome.INBOX);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.setSizeFull();

	        return root;
	    }
		
		private Component buildRateTab() {
	        VerticalLayout root = new VerticalLayout();
	        root.setCaption("Oceny");
	        root.setIcon(FontAwesome.THUMBS_UP);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.setSizeFull();

	        return root;
	    }
		
		private Component buildSettingsTab() {
	        VerticalLayout root = new VerticalLayout();
	        root.setCaption("Ustawienia");
	        root.setIcon(FontAwesome.GEARS);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.setSizeFull();

	        return root;
	    }
		
		private Component buildLogoutTab()
		{
			VerticalLayout root = new VerticalLayout();
			root.setCaption("Wyloguj");
	        root.setIcon(FontAwesome.POWER_OFF);
	        return root;
			
		}
}
