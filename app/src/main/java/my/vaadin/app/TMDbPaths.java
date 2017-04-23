package my.vaadin.app;

public final class TMDbPaths {

	private final static String API_KEY = "&api_key=7369e06a600e15f985316d3189192793&page=1";
	private final static String ROOT = "https://api.themoviedb.org";
	private final static String IMAGE_PATH = "http://image.tmdb.org";

	public final static String POPULAR_MOVIES = ROOT + "/3/discover/movie?sort_by=popularity.desc" + API_KEY;
	public final static String POSTER_IMAGE185 = IMAGE_PATH + "/t/p/w185/";
	public final static String MOVIE_SEARCH = ROOT + "/3/search/movie?" + API_KEY.substring(1);
	public final static String QUERY = "&query=";
	public static final String PAGEID = "&page=";

	public static final String RESULTS = "results";
	public static final String TOTAL_RESULTS = "total_results";
	public static final String TOTAL_PAGES = "total_pages";
	public static final String TITLE="title";
	public static final String POSTER_PATH = "poster_path";
	public static final String VOTE_COUNT="vote_count";
	public static final String VOTE_AVERAGE= "vote_average";
	public static final String DESCRIPTION= "overview";
	public static final String RELEASE_DATE= "release_date";

	private TMDbPaths() {

	}
}
