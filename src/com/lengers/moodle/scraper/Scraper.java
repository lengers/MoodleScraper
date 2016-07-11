package com.lengers.moodle.scraper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
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

		// download(null, null, null);

		if (args.length < 1) {
			help();
		} else if (args[0].contains("-h")) {
			help();
		} else if (args.length >= 2) {
			download(args[0], args[1], args[2]);
		} else {
			System.out.println(args[0]);
		}

	}

	private static void help() {
		System.out.println("A program to extract forum topics from the Moodle platform. \n\n"
				+ "Usage: MoodleScraper-jar [-h] [USERNAME] [PASSWORD] [URL]\n\n" + "Options: \n"
				+ "    -h   Display this help");
	}

	private static void download(String username, String password, String url) throws FileNotFoundException {
		final OutputStream os = new FileOutputStream("questions.md");
		final PrintStream printStream = new PrintStream(os);

		Response res;
		try {

			System.out.println("[*] Downloading questions... \n");

			printStream.write((new String()).getBytes());

			res = Jsoup.connect("http://moodle.dhbw-mannheim.de/login/index.php")
					.data("username", username, "password", password).method(Method.POST).execute();

			// This will get you cookies
			Map<String, String> loginCookies = res.cookies();

			// http://moodle.dhbw-mannheim.de/course/view.php?id=3291
			Document doc = Jsoup.connect(url).cookies(loginCookies).get();

			Elements links = doc.select("a[class]");

			for (Iterator<Element> iterator = links.iterator(); iterator.hasNext();) {
				Element element = iterator.next();
				if (element.text().contains("Exam Questions")) {
					String followLink = element.attributes().get("href");
					Document followDoc = Jsoup.connect(followLink).cookies(loginCookies).get();

					Elements followLinks = followDoc.select("td[class]");

					for (Iterator<Element> followIterator = followLinks.iterator(); followIterator.hasNext();) {
						Element followElement = followIterator.next();
						if (followElement.attributes().get("class").contains("topic starter")) {
							Elements inner = followElement.select("a[href");

							printStream.print("## " + followElement);
							for (Iterator<Element> innerIterator = inner.iterator(); innerIterator.hasNext();) {
								Element innerElement = innerIterator.next();
								String topicLink = innerElement.attributes().get("href");

								Document topicDoc = Jsoup.connect(topicLink).cookies(loginCookies).get();

								Elements topicData = topicDoc.select("div[class=\"posting fullpost\"]");

								// printStream.print("\n###");

								Element first = topicData.iterator().next();
								for (Iterator<Element> topicIterator = topicData.iterator(); topicIterator.hasNext();) {
									Element answer = topicIterator.next();
									if (answer == first) {
										String stringAnswer = answer.toString().substring(0, 31) + "### "
												+ answer.toString().substring(31, answer.toString().length());
										printStream.print(stringAnswer);
									} else {
										printStream.print(answer);
									}
									printStream.println("\n---\n");

								}

							}

						}

					}

				}
			}
			printStream.close();
			System.out.println("[*] Done. Questions written to file \"questions.md\".");

			// System.out.println(links);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("[!] Something funny happend. Abort.");
			e.printStackTrace();
		}
	}

}
