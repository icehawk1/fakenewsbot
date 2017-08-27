package de.mhaug.fakenewsbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Main {
	private final Set<Thread> threads;
	private List<SocialMedium> mediaList;
	private List<Faktenquelle> quellenliste;
	private WatsonFacade watson;
	public static final Logger logger = LoggerFactory.getLogger("fakenewsbot");


	public static void main(String[] args) throws InterruptedException {
		configureLogging();
		Main main = new Main();
		for(int i=0;i<Runtime.getRuntime().availableProcessors(); i++)
			main.startMonitoringForFakeNews();
		synchronized (main) {
			main.wait();
		}
		main.stop();
	}

	public Main() {
		mediaList = buildDatasources();
		quellenliste = buildFaktenQuellen();
		watson = connectToWatson();
		threads = new TreeSet<>();
	}

	private WatsonFacade connectToWatson() {
		return null;
	}

	private List<Faktenquelle> buildFaktenQuellen() {
		return null;
	}

	private List<SocialMedium> buildDatasources() {
		List<SocialMedium> result = new ArrayList<>();
		try {
			result.add(new TwitterFacade());
		} catch (TwitterException ex) {
			logger.warn("Konnte TwitterFacade nicht erzeugen", ex);
		}
		return result;
	}

	private void startMonitoringForFakeNews() {

	}

	private void stop() {
		for(SocialMedium medium:mediaList) medium.disconnect();
		for(Faktenquelle quelle:quellenliste) quelle.disconnect();
		watson.disconnect();
	}

	private static void configureLogging() {

	}
}
