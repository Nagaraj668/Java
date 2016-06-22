package model;

public class NewGame {
	private double overs;
	private int wickets;

	public NewGame() {
		super();
	}

	public NewGame(double overs, int wickets) {
		super();
		this.overs = overs;
		this.wickets = wickets;
	}

	public double getOvers() {
		return overs;
	}

	public void setOvers(double overs) {
		this.overs = overs;
	}

	public int getWickets() {
		return wickets;
	}

	public void setWickets(int wickets) {
		this.wickets = wickets;
	}
}