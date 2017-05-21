package my.vaadin.app;

import java.io.Serializable;
import java.util.ArrayList;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

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

	public static Label descriptionLabel(String overview) {

		Label description = new Label(overview);
		description.setWidth("800px");

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

}
