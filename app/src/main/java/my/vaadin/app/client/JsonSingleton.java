package my.vaadin.app.client;


import static my.vaadin.app.client.Constants.GENRES;
import static my.vaadin.app.client.Constants.LIST_OF_GENRES;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.impl.JsonUtil;

public class JsonSingleton {

	private JsonSingleton(){}
	
	private static class SingletonHelper{
		private static final JsonSingleton INSTANCE =new JsonSingleton();
	}
	
	public static JsonSingleton getInstance(){
		return SingletonHelper.INSTANCE;
	}
	

	private final HashMap<Integer, String> genreMap=getGenres();
	
	public JsonObject getJsonObjectFromURL(String linkURL){
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsObject;
	}
	
	private HashMap<Integer, String> getGenres() {
		// TODO Auto-generated method stub
		JsonArray jsObject=getJsonObjectFromURL(LIST_OF_GENRES).getArray(GENRES);
		HashMap<Integer, String> genresMap=new HashMap<>();
		for(int i=0;i<jsObject.length();i++){
			genresMap.put((int)jsObject.getObject(i).get("id").asNumber(), jsObject.getObject(i).get("name").asString());
		}
		return genresMap;
	}

	public ArrayList<String> getGenreList(int... genreKeys){
		ArrayList<String> genreList=new ArrayList<String>();
		for(int key: genreKeys){
			//Some keys might have been deleted by TMDB e.g 10769-Foreign -> https://www.themoviedb.org/talk/586ecd68c3a3683b6900aebc
			if(genreMap.get(key)!=null){
				genreList.add(genreMap.get(key).toString());
			}else{
				genreList.add("");
			}
		}
		return genreList;
	}

	public HashMap<Integer, String> getGenreMap() {
		return genreMap;
	}

	
	
}
