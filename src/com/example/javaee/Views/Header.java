package com.example.javaee.Views;


import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class Header extends VerticalLayout{
	
	public Header()
	{
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		Label lab = new Label();
		lab.setIcon(FontAwesome.HOME);
		hl.addComponent(lab);
		hl.addComponent(new Label("Mamkwadrat.pl"));
		addComponent(hl);
		
		Label user;
		if(VaadinSession.getCurrent().getAttribute("user") != null)
		{
			if(VaadinSession.getCurrent().getAttribute("flat_name") == null)
			{
				user = new Label("Witaj, " + VaadinSession.getCurrent().getAttribute("user") + "! Nie masz obecnie żadnego mieszkania</br>", ContentMode.HTML); 	
			}
			else
			{
				user = new Label("Witaj, " + VaadinSession.getCurrent().getAttribute("user") + "! Twoje obecne mieszkanie to: " + VaadinSession.getCurrent().getAttribute("flat_name"), ContentMode.HTML);
				
			}
			
			user.addStyleName(ValoTheme.LABEL_SMALL);
			user.setWidth(null);
			addComponent(user);
			setComponentAlignment(user, Alignment.TOP_RIGHT);
		}
		
		
		
		
		
	}
	
	
}
