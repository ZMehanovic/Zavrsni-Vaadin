package my.vaadin.app;

import static my.vaadin.app.TMDbPaths.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

import java.lang.Object;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import elemental.json.impl.JsonUtil;

public class BodyLayout extends FormLayout {

	private static final long serialVersionUID = 1L;
	
	private MyUI myUI;
	
	public BodyLayout(MyUI myUI){
		this.myUI=myUI;
		getStartBody();
	}

	private void getStartBody() {
		removeAllComponents();
//		JsonValue json=parse
//		JsonObject jsObject= JsonUtil.parse(popularMovies);
//		myUI.showNotification(jsObject.get("results").toString());
		JsonObject jsObject = getJsonObjectFromURL(POPULAR_MOVIES);

		if (jsObject!=null){
//			String str= TMDbPaths.imagePoster;
			
			addComponent(getPopularMoviesPosterGrid(jsObject.getArray(RESULTS)));
			
		}else{
			Notification.show("Error fetching data...",Type.ERROR_MESSAGE);
		}
		
	}

	private JsonObject getJsonObjectFromURL(String linkURL){
		JsonObject jsObject=null;
		try {
			URL myservice = new URL(linkURL);

			InputStream openStream = myservice.openStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openStream));
			String line;
			String str="";
			
			while ((line = bufferedReader.readLine()) != null) {
				str+=line;
			}
			jsObject= JsonUtil.parse(str);
//			myUI.showNotification(jsObject.getArray("results").getObject(0).getString("overview"));
//			myUI.showNotification(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsObject;
	}
	
	private GridLayout getPopularMoviesPosterGrid(JsonArray jsResults) {
		GridLayout popularMoviesGrid = new GridLayout(5, 8);

		for (int rowCounter = 0, i = 0; rowCounter < popularMoviesGrid.getRows(); rowCounter += 2) {
			for (int columnCounter = 0; columnCounter < popularMoviesGrid.getColumns(); columnCounter++, i++) {
				
				
				Label movieTitle=new Label((i+1)+". "+jsResults.getObject(i).getString(TITLE));
				movieTitle.setWidth(185, Unit.PIXELS);
				
				
				
				popularMoviesGrid.addComponent(getImage(jsResults.getObject(i)), columnCounter, rowCounter);
				popularMoviesGrid.addComponent(movieTitle, columnCounter, rowCounter + 1);
			}
		}
		popularMoviesGrid.setSpacing(true);
		return popularMoviesGrid;
	}
	
	private Image getImage(JsonObject jsObject){

		String poster = jsObject.get(POSTER_PATH).jsEquals(Json.createNull())?"":jsObject.getString(POSTER_PATH);

		String title = jsObject.getString(TITLE);
//		
		Image img = new Image();
		if (poster.isEmpty()) {
			img.setSource(new ThemeResource("images/Default_Image.jpg"));
		} else {
			img.setSource(new ExternalResource(POSTER_IMAGE185 + poster));
		}
		img.setWidth("185px");
		img.setHeight("278px");
		
		img.setDescription(title);
		return img;
	}
	
	public void searchForMovies(String searchString,int page){
		Notification.show(searchString);
		if(searchString==null||searchString.isEmpty()){
			getStartBody();
		}else{
			String url=MOVIE_SEARCH+QUERY+searchString+PAGEID+page;
			JsonObject jsObject= getJsonObjectFromURL(url);
			if(jsObject!=null&&jsObject.getNumber(TOTAL_RESULTS)>0){
				removeAllComponents();
//				int resultNumber=;
//				Notification.show(""+resultNumber);
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
			imageDetailsLayout.addComponents(getImage(jsObject),getMovieDetails(jsObject));
			
			imageDetailsLayout.addStyleName("searchLayoutBorders");
			searchLayout.addComponent(imageDetailsLayout);
		}
		
		return searchLayout;
		
	}

	private VerticalLayout getMovieDetails(JsonObject jsObject) {
		VerticalLayout movieDetailsLayout=new VerticalLayout();
		
		HorizontalLayout detailsHeaderLayout=new HorizontalLayout();
		Label title=new Label("Title: "+jsObject.getString(TITLE));
		Label vote=new Label("Average vote: "+jsObject.getNumber(VOTE_AVERAGE));
		detailsHeaderLayout.addComponents(title,vote);
		
		Label description=new Label(jsObject.getString(DESCRIPTION));
		description.setWidth("800px");
		HorizontalLayout detailsFooterLayout=new HorizontalLayout();
//		Label genres=new Label(jsObject.getString(TITLE));
		Label releaseDate=new Label("Release Date: "+jsObject.getString(RELEASE_DATE));
		detailsFooterLayout.addComponents(releaseDate);
		
		
		movieDetailsLayout.addComponents(detailsHeaderLayout,description,detailsFooterLayout);
		return movieDetailsLayout;
	}
	
	
}
