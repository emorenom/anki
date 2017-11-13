package domain;

import java.time.LocalDate;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import domain.Box.Color;

public class Session {

	private LocalDate sessionDate;
	private Map<Color, Box> boxes = new HashMap<>();

	public Session(LocalDate sessionDate) {
		this.sessionDate = sessionDate;
	}

	public Session() {
	}

	public LocalDate getSessionDate() {
		return sessionDate;
	}

	public void setSessionDate(LocalDate sessionDate) {
		this.sessionDate = sessionDate;
	}

	public Map<Color, Box> getBoxes() {
		return boxes;
	}

	public void setBoxes(Map<Color, Box> boxes) {
		this.boxes = boxes;
	}

	public void addBox(Box box) {
		if (!this.boxes.containsKey(box.getId()))
			this.boxes.put(box.getId(), box);
	}

	public Box getBox(Color color) {
		return this.boxes.get(color);
	}

	public void replaceBox(Color color, Box box) {
		if (box == null)
			this.removeBox(color);
		else {
			box.setId(color);
			this.boxes.put(color, box);
		}
	}

	public void removeBox(Color color) {
		this.boxes.remove(color);
	}

	public void addCard(Color boxId, Card card) {

		if(card == null)
			return;
		if (!this.boxes.containsKey(boxId))
			this.boxes.put(boxId, new Box(boxId));
		this.boxes.get(boxId).addCard(card);

	}

	public Deque<Card> getBoxCards(Color boxColor) {

		if (this.boxes.get(boxColor) == null)
			return null;

		return this.boxes.get(boxColor).getCards();

	}

	public Card getNextCard(Color boxColor) {

		if (this.boxes.get(boxColor) == null)
			return null;
		return this.boxes.get(boxColor).popCard();

	}

}
