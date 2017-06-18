package my.vaadin.app;

import static my.vaadin.app.Constants.*;

import com.vaadin.server.Page;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

import elemental.json.JsonArray;
import elemental.json.JsonObject;

public class BodyLayout extends FormLayout {

	private static final long serialVersionUID = 1L;

	private MyUI myUI;

	public BodyLayout(MyUI myUI) {
		this.myUI = myUI;
		getStartBody();

	}

	private void getStartBody() {
		removeAllComponents();
		JsonObject jsObject = JsonSingleton.getInstance().getJsonObjectFromURL(POPULAR_MOVIES);

		if (jsObject != null) {

			addComponent(getPopularMoviesPosterGrid(jsObject.getArray(RESULTS)));

		} else {
			Notification.show("Error fetching data...", Type.ERROR_MESSAGE);
		}

	}

	private GridLayout getPopularMoviesPosterGrid(JsonArray jsResults) {
		GridLayout popularMoviesGrid = new GridLayout(5, 8);

		for (int rowCounter = 0, i = 0; rowCounter < popularMoviesGrid.getRows(); rowCounter += 2) {
			for (int columnCounter = 0; columnCounter < popularMoviesGrid.getColumns(); columnCounter++, i++) {

				Label movieTitle = new Label((i + 1) + ". " + jsResults.getObject(i).getString(TITLE));
				movieTitle.setWidth(185, Unit.PIXELS);

				String poster = jsResults.getObject(i).getString(POSTER_PATH);
				String title = jsResults.getObject(i).getString(TITLE);
				String imageID = String.valueOf((int) jsResults.getObject(i).getNumber("id"));

				popularMoviesGrid.addComponent(
						CustomItems.getImage(poster, title, imageID, POSTER_IMAGE_185, false, this), columnCounter,
						rowCounter);
				popularMoviesGrid.addComponent(movieTitle, columnCounter, rowCounter + 1);
			}
		}
		popularMoviesGrid.setSpacing(true);
		return popularMoviesGrid;
	}

	public void searchForMovies(boolean scrollToTop, String searchString, int page) {
		if (searchString == null || searchString.isEmpty()) {
			getStartBody();
			Page.getCurrent().pushState(NAVIGATION_START_PAGE);
		} else {
			// TODO JSON singleton
			String url = MOVIE_SEARCH + QUERY + searchString + PAGEID + page;
			JsonObject jsObject = JsonSingleton.getInstance().getJsonObjectFromURL(url);
			if (jsObject != null && jsObject.getNumber(TOTAL_RESULTS) > 0) {
				removeAllComponents();
				addComponent(new SearchLayout(this, jsObject, false, searchString, null));
				if (scrollToTop) {
					UI.getCurrent().scrollIntoView(myUI.getHeaderLayout());
				}
			} else {
				Notification.show("Data not found.", Notification.Type.ERROR_MESSAGE);
			}
		}
	}

	public void showMovieDetails(int movieId) {
		removeAllComponents();
		addComponent(new DetailsLayout.DetailsLayoutBuilder(this, movieId).appendVideos(true).appendImages(true)
				.appendCredits(true).appendSimilar(true).build());

	}

	public void browseGenres(boolean scrollToTop,String genreIds, int page) {

		BrowseGenresLayout browseLayout=new BrowseGenresLayout();
		browseLayout.loadData(this, genreIds, page);
		removeAllComponents();
		addComponent(browseLayout);
		
		if (scrollToTop) {
			UI.getCurrent().scrollIntoView(myUI.getHeaderLayout());
		}
	}
}
