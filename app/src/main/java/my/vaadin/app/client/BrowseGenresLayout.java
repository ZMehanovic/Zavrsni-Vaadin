package my.vaadin.app.client;

import static my.vaadin.app.client.Constants.*;

import java.awt.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.atmosphere.config.service.Singleton;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import elemental.json.JsonObject;

public class BrowseGenresLayout extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private BodyLayout bodyLayout;
	private String genreHistory = "";
	private Set<Integer> genreIds = new HashSet<>();
	private String genres;
	private int pageId;
	HashMap<Integer, String> genreMap = JsonSingleton.getInstance().getGenreMap();
	CssLayout genrePickerLayout = new CssLayout() {

		private static final long serialVersionUID = 1L;

		@Override
		protected String getCss(Component c) {
			if (c instanceof Button) {
				return "margin-left: 15px; margin-top: 10px;";
			}
			return super.getCss(c);
		}

	};

	
	public void loadData(BodyLayout bodyLayout, String genres, int page){
		this.bodyLayout = bodyLayout;
		this.genres = genres;
		this.pageId = page;
		
		setPickerLayout();
		if (genres != null) {
			genreHistory = NAVIGATION_GENRE_IDS + genres + NAVIGATION_RESULT_PAGE_NUMBER + pageId;
			fillData();
		}
		Page.getCurrent().pushState(NAVIGATION_BROWSE_PAGE + genreHistory);
	}
	
	private void fillData() {

		genreIds = Stream.of(genres.split(",")).map(Integer::parseInt).collect(Collectors.toSet());

		for (Integer i : genreIds) {
			selectionListener(genreMap.get(i), i, true);
		}
		browse();
	}

	private void browse() {
		if(getComponentCount()>1&&getComponent(1)!=null){
			removeComponent(getComponent(1));
		}
		JsonObject jsonObject = JsonSingleton.getInstance()
				.getJsonObjectFromURL(BROWSE_GENRE.replace(WITH_GENRES, WITH_GENRES+genres) + pageId);

		addComponent(new SearchLayout(bodyLayout, jsonObject, false, null, genres));

	}

	private void setPickerLayout() {

		genrePickerLayout.setWidth("1100px");
		ArrayList<String> genreNames = new ArrayList<>();

		genreMap.forEach((key, value) -> {
			genreNames.add(value);
		});
		genreNames.sort(String::compareToIgnoreCase);
		ComboBox<String> genreBrowse = new ComboBox<>();

		genreBrowse.addSelectionListener(e -> {
			final Integer idGenre = genreMap.entrySet().stream()
					.filter(entry -> Objects.equals(entry.getValue(), e.getValue())).map(Map.Entry::getKey).findFirst()
					.orElse(null);
			selectionListener(e.getValue(), idGenre, false);
			browse();
		});
		genreBrowse.setItems(genreNames);
		genrePickerLayout.addComponent(genreBrowse);
		addComponents(genrePickerLayout);

	}

	private String getGenreIds(Set<Integer> genresId) {
		String result = "";
		String delim = "";
		for (Integer id : genresId) {
			result += delim + id;
			delim = ",";
		}
		return result;
	}

	private void selectionListener(String name, int idGenre, boolean refresh) {

		if (refresh || !genreIds.contains(idGenre)) {
			Button selectedBtn = CustomItems.createComboBoxButtons(name);
			selectedBtn.addClickListener(event -> {
				selectedBtn.setVisible(false);
				genreIds.remove(idGenre);
				genres = getGenreIds(genreIds);
				browse();
				Page.getCurrent().pushState(NAVIGATION_BROWSE_PAGE + NAVIGATION_GENRE_IDS + genres
						+ NAVIGATION_RESULT_PAGE_NUMBER + pageId);
			});
			genrePickerLayout.addComponent(selectedBtn);

			genreIds.add(idGenre);
			genres = getGenreIds(genreIds);
			Page.getCurrent().pushState(
					NAVIGATION_BROWSE_PAGE + NAVIGATION_GENRE_IDS + genres + NAVIGATION_RESULT_PAGE_NUMBER + pageId);
		}
	}

}
