package com.weekendesk.anki;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import domain.Box.Color;
import domain.Card;
import domain.Session;
import service.SessionService;

public class SessionServiceTest {

	SessionService sessionService = new SessionService();

	@Before
	public void setUp() throws Exception {
		AnkiProperties.loadProperties("config/anki.test.properties");
		String cardsContent = "card question | card answer\nquestion 1 | answer 1\nquestion 2 | answer 2";
		Files.write(Paths.get(AnkiProperties.getProperty("cardFile")), cardsContent.getBytes(),
				StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		String sessionContent = "Date:2017-10-21\r\nRed | question 1 | answer 1\r\nOrange | question 2 | answer 2";
		Files.write(Paths.get(AnkiProperties.getProperty("sessionFile")), sessionContent.getBytes(),
				StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}

	@After
	public void tearDown() throws Exception {
		Files.deleteIfExists(Paths.get(AnkiProperties.getProperty("cardFile")));
		Files.deleteIfExists(Paths.get(AnkiProperties.getProperty("sessionFile")));
	}

	@Test
	public void testLoadExistingSession() {
		Session session = sessionService.loadSession();
		if (session == null)
			fail("session not found");
		assertEquals(2, session.getBoxes().size());
	}

	@Test
	public void testCreateFirstSession() {
		Session session = sessionService.createFirstSession();
		if (session == null)
			fail("session not found");
		assertEquals(1, session.getBoxes().size());
		assertEquals(2, session.getBoxes().get(Color.RED).getCards().size());
	}

	@Test
	public void testStoreSession() throws IOException {
		Files.deleteIfExists(Paths.get(AnkiProperties.getProperty("sessionFile")));

		Session session = createSession();
		sessionService.storeSession(session);

		assertTrue(Files.exists(Paths.get(AnkiProperties.getProperty("sessionFile"))));
	}

	@Test
	public void testUpdateSession() throws IOException {

		Session session = createSession();
		sessionService.updateSession(session);

		assertEquals(2, session.getBoxes().get(Color.RED).getCards().size());
		assertEquals(2, session.getBoxes().get(Color.ORANGE).getCards().size());
		assertNull(session.getBoxes().get(Color.GREEN));

	}

	private Session createSession() {
		Session session = new Session();
		session.setSessionDate(LocalDate.now());
		session.addCard(Color.GREEN, new Card("question", "answer"));
		session.addCard(Color.RED, new Card("question", "answer"));
		session.addCard(Color.ORANGE, new Card("question", "answer"));
		session.addCard(Color.GREEN, new Card("question", "answer"));
		return session;
	}
}
