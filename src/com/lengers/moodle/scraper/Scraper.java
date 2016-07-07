package com.lengers.moodle.scraper;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;


import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
		// TODO Auto-generated method stub
		final OutputStream os = new FileOutputStream("questions.md");
		final PrintStream printStream = new PrintStream(os);
		
		Response res;
		try {
			
			System.out.println("[*] Downloading questions... \n");
			
			printStream.write((new String()).getBytes());
			
			res = Jsoup.connect("http://moodle.dhbw-mannheim.de/login/index.php").data("username", "s150614", "password", "pzXufEBL7").method(Method.POST).execute();
			
			//This will get you cookies
			 Map<String, String> loginCookies = res.cookies();
			 
			 Document doc = Jsoup.connect("http://moodle.dhbw-mannheim.de/course/view.php?id=3291")
				      .cookies(loginCookies)
				      .get();
			 
			 Elements links = doc.select("a[class]");
			 
			 for (Iterator<Element> iterator = links.iterator(); iterator.hasNext();) {
				Element element = iterator.next();
				if (element.text().contains("Exam")) {
					String followLink = element.attributes().get("href");
					Document followDoc = Jsoup.connect(followLink).cookies(loginCookies).get();
										
					Elements followLinks = followDoc.select("td[class]");
										
					for (Iterator<Element> followIterator = followLinks.iterator(); followIterator.hasNext();) {
						Element followElement = followIterator.next();
						if (followElement.attributes().get("class").contains("topic starter")) {
							Elements inner = followElement.select("a[href");
							String topicHeading = followElement.text();

							printStream.print("## " + followElement);
							for (Iterator<Element> innerIterator = inner.iterator(); innerIterator.hasNext();) {
								Element innerElement = innerIterator.next();
								String topicLink = innerElement.attributes().get("href");
								
								Document topicDoc = Jsoup.connect(topicLink).cookies(loginCookies).get();
								
								Elements topicData = topicDoc.select("div[class=\"posting fullpost\"]");
								
//								printStream.print("\n###");
								
								Element first = topicData.iterator().next();
								for (Iterator<Element> topicIterator = topicData.iterator(); topicIterator.hasNext();) {
									Element answer = topicIterator.next();
									if (answer == first) {
										String stringAnswer = answer.toString().substring(0, 31) + "### " + answer.toString().substring(31, answer.toString().length());
										System.out.println(answer);
										System.out.println(answer.data());
										printStream.print(stringAnswer);
									} else {
										printStream.print(answer);
									}
									printStream.println("\n---\n");
									
								}
								
							}
							printStream.println("\n---\n");
							
						}
						
					}
					
								
				}
			}
				printStream.close();	
				System.out.println("[*] Done. Questions written to file \"questions.md\".");
				

			 
//			 System.out.println(links);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("[!] Something funny happend. Abort.");
			e.printStackTrace();
		}

			 
	}

}
