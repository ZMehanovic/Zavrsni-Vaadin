package my.vaadin.app;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class HeaderLayout extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 901232259231055975L;

	private MyUI myUI;
	
	private TextField txtSearch=new TextField();
	private Button btnSearch=new Button();
	private Button btnBrowse=new Button("Browse");
	private Button btnLogin=new Button("Login");
	private Button btnRegister=new Button("Register");

	public HeaderLayout(MyUI myUI) {
		this.myUI = myUI;
//		Page.getCurrent().setUriFragment("header");
		setMargin(true);
		setSpacing(true);
		setSizeUndefined();
		addComponents(getSearchLayout(),getButtons());
	}

	private Component getButtons() {
		HorizontalLayout buttons = new HorizontalLayout();

		btnBrowse.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnBrowse.addClickListener(e -> {
			Notification.show("Click");
		});
		btnLogin.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnLogin.addClickListener(e -> {
			Notification.show("Click");
		});
		btnRegister.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnRegister.addClickListener(e -> {
			Notification.show("Click");
		});

		buttons.addComponents(btnBrowse, btnLogin, btnRegister);
		return buttons;
	}

	private Component getSearchLayout() {
		CssLayout searchLayout=new CssLayout();
		
		btnSearch.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnSearch.setIcon(VaadinIcons.SEARCH);
		btnSearch.setClickShortcut(KeyCode.ENTER);
		btnSearch.addClickListener(e-> {
				Notification.show("Click");
		});
		searchLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		searchLayout.addComponents(txtSearch,btnSearch);
		return searchLayout;
	}

	
	
}
