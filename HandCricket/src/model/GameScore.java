package model;

public class GameScore {

	private int runs;
	private int wickets;
	private float overs;
	private float runRate;
	private int ballsRemaining;
	private int totalWickets;
	private float totalOvers;
	private int runsToWin;

	public GameScore() {
		super();
	}

	public GameScore(int runs, int wickets, float overs, float runRate, int ballsRemaining, int runsToWin,
			int totalWickets, float totalOvers) {
		super();
		this.runs = runs;
		this.wickets = wickets;
		this.overs = overs;
		this.runRate = runRate;
		this.ballsRemaining = ballsRemaining;
		this.runsToWin = runsToWin;
		this.totalWickets = totalWickets;
		this.totalOvers = totalOvers;
	}

	public int getTotalWickets() {
		return totalWickets;
	}

	public void setTotalWickets(int totalWickets) {
		this.totalWickets = totalWickets;
	}

	public float getTotalOvers() {
		return totalOvers;
	}

	public void setTotalOvers(float totalOvers) {
		this.totalOvers = totalOvers;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public int getWickets() {
		return wickets;
	}

	public void setWickets(int wickets) {
		this.wickets = wickets;
	}

	public float getOvers() {
		return overs;
	}

	public void setOvers(float overs) {
		this.overs = overs;
	}

	public float getRunRate() {
		return runRate;
	}

	public void setRunRate(float runRate) {
		this.runRate = runRate;
	}

	public int getBallsRemaining() {
		return ballsRemaining;
	}

	public void setBallsRemaining(int ballsRemaining) {
		this.ballsRemaining = ballsRemaining;
	}

	public int getRunsToWin() {
		return runsToWin;
	}

	public void setRunsToWin(int runsToWin) {
		this.runsToWin = runsToWin;
	}

	public boolean isAllOut() {
		boolean flag = false;
		if (wickets == totalWickets) {
			flag = true;
		}
		return flag;
	}

	public boolean isThereWickets() {
		boolean flag = false;
		if (wickets < totalWickets) {
			flag = true;
		}
		return flag;
	}

	public void hitWicket() {
		wickets++;
	}
}
