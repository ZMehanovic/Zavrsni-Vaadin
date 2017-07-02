package my.vaadin.app.client;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class LoginWindow extends Window{

	private static final long serialVersionUID = 1L;

	public LoginWindow() {
		super();
		getWindow();
	}

	private void getWindow() {
		setCaption("Login");
		setWidth("500px");
		setHeight("500px");
		center();

		VerticalLayout loginLayout = new VerticalLayout();
		HorizontalLayout valuesLayout = new HorizontalLayout();
		VerticalLayout labelLayout = new VerticalLayout();
		VerticalLayout fieldLayout = new VerticalLayout();

		Label usernameLabel = new Label("Username: ");
		usernameLabel.setHeight("40px");
		Label passwordLabel = new Label("Password: ");
		passwordLabel.setHeight("40px");

		TextField usernameField = new TextField();
		TextField passwordField = new TextField();

		Button loginButton = new Button("Login");

		labelLayout.addComponents(usernameLabel, passwordLabel);
		fieldLayout.addComponents(usernameField, passwordField);

		valuesLayout.addComponents(labelLayout, fieldLayout);
		loginLayout.addComponents(valuesLayout, loginButton);

		setContent(loginLayout);
	}


	
	
}
