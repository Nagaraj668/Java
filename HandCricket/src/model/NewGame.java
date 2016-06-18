package model;

public class NewGame {
	private float overs;
	private int wickets;

	public NewGame() {
		super();
	}

	public NewGame(float overs, int wickets) {
		super();
		this.overs = overs;
		this.wickets = wickets;
	}

	public float getOvers() {
		return overs;
	}

	public void setOvers(float overs) {
		this.overs = overs;
	}

	public int getWickets() {
		return wickets;
	}

	public void setWickets(int wickets) {
		this.wickets = wickets;
	}
}