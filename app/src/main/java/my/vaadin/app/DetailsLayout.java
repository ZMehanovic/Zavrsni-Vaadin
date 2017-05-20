package my.vaadin.app;

import static my.vaadin.app.TMDbPaths.*;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import elemental.json.JsonObject;


public class DetailsLayout extends FormLayout{
	
	private static final long serialVersionUID = 1L;

	private JsonObject jsObject;
	private BodyLayout bodyLayout;
	private int movieID;
	
	private boolean appendVideos;
	private boolean appendSimilar;
	private boolean appendCredits;
	
	public static class DetailsLayoutBuilder{
		private BodyLayout bodyLayout;
		private int movieID;
		
		private boolean appendVideos;
		private boolean appendSimilar;
		private boolean appendCredits;
		
		public DetailsLayoutBuilder(BodyLayout bodyLayout, int movieID){
			this.bodyLayout=bodyLayout;
			this.movieID=movieID;
		}
		public DetailsLayoutBuilder appendVideos(boolean appendVideos){
			this.appendVideos=appendVideos;
			return this;
		}
		public DetailsLayoutBuilder appendSimilar(boolean appendSimilar){
			this.appendSimilar=appendSimilar;
			return this;
		}
		public DetailsLayoutBuilder appendCredits(boolean appendCredits){
			this.appendCredits=appendCredits;
			return this;
		}
		public DetailsLayout build(){
			return new DetailsLayout(this);
		}
		
	}
	

	private DetailsLayout(DetailsLayoutBuilder builder) {
		this.bodyLayout=builder.bodyLayout;
		this.movieID=builder.movieID;
		
		this.appendVideos=builder.appendVideos;
		this.appendSimilar=builder.appendSimilar;
		this.appendCredits=builder.appendCredits;
		
		String append=APPEND_TO_RESPONSE;
		String delimiter="";
		if(appendVideos){
			append+="videos";
			delimiter=",";
		}
		if(appendSimilar){
			append+=delimiter+"similar";
			delimiter=",";
		}
		if(appendCredits){
			append+=delimiter+"credits";
			delimiter=",";
		}
		this.jsObject=JsonSingleton.getInstance().getJsonObjectFromURL(MOVIE_DETAILS.replace(REPLACE_STRING, ""+movieID)+append);
		setMargin(false);
		setSpacing(false);
		addComponent(getMovieDetails());
	}

	private Component getMovieDetails() {
		VerticalLayout vlLayout=new VerticalLayout();
		HorizontalLayout movieDetailsLayout=new HorizontalLayout();
		
		Label movieName=new Label(jsObject.getString(TITLE));
		movieName.addStyleName("h1LabelStyle");
		
		Image movieImage=bodyLayout.getImage(jsObject, POSTER_IMAGE_300, true);
		
		Button imdbLink=new Button("IMDB Link");
		imdbLink.setStyleName(ValoTheme.BUTTON_LINK);
		imdbLink.addClickListener(e->{
			getUI().getPage().open(IMDB_LINK+jsObject.getString(IMDB_ID), "_blank");
		});
		

		movieDetailsLayout.addComponents(movieImage,imdbLink);
		
		vlLayout.addComponents(movieName,movieDetailsLayout);

		
		return vlLayout;
	}

}
