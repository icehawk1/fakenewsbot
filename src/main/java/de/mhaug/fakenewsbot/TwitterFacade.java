package de.mhaug.fakenewsbot;

import twitter4j.*;

import java.util.Date;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class TwitterFacade extends SocialMedium {
	private static final int REFILL_SIZE = 2;
	private final Twitter twitter;
	/**
	 *
	 */
	private final PriorityQueue<Message> msgQueue;
	/**
	 * Hiermit merke ich mir welche Tweets bereits bearbeitet wurden, da kein Tweet zweimal bearbeitet werden sollte.
	 * Dies kann nicht in der Queue geschehen, da Tweets aus der Queue gelöscht werden, wenn sie bearbeitet werden sollen.
	 * Abzufragen ob ich bereits auf den Tweet geantwortet habe wäre ein zusätzlicher API-call und würde außerdem
	 * synchronization-issues verursachen.
	 */
	private final Set<Long> seenTweets;

	public TwitterFacade() throws TwitterException {
		twitter = TwitterFactory.getSingleton();
		msgQueue = new PriorityQueue<>();
		seenTweets = new HashSet<>();
	}

	@Override
	public synchronized Message retrieveNextMessage() {
		if (msgQueue.size() <= REFILL_SIZE) refillMessageQueue();
		return msgQueue.poll();
	}

	@Override
	public void disconnect() {
		// Twitter hat keine Cleanup-methoden
	}

	/**
	 * Füllt eine leer gewordene Queue mit einer neuen Query wieder auf.
	 * Diese Methode soll Networkoverhead minimieren, da sie nur bei Bedarf ausgeführt wird.
	 */
	private synchronized void refillMessageQueue() {
		Query query = new Query("flüchtlinge führerscheine");
		try {
			QueryResult result = twitter.search(query);
			for (Status tweet : result.getTweets()) {
				if (!seenTweets.contains(tweet.getId())) {
					Message msg = new TwitterMessage(tweet.getId(), "@" + tweet.getUser().getScreenName(), tweet.getText(), tweet.getCreatedAt(), tweet.getRetweetCount());
					msgQueue.offer(msg);
					seenTweets.add(tweet.getId());
				}
			}
		} catch (TwitterException ex) {
			Main.logger.warn("Konnte Twitterquery nicht ausführen: " + query, ex);
		}
	}
}

/**
 * Diese Klasse wird nur benötigt, weil PriorityQueue leider mit Comparable arbeitet anstatt extra Methoden für die
 * Übergabe der Priorität zu implementieren.
 */
class TwitterMessage extends Message implements Comparable<TwitterMessage> {
	private final int priority;

	public TwitterMessage(long id, String username, String text, Date createdAt, int priority) {
		super(id, username, text, createdAt);
		this.priority = priority;
	}

	@Override
	public int compareTo(TwitterMessage that) {
		return this.priority-that.priority;
	}
}