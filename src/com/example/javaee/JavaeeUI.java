package com.example.javaee;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.example.javaee.Views.*;


@SuppressWarnings("serial")
@Theme("javaee")
public class JavaeeUI extends UI {

	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = JavaeeUI.class)
	public static class JavaeeServlet extends VaadinServlet {	
	}

	@Override
	protected void init(VaadinRequest request) {
		
		Navigator navigator;		
		navigator = new Navigator(this, this);	
		navigator.addView("", new StartView());
		navigator.addView("main", new MainView());
		navigator.addView("settings", new SettingsView());
		navigator.addView("rate", new RateView());
		navigator.addView("message", new MessageView());
		navigator.addView("flat", new FlatView());
		navigator.addView("timetable", new TimetableView());
		navigator.addView("task", new TaskView());
		navigator.addView("shopping", new ShoppingView());
		navigator.addView("bill", new BillView());
		
		navigator.addViewChangeListener(new ViewChangeListener() {
			
			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				
				// Check if a user has logged in
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof StartView;

                if (!isLoggedIn && !isLoginView) {
                    // Redirect to login view always if a user has not yet
                    // logged in
                    getNavigator().navigateTo("");
                    return false;

                } else if (isLoggedIn && isLoginView) {
                    // If someone tries to access to login view while logged in,
                    // then cancel
                	navigator.addView("main", new MainView());
                    getNavigator().navigateTo("main");
                	return false;
                }

                return true;
			}
			
			@Override
			public void afterViewChange(ViewChangeEvent event) {
				
			}
		});
		
	}

}