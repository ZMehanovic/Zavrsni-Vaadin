package my.vaadin.app;

import java.io.Serializable;

public final class TMDbPaths implements Serializable {

	private static final long serialVersionUID = 1L;

	private final static String API_KEY = "api_key=7369e06a600e15f985316d3189192793&page=1";
	private final static String ROOT = "https://api.themoviedb.org/3";
	private final static String IMAGE_PATH = "https://image.tmdb.org/t/p";

	public final static String IMDB_LINK="http://www.imdb.com/title/";
	public final static String YOUTUBE_TRAILER_ROOT = "//www.youtube.com/embed/";
	public final static String POPULAR_MOVIES = ROOT + "/discover/movie?sort_by=popularity.desc&" + API_KEY;
	public final static String POSTER_IMAGE_185 = IMAGE_PATH + "/w185/";
	public final static String POSTER_IMAGE_300 = IMAGE_PATH + "/w300/";
	public final static String POSTER_IMAGE_500 = IMAGE_PATH + "/w500/";
	public final static String POSTER_IMAGE_ORIGINAL = IMAGE_PATH + "/original/";
	public final static String MOVIE_SEARCH = ROOT + "/search/movie?" + API_KEY;
	public final static String QUERY = "&query=";
	public final static String PAGEID = "&page=";
	public final static String LIST_OF_GENRES = ROOT + "/genre/list?" + API_KEY;
	public final static String REPLACE_STRING = "REPLACE";
	public final static String MOVIE_DETAILS = ROOT + "/movie/" + REPLACE_STRING + "?" + API_KEY;
	public final static String APPEND_TO_RESPONSE="&append_to_response=";
  
	public final static String IMDB_ID="imdb_id";
	public final static String RESULTS = "results";
	public final static String TOTAL_RESULTS = "total_results";
	public final static String TOTAL_PAGES = "total_pages";
	public final static String TITLE = "title";
	public final static String ORIGINAL_TITLE = "original_title";
	public final static String POSTER_PATH = "poster_path";
	public final static String VOTE_COUNT = "vote_count";
	public final static String VOTE_AVERAGE = "vote_average";
	public final static String DESCRIPTION = "overview";
	public final static String RELEASE_DATE = "release_date";
	public final static String GENRE_IDS = "genre_ids";
	public final static String GENRES = "genres";
	public final static String VIDEOS = "videos";
	

	public final static String MOVIE_HOME_PAGE = "homepage";
	public final static String MOVIE_STATUS = "status";
	public final static String MOVIE_RELEASE_DATE = "release_date";
	public final static String MOVIE_RUNTIME = "runtime";
	public final static String MOVIE_COLLECTION = "belongs_to_collection";
	public final static String MOVIE_PRODUCTION_COMPANY = "production_companies";
	public final static String MOVIE_PRODUCTION_COUNTRY = "production_countries";
	public final static String MOVIE_SPOKEN_LANGUAGE = "spoken_languages";
	public final static String MOVIE_BUDGET = "budget";
	public final static String MOVIE_REVENUE = "revenue";

	private TMDbPaths() {

	}
}
