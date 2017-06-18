package my.vaadin.app;

public enum PageIds {
	START(1),
	SEARCH(2),
	MOVIE_DETAILS(3),
	BROWSE(4);

	private int pageId;
	
	PageIds(int pageId) {
		this.pageId=pageId;
	}
	public int getPageId(){
		return pageId;
	}
	
}
