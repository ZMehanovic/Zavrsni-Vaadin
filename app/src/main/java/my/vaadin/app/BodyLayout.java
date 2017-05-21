package my.vaadin.app;

import static my.vaadin.app.TMDbPaths.*;

import java.util.ArrayList;

import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import my.vaadin.app.DetailsLayout.DetailsLayoutBuilder;

public class BodyLayout extends FormLayout {

	private static final long serialVersionUID = 1L;
	
	private MyUI myUI;
	
	public BodyLayout(MyUI myUI){
		this.myUI=myUI;
		getStartBody();
	}

	private void getStartBody() {
		removeAllComponents();
		JsonObject jsObject = JsonSingleton.getInstance().getJsonObjectFromURL(POPULAR_MOVIES);

		if (jsObject!=null){
			
			addComponent(getPopularMoviesPosterGrid(jsObject.getArray(RESULTS)));
			
		}else{
			Notification.show("Error fetching data...",Type.ERROR_MESSAGE);
		}
		
	}


	
	private GridLayout getPopularMoviesPosterGrid(JsonArray jsResults) {
		GridLayout popularMoviesGrid = new GridLayout(5, 8);

		for (int rowCounter = 0, i = 0; rowCounter < popularMoviesGrid.getRows(); rowCounter += 2) {
			for (int columnCounter = 0; columnCounter < popularMoviesGrid.getColumns(); columnCounter++, i++) {
				
				
				Label movieTitle=new Label((i+1)+". "+jsResults.getObject(i).getString(TITLE));
				movieTitle.setWidth(185, Unit.PIXELS);
				
				
				
				popularMoviesGrid.addComponent(getImage(jsResults.getObject(i), POSTER_IMAGE_185,false), columnCounter, rowCounter);
				popularMoviesGrid.addComponent(movieTitle, columnCounter, rowCounter + 1);
			}
		}
		popularMoviesGrid.setSpacing(true);
		return popularMoviesGrid;
	}
	
	public Image getImage(JsonObject jsObject, String imageSize, boolean isFromDetails){

		String poster = jsObject.get(POSTER_PATH).jsEquals(Json.createNull())?"":jsObject.getString(POSTER_PATH);
		String title = jsObject.getString(TITLE);

		Image img = new Image();
		
		
		if (poster.isEmpty()) {
			img.setSource(new ThemeResource("images/Default_Image.jpg"));
		} else {
			img.setSource(new ExternalResource(imageSize + poster));
		}
		setImageSize(img, imageSize);
		
		img.setId(String.valueOf((int)jsObject.getNumber("id")));
		img.setDescription(title);
		img.addClickListener(e -> {
			if (isFromDetails) {
				//TODO open original image size (Popup??)
			} else {
				removeAllComponents();
				addComponent(new DetailsLayout.DetailsLayoutBuilder(this, Integer.parseInt(img.getId())).appendVideos(true).build());
			}
		});
	
		return img;
	}
	
	private void setImageSize(Image img, String imageSize) {
		if(imageSize.equals(POSTER_IMAGE_185)){
			img.setWidth("185px");
			img.setHeight("278px");
		}else if(imageSize.equals(POSTER_IMAGE_300)){
			img.setWidth("300px");
			img.setHeight("450px");
		}else if(imageSize.equals(POSTER_IMAGE_500)){
			img.setWidth("500px");
			img.setHeight("750px");
		}else{
			img.setWidth("*");
			img.setHeight("*");
		}
		
	}

	public void searchForMovies(String searchString,int page){
		if(searchString==null||searchString.isEmpty()){
			getStartBody();
		}else{
			//TODO JSON singleton
			String url=MOVIE_SEARCH+QUERY+searchString+PAGEID+page;
			JsonObject jsObject= JsonSingleton.getInstance().getJsonObjectFromURL(url);
			if(jsObject!=null&&jsObject.getNumber(TOTAL_RESULTS)>0){
				removeAllComponents();
				addComponent(getSearchLayout(jsObject.getArray(RESULTS)));
				
			}else{
				Notification.show("Data not found.",Notification.Type.ERROR_MESSAGE);
			}
		}
	}
	
	private VerticalLayout getSearchLayout(JsonArray jsArray){
		VerticalLayout searchLayout=new VerticalLayout();
		for(int i=0;i<jsArray.length();i++){
			JsonObject jsObject=jsArray.get(i);
			HorizontalLayout imageDetailsLayout=new HorizontalLayout();
			imageDetailsLayout.addComponents(getImage(jsObject,POSTER_IMAGE_185, false),getMovieDetails(jsObject));
			imageDetailsLayout.addStyleName("searchLayoutBorders");
			searchLayout.addComponent(imageDetailsLayout);
		}
		
		return searchLayout;
		
	}

	private VerticalLayout getMovieDetails(JsonObject jsObject) {
		VerticalLayout movieDetailsLayout=new VerticalLayout();
		
		HorizontalLayout detailsHeaderLayout=new HorizontalLayout();
		Label title=new Label("Title: "+jsObject.getString(TITLE));
		HorizontalLayout vote=CustomItems.avgVoteStarLayout(String.valueOf(jsObject.getNumber(VOTE_AVERAGE)));
		detailsHeaderLayout.addComponents(title,vote);
		
		Label description=CustomItems.descriptionLabel(jsObject.getString(DESCRIPTION));

		HorizontalLayout detailsFooterLayout=new HorizontalLayout();

//		Label genres=new Label(getGenres(jsObject.getArray(GENRE_IDS)));
		HorizontalLayout genres=CustomItems.genresLayout(jsObject.getArray(GENRE_IDS), false);
		
		Label releaseDate=new Label("Release Date: "+jsObject.getString(RELEASE_DATE));
		detailsFooterLayout.addComponents(genres,releaseDate);
		
		movieDetailsLayout.addComponents(detailsHeaderLayout,description,detailsFooterLayout);
		return movieDetailsLayout;
	}

	
	
	
}
