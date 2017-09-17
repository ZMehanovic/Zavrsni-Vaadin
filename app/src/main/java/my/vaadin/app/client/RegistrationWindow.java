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

public class RegistrationWindow extends Window{

		private static final long serialVersionUID = 1L;

		public RegistrationWindow() {
			super();
			getWindow();
		}

		private void getWindow() {
			setCaption("Register");
			setWidth("500px");
			setHeight("500px");
			center();

			VerticalLayout registrationLayout = new VerticalLayout();
			HorizontalLayout valuesLayout = new HorizontalLayout();
			VerticalLayout labelLayout = new VerticalLayout();
			VerticalLayout fieldLayout = new VerticalLayout();

			Label emailLabel = new Label("Email: ");
			emailLabel.setHeight("40px");
			Label usernameLabel = new Label("Username: ");
			usernameLabel.setHeight("40px");
			Label passwordLabel = new Label("Password: ");
			passwordLabel.setHeight("40px");
			Label repeatLabel = new Label("Repeat Password: ");
			repeatLabel.setHeight("40px");

			TextField emailField = new TextField();
			TextField usernameField = new TextField();
			PasswordField passwordField = new PasswordField();
			PasswordField repeatField = new PasswordField();
			
			
			
			Button btnRegister = new Button("Login");
			btnRegister.setClickShortcut(KeyCode.ENTER);
			btnRegister.addClickListener(e->{
				String query ="SELECT USERNAME FROM users WHERE USERNAME='"+ usernameField.getValue()+"' OR EMAIL='"+emailField.getValue()+"'";
				ResultSet rs =DbManager.getInstance().executeQuery(query);
				 try {
//					 
					 if(rs.isBeforeFirst()){
						 Notification.show("User name has been taken...", Notification.Type.WARNING_MESSAGE);
					 }else if(!passwordField.getValue().equals(repeatField.getValue())){
						 Notification.show("Passwords do not match...", Notification.Type.WARNING_MESSAGE);
					 }else{
						 String sqlInsert ="INSERT INTO users (EMAIL, USERNAME, PASSWORD, ROLE_ID) VALUES ('"+emailField.getValue()+"','"+usernameField.getValue()+"', '"+passwordField.getValue()+"', '1')";
						 DbManager.getInstance().executeUpdateQuery(sqlInsert);
						 Notification.show("Registration successful.", Notification.Type.HUMANIZED_MESSAGE);
						 
					 }
//					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
//		

			});

			labelLayout.addComponents(emailLabel, usernameLabel, passwordLabel, repeatLabel);
			fieldLayout.addComponents(emailField, usernameField, passwordField, repeatField);

			valuesLayout.addComponents(labelLayout, fieldLayout);
			registrationLayout.addComponents(valuesLayout, btnRegister);
			registrationLayout.setComponentAlignment(btnRegister, Alignment.MIDDLE_CENTER);
			
			setContent(registrationLayout);
		}


}
