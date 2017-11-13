package com.weekendesk.anki;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Scanner;
import java.util.regex.Pattern;

import domain.Box.Color;
import domain.Card;
import domain.Session;
import service.SessionService;

public class Anki {

	private SessionService sessionService = new SessionService();

	public void start() {

		AnkiProperties.loadProperties("config/anki.properties");

		// check if there is session file and load it if there is one
		Session session = sessionService.loadSession();

		if (session == null || session.getSessionDate() == null) {
			System.out.println("Welcome to Anki, this is your first session. Let's load the cards...");
			if ((session = sessionService.createFirstSession()) == null)
				return;
		} else if (session.getSessionDate().isEqual(Util.getToday())) {
			System.out.println("Welcome back! Let's try again today");
		} else {
			System.out.println("Welcome back! Loading last session...");
			sessionService.updateSession(session);
		}

		session.setSessionDate(Util.getToday());
		if (session.getBox(Color.RED) == null || session.getBox(Color.RED).getCards().size() == 0) {
			System.out.println("No more cards today");
		} else {
			Deque<Card> errorCards = this.play(session);
			session.getBox(Color.RED).setCards(errorCards);
		}
		sessionService.storeSession(session);

	}

	public Deque<Card> play(Session session) {

		// ask questions from the red box
		Card currentCard = null;
		Scanner scanner = new Scanner(System.in);
		scanner.useDelimiter(Pattern.compile("[\\r\\n;]+"));
		Deque<Card> errorCards = new ArrayDeque<>();

		try {
			while ((currentCard = session.getNextCard(Color.RED)) != null) {

				System.out.println("Question: " + currentCard.getQuestion());
				System.out.print("Press any key to show the answer... ");
				scanner.nextLine();
				System.out.println("Answer: " + currentCard.getAnswer());
				char result = Util.readValidInput(
						"Did you get it right? Y (completelly) P (Partially) N (Not at all): ",
						Arrays.asList(new Short[] { 'y', 'p', 'n' }), scanner);
				switch (result) {
				case 'n':
					errorCards.addLast(currentCard);
					break;
				case 'y':
					session.addCard(Color.GREEN, currentCard);
					break;
				case 'p':
					session.addCard(Color.ORANGE, currentCard);
					break;
				}
				;
			}
		} finally {
			scanner.close();
		}

		if ((session.getBoxCards(Color.ORANGE) == null || session.getBoxCards(Color.ORANGE).size() == 0)
				&& (session.getBoxCards(Color.RED) == null || session.getBoxCards(Color.RED).size() == 0))
			System.out.println("All questions correctly answered. Congratulations!");
		else
			System.out.println("No more questions for you today. See you later");

		return errorCards;

	}

}
