package my.vaadin.app.client;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import server.DbManager;

public class LoginWindow extends Window {

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
		PasswordField passwordField = new PasswordField();

		Button btnLogin = new Button("Login");
		btnLogin.setClickShortcut(KeyCode.ENTER);
		btnLogin.addClickListener(e -> {
			String query = "SELECT USERNAME FROM users WHERE USERNAME='" + usernameField.getValue() + "' AND PASSWORD='"
					+ passwordField.getValue() + "'";
			ResultSet rs = DbManager.getInstance().executeQuery(query);
			try {

				if (rs.isBeforeFirst()) {
					Notification.show("Login successful.", Notification.Type.HUMANIZED_MESSAGE);
				} else {
					Notification.show("Wrong User Name or Password.", Notification.Type.WARNING_MESSAGE);

				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		});

		labelLayout.addComponents(usernameLabel, passwordLabel);
		fieldLayout.addComponents(usernameField, passwordField);

		valuesLayout.addComponents(labelLayout, fieldLayout);
		loginLayout.addComponents(valuesLayout, btnLogin);
		loginLayout.setComponentAlignment(btnLogin, Alignment.MIDDLE_CENTER);

		setContent(loginLayout);
	}

}
