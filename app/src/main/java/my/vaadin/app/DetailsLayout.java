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
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

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

		Image movieImage = bodyLayout.getImage(jsObject, POSTER_IMAGE_300, true, false);

		Button imdbLink = new Button("IMDB");
		imdbLink.setStyleName(ValoTheme.BUTTON_LINK);
		imdbLink.addClickListener(e -> {
			getUI().getPage().open(IMDB_LINK + jsObject.getString(IMDB_ID), "_blank");
		});

		HorizontalLayout avgVote = CustomItems.avgVoteStarLayout(String.valueOf(jsObject.getNumber(VOTE_AVERAGE)));
		avgVote.setDescription(String.valueOf(jsObject.getNumber(VOTE_COUNT)));

		Label description = CustomItems.descriptionLabel(jsObject.getString(DESCRIPTION));
		HorizontalLayout genres = CustomItems.genresLayout(jsObject.getArray(GENRES), true);

		movieDetailsHeaderLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		movieDetailsHeaderLayout.addComponents(imdbLink, avgVote);

		middleLayout.addComponents(movieDetailsHeaderLayout, description, genres);

		movieDetailsLayout.addComponents(movieImage, middleLayout);

		leftLayout.addComponents(movieName, movieDetailsLayout,getDetailsTabSheet());
		
		HorizontalLayout hl=new HorizontalLayout();
		hl.addComponent(getDetailsTabSheet());
		
		return leftLayout;
	}

	private TabSheet getDetailsTabSheet() {
		TabSheet movieTabs=new TabSheet();
		
		movieTabs.addTab(getDetailsTab(), "Details");
		movieTabs.addTab(getTrailerTab(), "Trailers");
		movieTabs.addTab(getImageTab(), "Images");
		movieTabs.addTab(getCastTab(), "Cast");
		movieTabs.addTab(getCrewTab(), "Crew");
		movieTabs.addTab(getRecommendationTab(), "Recommendations");
		
		return movieTabs;
	}

	private VerticalLayout getDetailsTab() {
		VerticalLayout movieDetails = new VerticalLayout();

		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

		String home=jsObject.getString(MOVIE_HOME_PAGE);
		Label homePage = CustomItems.htmlLabel("<b>Homepage:</b> <a href='"+home+"' target='_blank'>" +home+"</a>" );
		Label status = CustomItems.htmlLabel("<b>Status: </b>" + jsObject.getString(MOVIE_STATUS));
		Label releaseDate = CustomItems.htmlLabel("<b>Release date: </b>" + jsObject.getString(MOVIE_RELEASE_DATE));
		Label runtime = CustomItems.htmlLabel("<b>Runtime: </b>" + String.valueOf(jsObject.getNumber(MOVIE_RUNTIME)));
		Label budget = CustomItems.htmlLabel("<b>Budget: </b>" + decimalFormat.format(jsObject.getNumber(MOVIE_BUDGET)));
		Label revenue = CustomItems.htmlLabel("<b>Revenue: </b>" + decimalFormat.format(jsObject.getNumber(MOVIE_REVENUE)));
		
		String collectionString ="<b>Collection: </b>";
		if (!jsObject.get(MOVIE_COLLECTION).jsEquals(Json.createNull())) {
			collectionString += jsObject.getObject(MOVIE_COLLECTION).getString("name");
			//TODO open collection details on click
		}
		Label collections = CustomItems.htmlLabel(collectionString);
		Label productionCompanies=getcompanies(jsObject.getArray(MOVIE_PRODUCTION_COMPANY),"Production companies: ");
		Label productionCountries=getcompanies(jsObject.getArray(MOVIE_PRODUCTION_COUNTRY),"Production countries: ");
		Label spokenLanguages=getcompanies(jsObject.getArray(MOVIE_SPOKEN_LANGUAGE),"Spoken languages: ");
		
		movieDetails.addComponents(homePage, status, releaseDate, runtime, budget, revenue, collections, productionCompanies, productionCountries, spokenLanguages);

		return movieDetails;
	}

	private Label getcompanies(JsonArray array, String title) {
		String names="<b>"+title+"</b>";
		String delim="";
		for(int i=0;i<array.length();i++){
			names +=delim+ array.getObject(i).getString("name");
			delim=", ";
		}
		Label l=CustomItems.htmlLabel(names);
		
		return l;
	}

	private Component getTrailerTab() {
		CssLayout trailers = new CssLayout();
		trailers.setWidth("1000px");
		for(int i=0;i<jsObject.getObject(VIDEOS).getArray(RESULTS).length();i++){
			Label l=CustomItems.htmlLabel("<iframe width='450' height='300' style='margin:15px;' src='"+YOUTUBE_TRAILER_ROOT+jsObject.getObject(VIDEOS).getArray(RESULTS).getObject(i).getString("key")+"' allowfullscreen ></iframe>");
			trailers.addComponent(l);
		}
		if(trailers.getComponentCount()<1){
			trailers.addComponent(new Label("No trailers available..."));
		}

		return trailers;
	}

	private Component getImageTab() {
		CssLayout images = new CssLayout();
		images.setWidth("1000px");
		
		for(int i=0;i<jsObject.getObject(IMAGES).getArray(POSTERS).length();i++){
			//jsObject.getObject(VIDEOS).getArray(RESULTS).getObject(i).getString("key")
			Image img=bodyLayout.getImage(jsObject.getObject(IMAGES).getArray(POSTERS).getObject(i), POSTER_IMAGE_300, true, true);
			
			//img.addStyleName("detailsImageMarginFix");
			images.addComponent(img);
		}
		if(images.getComponentCount()<1){
			images.addComponent(new Label("No trailers available..."));
		}
		return images;
	}
	private Component getCastTab() {
		VerticalLayout movieDetails = new VerticalLayout();
		movieDetails.addComponent(new Label("test4"));

		return movieDetails;
	}
	private Component getCrewTab() {
		VerticalLayout movieDetails = new VerticalLayout();
		movieDetails.addComponent(new Label("test5"));

		return movieDetails;
	}
	private Component getRecommendationTab() {
		VerticalLayout movieDetails = new VerticalLayout();
		movieDetails.addComponent(new Label("test6"));

		return movieDetails;
	}


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
