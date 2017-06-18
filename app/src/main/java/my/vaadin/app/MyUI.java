package my.vaadin.app;

import static my.vaadin.app.Constants.NAVIGATION_START_PAGE;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.Page.PopStateEvent;
import com.vaadin.server.Page.PopStateListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout = new VerticalLayout();
	private HeaderLayout headerLayout = new HeaderLayout(this);
	private BodyLayout bodyLayout = new BodyLayout(this);

	@Override
	protected void init(VaadinRequest vaadinRequest) {

		getPage().addPopStateListener(new PopStateListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void uriChanged(PopStateEvent event) {

				navigateToPage(Page.getCurrent().getLocation().toString());
			}

		});

		if (Page.getCurrent().getLocation() == null || Page.getCurrent().getLocation().toString().isEmpty()) {

			Page.getCurrent().pushState(NAVIGATION_START_PAGE);
		} else {
			navigateToPage(Page.getCurrent().getLocation().toString());
		}

		mainLayout.addComponents(headerLayout, bodyLayout);
		setContent(mainLayout);

	}

	public HeaderLayout getHeaderLayout() {
		return headerLayout;
	}

	public BodyLayout getBodyLayout() {
		return bodyLayout;
	}

	private void navigateToPage(String url) {
		String[] splitUrl = url.split("\\?");
		String[] splitVariables = splitUrl[1].split("&");
		int pageId = Integer.parseInt(splitVariables[0].split("=")[1]);

		switch (pageId) {
		case 1:
			bodyLayout.searchForMovies(false, null, 0);
			break;
		case 2:
			String searchQuery = splitVariables[1].split("=")[1];
			int resultPageNum = Integer.parseInt(splitVariables[2].split("=")[1]);
			bodyLayout.searchForMovies(false, searchQuery, resultPageNum);
			break;
		case 3:
			int movieId = Integer.parseInt(splitVariables[1].split("=")[1]);
			bodyLayout.showMovieDetails(movieId);
			break;

		default:
			break;
		}

	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;

	}
}
