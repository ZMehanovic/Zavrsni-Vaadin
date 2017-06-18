package my.vaadin.app;

import static my.vaadin.app.Constants.CURRENT_PAGE;
import static my.vaadin.app.Constants.DESCRIPTION;
import static my.vaadin.app.Constants.GENRE_IDS;
import static my.vaadin.app.Constants.NAVIGATION_RESULT_PAGE_NUMBER;
import static my.vaadin.app.Constants.NAVIGATION_SEARCH_PAGE;
import static my.vaadin.app.Constants.POSTER_IMAGE_185;
import static my.vaadin.app.Constants.POSTER_PATH;
import static my.vaadin.app.Constants.RELEASE_DATE;
import static my.vaadin.app.Constants.RESULTS;
import static my.vaadin.app.Constants.TITLE;
import static my.vaadin.app.Constants.TOTAL_PAGES;
import static my.vaadin.app.Constants.VOTE_AVERAGE;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

public class SearchLayout extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private BodyLayout bodyLayout;
	private boolean isRecommendation;
	private String genreIds;
	private String searchQuery;

	public SearchLayout(BodyLayout bodyLayout, JsonObject jsonObject, boolean isRecommendation, String searchQuery,
			String genreIds) {
		this.bodyLayout = bodyLayout;
		this.isRecommendation = isRecommendation;
		this.genreIds = genreIds;
		this.searchQuery = searchQuery;
		setSearchLayout(jsonObject.getArray(RESULTS));

		int currentPage = (int) jsonObject.getNumber(CURRENT_PAGE);
		if (!isRecommendation && jsonObject.getNumber(TOTAL_PAGES) > 1) {
			//TODO check why TMDb is weird (12,18 not ok 18,12 is ok?!?) BROWSE
			resolvePagingLayout(currentPage, (int) jsonObject.getNumber(TOTAL_PAGES));

		}
		setWidth("1050px");
		if (genreIds == null) {
			Page.getCurrent()
					.pushState(NAVIGATION_SEARCH_PAGE + searchQuery + NAVIGATION_RESULT_PAGE_NUMBER + currentPage);
		}
	}

	private void resolvePagingLayout(int currentPage, int totalPages) {

		if (currentPage <= 3) {
			int endCounter = 6;
			if (totalPages < 5) {
				endCounter = totalPages + 1;
			}
			setPagingLayout(currentPage, totalPages, 1, endCounter);
		} else if (currentPage + 2 >= totalPages) {
			setPagingLayout(currentPage, totalPages, totalPages - 5, totalPages + 1);
		} else {
			setPagingLayout(currentPage, totalPages, currentPage - 2, currentPage + 3);
		}

	}

	private void setPagingLayout(int currentPage, int lastPage, int startCounter, int endCounter) {

		HorizontalLayout pagesLayout = new HorizontalLayout();
		if (currentPage != 1) {
			if (lastPage > 5 && currentPage > 3) {
				Button first = CustomItems.createBorderlessButton("First", false);
				first.addClickListener(e -> {
					openPage(1);
				});
				pagesLayout.addComponent(first);
			}

			Button previous = CustomItems.createBorderlessButton("Previous", false);
			previous.addClickListener(e -> {
				openPage(currentPage - 1);
			});
			pagesLayout.addComponents(previous);
		}
		for (int i = startCounter; i < endCounter; i++) {
			Button page = CustomItems.createBorderlessButton(i + "", false);
			final int pageNum = i;
			if (currentPage == i) {
				page.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			}

			page.addClickListener(e -> {
				openPage(pageNum);
			});
			pagesLayout.addComponent(page);
		}
		if (currentPage != lastPage) {

			Button next = CustomItems.createBorderlessButton("Next", false);

			next.addClickListener(e -> {
				openPage(currentPage + 1);
			});
			pagesLayout.addComponent(next);

			if (currentPage + 2 < lastPage) {
				Button last = CustomItems.createBorderlessButton("Last", false);
				last.addClickListener(e -> {
					openPage(lastPage);
				});
				pagesLayout.addComponent(last);
			}
		}

		addComponent(pagesLayout);
	}

	private void openPage(int pageNumber) {
		if (genreIds!=null) {
			bodyLayout.browseGenres(true, genreIds, pageNumber);
		} else {
			bodyLayout.searchForMovies(true, searchQuery, pageNumber);
		}
	}

	private void setSearchLayout(JsonArray jsArray) {

		for (int i = 0; i < jsArray.length(); i++) {
			JsonObject jsObject = jsArray.get(i);
			HorizontalLayout imageDetailsLayout = new HorizontalLayout();

			String poster = jsObject.get(POSTER_PATH).jsEquals(Json.createNull()) ? ""
					: jsObject.getString(POSTER_PATH);
			String title = jsObject.get(TITLE).jsEquals(Json.createNull()) ? "" : jsObject.getString(TITLE);
			String imageID = String.valueOf((int) jsObject.getNumber("id"));

			imageDetailsLayout.addComponents(
					CustomItems.getImage(poster, title, imageID, POSTER_IMAGE_185, false, bodyLayout),
					getMovieDetails(jsObject));
			imageDetailsLayout.addStyleName("searchLayoutBorders");
			addComponent(imageDetailsLayout);
		}

	}

	private VerticalLayout getMovieDetails(JsonObject jsObject) {
		VerticalLayout movieDetailsLayout = new VerticalLayout();

		HorizontalLayout detailsHeaderLayout = new HorizontalLayout();
		Label title = new Label("Title: " + jsObject.getString(TITLE));
		HorizontalLayout vote = CustomItems.avgVoteStarLayout(String.valueOf(jsObject.getNumber(VOTE_AVERAGE)));
		detailsHeaderLayout.addComponents(title, vote);

		Label description = CustomItems.descriptionLabel(jsObject.getString(DESCRIPTION), isRecommendation);

		HorizontalLayout detailsFooterLayout = new HorizontalLayout();

		HorizontalLayout genres = CustomItems.genresLayout(jsObject.getArray(GENRE_IDS), false);

		Label releaseDate = new Label("Release Date: " + jsObject.getString(RELEASE_DATE));
		detailsFooterLayout.addComponents(genres, releaseDate);

		Button moreDetails = new Button("More details...");
		moreDetails.setStyleName(ValoTheme.BUTTON_LINK);
		moreDetails.addClickListener(e->{
			bodyLayout.showMovieDetails((int) jsObject.getNumber("id"));
		});

		movieDetailsLayout.addComponents(detailsHeaderLayout, description, detailsFooterLayout, moreDetails);
		return movieDetailsLayout;
	}

}
