package my.vaadin.app;

import static my.vaadin.app.TMDbPaths.*;

import java.text.DecimalFormat;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;

public class DetailsLayout extends FormLayout {

	private static final long serialVersionUID = 1L;

	private JsonObject jsObject;
	private BodyLayout bodyLayout;
	private int movieID;

	private boolean appendVideos;
	private boolean appendSimilar;
	private boolean appendCredits;
	private boolean appendImages;

	public static class DetailsLayoutBuilder {
		private BodyLayout bodyLayout;
		private int movieID;

		private boolean appendVideos;
		private boolean appendSimilar;
		private boolean appendCredits;
		private boolean appendImages;

		public DetailsLayoutBuilder(BodyLayout bodyLayout, int movieID) {
			this.bodyLayout = bodyLayout;
			this.movieID = movieID;
		}

		public DetailsLayoutBuilder appendVideos(boolean appendVideos) {
			this.appendVideos = appendVideos;
			return this;
		}

		public DetailsLayoutBuilder appendSimilar(boolean appendSimilar) {
			this.appendSimilar = appendSimilar;
			return this;
		}

		public DetailsLayoutBuilder appendCredits(boolean appendCredits) {
			this.appendCredits = appendCredits;
			return this;
		}

		public DetailsLayoutBuilder appendImages(boolean appendImages) {
			this.appendImages = appendImages;
			return this;
		}

		public DetailsLayout build() {
			return new DetailsLayout(this);
		}

	}

	private DetailsLayout(DetailsLayoutBuilder builder) {
		this.bodyLayout = builder.bodyLayout;
		this.movieID = builder.movieID;

		this.appendVideos = builder.appendVideos;
		this.appendSimilar = builder.appendSimilar;
		this.appendCredits = builder.appendCredits;
		this.appendImages = builder.appendImages;

		setMargin(false);
		setSpacing(false);
		addComponent(getMovieDetails());
	}

	private Component getMovieDetails() {
		jsObject = JsonSingleton.getInstance()
				.getJsonObjectFromURL(MOVIE_DETAILS.replace(REPLACE_STRING, "" + movieID) + appendToResponse());

		VerticalLayout leftLayout = new VerticalLayout();
		VerticalLayout middleLayout = new VerticalLayout();
		HorizontalLayout movieDetailsLayout = new HorizontalLayout();
		HorizontalLayout movieDetailsHeaderLayout = new HorizontalLayout();

		// TODO add to layout, add icons for bookmarks etc.

		Label movieName = new Label(jsObject.getString(TITLE));
		movieName.addStyleName(ValoTheme.LABEL_H2);

		String poster = jsObject.getString(POSTER_PATH);
		String title = jsObject.getString(TITLE);
		String imageID = String.valueOf((int) jsObject.getNumber("id"));

		Image movieImage = CustomItems.getImage(poster, title, imageID, POSTER_IMAGE_300, true, null);

		Button imdbLink = new Button("IMDB");
		imdbLink.setStyleName(ValoTheme.BUTTON_LINK);
		imdbLink.addClickListener(e -> {
			getUI().getPage().open(IMDB_LINK + jsObject.getString(IMDB_ID), "_blank");
		});

		HorizontalLayout avgVote = CustomItems.avgVoteStarLayout(String.valueOf(jsObject.getNumber(VOTE_AVERAGE)));
		avgVote.setDescription(String.valueOf(jsObject.getNumber(VOTE_COUNT)));

		Label description = CustomItems.descriptionLabel(jsObject.getString(DESCRIPTION),true);
		HorizontalLayout genres = CustomItems.genresLayout(jsObject.getArray(GENRES), true);

		movieDetailsHeaderLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		movieDetailsHeaderLayout.addComponents(imdbLink, avgVote);

		middleLayout.addComponents(movieDetailsHeaderLayout, description, genres);

		movieDetailsLayout.addComponents(movieImage, middleLayout);

		leftLayout.addComponents(movieName, movieDetailsLayout, getDetailsTabSheet());

		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(getDetailsTabSheet());

		return leftLayout;
	}

	private TabSheet getDetailsTabSheet() {
		TabSheet movieTabs = new TabSheet();

		movieTabs.addTab(getDetailsTab(), "Details");
		movieTabs.addTab(getTrailerTab(), "Trailers");
		movieTabs.addTab(getImageTab(), "Images");
		movieTabs.addTab(getCastTab(), "Cast");
		movieTabs.addTab(getCrewTab(), "Crew");
		// Notification.show(UI.getCurrent().getPage().getBrowserWindowWidth()+"
		// ");
		movieTabs.addTab(new SearchLayout(bodyLayout, jsObject.getObject(RECOMMENDATIONS), true, null),
				"Recommendations");

		return movieTabs;
	}

	private VerticalLayout getDetailsTab() {
		VerticalLayout movieDetails = new VerticalLayout();

		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

		String home = jsObject.getString(MOVIE_HOME_PAGE);
		Label homePage = CustomItems
				.htmlLabel("<b>Homepage:</b> <a href='" + home + "' target='_blank'>" + home + "</a>");
		Label status = CustomItems.htmlLabel("<b>Status: </b>" + jsObject.getString(MOVIE_STATUS));
		Label releaseDate = CustomItems.htmlLabel("<b>Release date: </b>" + jsObject.getString(MOVIE_RELEASE_DATE));
		Label runtime = CustomItems.htmlLabel("<b>Runtime: </b>" + String.valueOf(jsObject.getNumber(MOVIE_RUNTIME)));
		Label budget = CustomItems
				.htmlLabel("<b>Budget: </b>" + decimalFormat.format(jsObject.getNumber(MOVIE_BUDGET)));
		Label revenue = CustomItems
				.htmlLabel("<b>Revenue: </b>" + decimalFormat.format(jsObject.getNumber(MOVIE_REVENUE)));

		String collectionString = "<b>Collection: </b>";
		if (!jsObject.get(MOVIE_COLLECTION).jsEquals(Json.createNull())) {
			collectionString += jsObject.getObject(MOVIE_COLLECTION).getString("name");
			// TODO open collection details on click
		}
		Label collections = CustomItems.htmlLabel(collectionString);
		Label productionCompanies = getcompanies(jsObject.getArray(MOVIE_PRODUCTION_COMPANY), "Production companies: ");
		Label productionCountries = getcompanies(jsObject.getArray(MOVIE_PRODUCTION_COUNTRY), "Production countries: ");
		Label spokenLanguages = getcompanies(jsObject.getArray(MOVIE_SPOKEN_LANGUAGE), "Spoken languages: ");

		movieDetails.addComponents(homePage, status, releaseDate, runtime, budget, revenue, collections,
				productionCompanies, productionCountries, spokenLanguages);

		return movieDetails;
	}

	private Label getcompanies(JsonArray array, String title) {
		String names = "<b>" + title + "</b>";
		String delim = "";
		for (int i = 0; i < array.length(); i++) {
			names += delim + array.getObject(i).getString("name");
			delim = ", ";
		}
		Label l = CustomItems.htmlLabel(names);

		return l;
	}

	private Component getTrailerTab() {
		CssLayout trailers = new CssLayout();
		trailers.setWidth("1000px");
		for (int i = 0; i < jsObject.getObject(VIDEOS).getArray(RESULTS).length(); i++) {
			Label l = CustomItems.htmlLabel("<iframe width='450' height='300' style='margin:15px;' src='"
					+ YOUTUBE_TRAILER_ROOT + jsObject.getObject(VIDEOS).getArray(RESULTS).getObject(i).getString("key")
					+ "' allowfullscreen ></iframe>");
			trailers.addComponent(l);
		}
		if (trailers.getComponentCount() < 1) {
			trailers.addComponent(new Label("No trailers available..."));
		}

		return trailers;
	}

	private Component getImageTab() {
		CssLayout images = new CssLayout();
		images.setWidth("1000px");

		String title = jsObject.getString(TITLE);
		String imageID = String.valueOf((int) jsObject.getNumber("id"));
		JsonArray moviePosters = jsObject.getObject(IMAGES).getArray(POSTERS);

		for (int i = 0; i < moviePosters.length(); i++) {
			String poster = moviePosters.getObject(i).getString(POSTER_FILE_PATH);
			Image img = CustomItems.getImage(poster, title, imageID, POSTER_IMAGE_185, true, null);

			// img.addStyleName("detailsImageMarginFix");
			images.addComponent(img);
		}
		if (images.getComponentCount() < 1) {
			images.addComponent(new Label("No trailers available..."));
		}
		return images;
	}

	private Component getCastTab() {
		VerticalLayout castsLayout = new VerticalLayout();
		JsonArray castsArray = jsObject.getObject(CREDITS).getArray(CAST);

		for (int i = 0; i < castsArray.length(); i++) {
			HorizontalLayout hl = new HorizontalLayout();
			VerticalLayout castDetails = new VerticalLayout();

			String title = castsArray.getObject(i).getString(CAST_CHARACTER);
			String imageID = String.valueOf((int) castsArray.getObject(i).getNumber("id"));
			String poster = castsArray.getObject(i).get(CAST_CREW_PROFILE_PATH).jsEquals(Json.createNull()) ? ""
					: castsArray.getObject(i).getString(CAST_CREW_PROFILE_PATH);

			Image img = CustomItems.getImage(poster, title, imageID, POSTER_IMAGE_185, true, null);

			Label character = new Label("Character: " + title);
			Label actor = new Label("Name: " + castsArray.getObject(i).getString(CAST_CREW_NAME));

			castDetails.addComponents(character, actor);
			hl.addComponents(img, castDetails);
			castsLayout.addComponent(hl);
		}

		return castsLayout;
	}

	private Component getCrewTab() {
		VerticalLayout crewLayout = new VerticalLayout();

		JsonArray crewArray = jsObject.getObject(CREDITS).getArray(CREW);

		for (int i = 0; i < crewArray.length(); i++) {
			HorizontalLayout hl = new HorizontalLayout();
			VerticalLayout crewDetails = new VerticalLayout();

			String title = crewArray.getObject(i).getString(CAST_CREW_NAME);
			String imageID = String.valueOf((int) crewArray.getObject(i).getNumber("id"));
			String poster = crewArray.getObject(i).get(CAST_CREW_PROFILE_PATH).jsEquals(Json.createNull()) ? ""
					: crewArray.getObject(i).getString(CAST_CREW_PROFILE_PATH);
			String departmentName = crewArray.getObject(i).get(CREW_DEPARTMENT).jsEquals(Json.createNull()) ? ""
					: crewArray.getObject(i).getString(CREW_DEPARTMENT);
			String jobtName = crewArray.getObject(i).get(CREW_JOB).jsEquals(Json.createNull()) ? ""
					: crewArray.getObject(i).getString(CREW_JOB);

			Image img = CustomItems.getImage(poster, title, imageID, POSTER_IMAGE_185, true, null);

			Label name = new Label("Name: " + title);
			Label department = new Label("Department: " + departmentName);
			Label job = new Label("Job: " + jobtName);

			crewDetails.addComponents(name, department, job);
			hl.addComponents(img, crewDetails);
			crewLayout.addComponent(hl);
		}

		return crewLayout;
	}

	// private Component getRecommendationTab() {
	//
	//
	// return movieDetails;
	// }

	private String appendToResponse() {
		String append = APPEND_TO_RESPONSE;
		String delimiter = "";
		if (appendVideos) {
			append += "videos";
			delimiter = ",";
		}
		if (appendSimilar) {
			append += delimiter + "similar";
			delimiter = ",";
		}
		if (appendCredits) {
			append += delimiter + "credits";
			delimiter = ",";
		}
		if (appendImages) {
			append += delimiter + IMAGES;
			delimiter = ",";
		}
		return append;
	}

}
