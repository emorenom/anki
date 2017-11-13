package domain;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class Box {

	public enum Color {RED("Red"), ORANGE("Orange"), GREEN("Green");
			private String value;
			Color(String value) {
				this.value = value;
			};
			public String getValue() {
				return value;
			}
		}
	
	private Color id;
	private Deque<Card> cards = new ArrayDeque<>();
	
	public Box(Color boxId) {
		this.id = boxId;
	}

	public Color getId() {
		return id;
	}

	public void setId(Color id) {
		this.id = id;
	}

	public Deque<Card> getCards() {
		return cards;
	}

	public void setCards(Deque<Card> cards) {
		this.cards = cards;
	}

	public void addCard(Card card) {
		this.cards.addLast(card);
	}
	
	public Card popCard() {
		try {
			return this.cards.pop();
		} catch(NoSuchElementException e) {
			return null;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		
		if(o == null)
			return false;
		
		if(!(o instanceof Box))
			return false;
		
		Box other = (Box) o;
		if(!other.getId().equals(this.getId()))
			return false;
		
		return true;
		
	}
	
	@Override
	public String toString() {
		return "Box: " + id + " - Cards:[ " + cards + "]";
	}
}
