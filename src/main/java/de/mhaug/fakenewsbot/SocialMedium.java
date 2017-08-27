package de.mhaug.fakenewsbot;

public abstract class SocialMedium implements ConnectableService {
	public abstract Message retrieveNextMessage();
}
