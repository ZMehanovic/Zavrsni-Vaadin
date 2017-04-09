package my.vaadin.app;

import static my.vaadin.app.TMDbPaths.popularMovies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.vaadin.ui.FormLayout;

import elemental.json.JsonObject;
import elemental.json.impl.JsonUtil;

public class BodyLayout extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8049479056392735234L;

	private MyUI myUI;
	
	public BodyLayout(MyUI myUI){
		this.myUI=myUI;
		getStartBody();
	}

	private void getStartBody() {
		
//		JsonValue json=parse
//		JsonObject jsObject= JsonUtil.parse(popularMovies);
//		myUI.showNotification(jsObject.get("results").toString());
		URL myservice;
		try {
			myservice = new URL(popularMovies);

			InputStream openStream = myservice.openStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openStream));
			String line;
			String str="";
			
			while ((line = bufferedReader.readLine()) != null) {
				str+=line;
			}
			JsonObject jsObject= JsonUtil.parse(str);
//			myUI.showNotification(jsObject.getArray("results").getObject(0).getString("overview"));
//			myUI.showNotification(str);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	
}
