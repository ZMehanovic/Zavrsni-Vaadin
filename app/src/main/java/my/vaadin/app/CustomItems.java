package my.vaadin.app;

import static my.vaadin.app.Constants.POSTER_IMAGE_185;
import static my.vaadin.app.Constants.POSTER_IMAGE_300;
import static my.vaadin.app.Constants.POSTER_IMAGE_500;
import static my.vaadin.app.Constants.POSTER_IMAGE_ORIGINAL;

import java.io.Serializable;
import java.util.ArrayList;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import elemental.json.JsonArray;

public class CustomItems implements Serializable {

	private static final long serialVersionUID = 1L;

	public static HorizontalLayout avgVoteStarLayout(String title) {
		HorizontalLayout voteLayout = new HorizontalLayout();

		Label voteLabel = new Label(title);

		Image starImg = new Image();
		starImg.setIcon(VaadinIcons.STAR);

		voteLayout.addComponents(voteLabel, starImg);
		return voteLayout;
	}

	public static Label descriptionLabel(String overview, boolean isRecommendation) {

		Label description = new Label(overview);
		description.setWidthUndefined();
		if(isRecommendation){
		description.setWidth("700px");
		}else{
			description.setWidth("800px");
		}
//		description.width

		return description;
	}

	public static HorizontalLayout genresLayout(JsonArray genreIDs, boolean isFromDetails) {
		HorizontalLayout genreLayout = new HorizontalLayout();
		if (genreIDs == null || genreIDs.length() == 0) {
			genreLayout.addComponent(new Label("No genres found!"));
			return genreLayout;
		}
		int[] genreIds = new int[genreIDs.length()];
		ArrayList<String> genresList = new ArrayList<>();
		for (int i = 0; i < genreIDs.length(); i++) {
			if (isFromDetails) {
				genreIds[i] = (int) genreIDs.getObject(i).getNumber("id");
				genresList.add(genreIDs.getObject(i).getString("name"));
			} else {
				genreIds[i] = (int) genreIDs.getNumber(i);
			}
		}
		if (!isFromDetails) {
			genresList = JsonSingleton.getInstance().getGenreList(genreIds);
		}
		for (String genre : genresList) {
			Label l = new Label(genre);
			// TODO click handler for genre browsing
			genreLayout.addComponent(l);
		}

		return genreLayout;
	}
	
	public static Label htmlLabel(String text){
		Label l=new Label(text);
		l.setContentMode(ContentMode.HTML);
		return l;
	}
	
	public static Image getImage(String poster, String title, String imageID, String imageSize, boolean isFromDetails, BodyLayout bodyLayout){


		Image img = new Image();
		
		
		if (poster==null||poster.isEmpty()) {
			img.setSource(new ThemeResource("images/Default_Image.jpg"));
		} else {
			img.setSource(new ExternalResource(imageSize + poster));
		}
		setImageSize(img, imageSize);
		
		img.setId(imageID);
		img.setDescription(title);
		img.addClickListener(e -> {
			if (!isFromDetails) {
				bodyLayout.showMovieDetails(Integer.parseInt(img.getId()));
			}
		});
		if (isFromDetails) {
			BrowserWindowOpener imgOpener = new BrowserWindowOpener(POSTER_IMAGE_ORIGINAL + poster);
			imgOpener.setWindowName("_blank");
			imgOpener.extend(img);
		}
		return img;
	}
	
	private static void setImageSize(Image img, String imageSize) {
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
	
	public static Button createBorderlessButton(String text,boolean selected){
		Button btn = new Button(text);
		if (selected) {
			btn.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		} else {
			btn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		}
		return btn;
		
	}
	public static Button createComboBoxButtons(String text){
		Button btn = new Button(text);
		btn.setIcon(VaadinIcons.CLOSE_SMALL);
		btn.setStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		
		return btn;
		
	}
}
