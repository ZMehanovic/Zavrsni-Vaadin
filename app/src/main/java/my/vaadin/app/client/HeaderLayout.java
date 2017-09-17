package my.vaadin.app.client;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class HeaderLayout extends HorizontalLayout {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
			myUI.getBodyLayout().browseGenres(false, null, 1);
		});
		btnLogin.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnLogin.addClickListener(e -> {
			myUI.addWindow(new LoginWindow());
		});
		btnRegister.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnRegister.addClickListener(e -> {
			myUI.addWindow(new RegistrationWindow());
			
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
				myUI.getBodyLayout().searchForMovies(false, txtSearch.getValue().replaceAll(" ", "%20"), 1);
				
		});
		searchLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		searchLayout.addComponents(txtSearch,btnSearch);
		return searchLayout;
	}

	
	
}
