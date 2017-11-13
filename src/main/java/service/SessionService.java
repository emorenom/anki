package service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.weekendesk.anki.AnkiProperties;
import com.weekendesk.anki.Util;

import domain.Box;
import domain.Box.Color;
import domain.BoxCard;
import domain.Card;
import domain.Session;

public class SessionService {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public Session loadSession() {

		final Session newSession = new Session();
		try (Stream<String> stream = Files.lines(Paths.get(AnkiProperties.getProperty("sessionFile")))) {

			stream.forEach(temp -> {
				if (temp.startsWith("Date:")) {
					LocalDate sessionDate = LocalDate.parse(temp.split(":")[1].trim(), formatter);
					newSession.setSessionDate(sessionDate);
				} else {
					String[] cardInfo = temp.split("\\|");
					if (cardInfo.length == 3) {
						Card card = new Card(cardInfo[1].trim(), cardInfo[2].trim());
						String boxColor = cardInfo[0].trim();
						newSession.addCard(Box.Color.valueOf(boxColor.toUpperCase()), card);
					}
				}
			});

		} catch (IOException e) {
			return null;
		}

		return newSession;

	}

	@SuppressWarnings("unchecked")
	public Session createFirstSession() {

		final Session newSession = new Session();
		newSession.setSessionDate(Util.getToday());

		Box redBox = new Box(Color.RED);
		newSession.addBox(redBox);

		Path cardPath = Paths.get(AnkiProperties.getProperty("externalCardFile"));
		if (Files.notExists(cardPath))
			try {
				cardPath = Paths
						.get(ClassLoader.getSystemResource(AnkiProperties.getProperty("internalCardFile")).toURI());
			} catch (URISyntaxException e1) {
			}

		try (Stream<String> stream = Files.lines(cardPath)) {

			stream.skip(1).forEach(temp -> {
				String[] cardInfo = temp.split("\\|");
				if (cardInfo.length == 2) {
					Card card = new Card(cardInfo[0].trim(), cardInfo[1].trim());
					newSession.addCard(Color.RED, card);
				}
			});

		} catch (IOException e) {
			System.out.println("No cards were loaded");
			return null;
		}

		return newSession;

	}

	public void updateSession(Session session) {

		if (session.getBoxCards(Color.ORANGE) != null)
			session.getBoxCards(Color.ORANGE).forEach(c -> session.addCard(Color.RED, c));
		session.replaceBox(Color.ORANGE, session.getBox(Color.GREEN));
		session.removeBox(Color.GREEN);

	}

	public void storeSession(Session session) {

		List<String> cardStream = session.getBoxes().values().stream().flatMap(a -> a.getCards().stream().map(b -> {
			return new BoxCard(a.getId().getValue(), b.getQuestion(), b.getAnswer());
		})).map(x -> x.toString()).collect(Collectors.toList());

		try {
			String dateLine = "Date: " + formatter.format(session.getSessionDate()) + "\n";
			Files.write(Paths.get(AnkiProperties.getProperty("sessionFile")), dateLine.getBytes(),
					StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			Files.write(Paths.get(AnkiProperties.getProperty("sessionFile")), cardStream, StandardOpenOption.WRITE,
					StandardOpenOption.APPEND);
		} catch (IOException e) {
		}

	}

}
