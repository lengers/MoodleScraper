package com.lengers.moodle.scraper;


import java.io.IOException;
import java.util.Map;


import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Scraper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Response res;
		try {
			res = Jsoup
				    .connect("http://moodle.dhbw-mannheim.de/login/index.php")
				    .data("username", "s150614", "password", "pzXufEBL7")
				    .method(Method.POST)
				    .execute();
			
			//This will get you cookies
			 Map<String, String> loginCookies = res.cookies();
			 
			 Document doc = Jsoup.connect("http://moodle.dhbw-mannheim.de/mod/forum/view.php?id=67295")
				      .cookies(loginCookies)
				      .get();
			 
			 System.out.println(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			 
	}

}
