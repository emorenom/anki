package domain;

public class BoxCard extends Card {

	private String box;

	public BoxCard(String boxName, String question, String answer) {
		super(question,answer);
		this.box = boxName;
	}

	public String getBox() {
		return box;
	}

	public void setBox(String box) {
		this.box = box;
	}
	
	@Override
	public String toString() {
		return this.getBox() + " | " + this.getQuestion() + " | " + this.getAnswer();
	}
}
