package model;

import java.math.BigDecimal;

public class GameScore {

	private int runs;
	private int wickets;
	private double overs;
	private double runRate;
	private int ballsRemaining;
	private int totalWickets;
	private double totalOvers;
	private int runsToWin;
	private int innings;

	public GameScore() {
		super();
	}

	public GameScore(int runs, int wickets, double overs, double runRate, int ballsRemaining, int runsToWin,
			int totalWickets, double totalOvers, int innings) {
		super();
		this.runs = runs;
		this.wickets = wickets;
		this.overs = overs;
		this.runRate = runRate;
		this.ballsRemaining = ballsRemaining;
		this.runsToWin = runsToWin;
		this.totalWickets = totalWickets;
		this.totalOvers = totalOvers;
		this.innings = innings;
	}

	public int getInnings() {
		return innings;
	}

	public boolean setInnings() {
		this.innings = this.innings + 1;
		if (innings > 2) {
			return true;
		} else {
			return false;
		}
	}

	public int getTotalWickets() {
		return totalWickets;
	}

	public void setTotalWickets(int totalWickets) {
		this.totalWickets = totalWickets;
	}

	public double getTotalOvers() {
		return totalOvers;
	}

	public void setTotalOvers(double totalOvers) {
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

	public boolean setWickets(int wickets) {
		this.wickets = wickets;
		if (wickets == totalWickets) {
			return true;
		} else {
			return false;
		}
	}

	public double getOvers() {
		return overs;
	}

	public void setOvers(double overs) {
		this.overs = overs;
	}

	public double getRunRate() {
		return runRate;
	}

	public void setRunRate(double runRate) {
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

	public boolean updateOver() {
		boolean flag = false;
		overs = overs + 0.1;
		double balls = BigDecimal.valueOf(overs).divideAndRemainder(BigDecimal.ONE)[1].floatValue();
		if (balls == 0.6) {
			overs = overs - 0.6 + 1.0;
			if (overs == totalOvers)
				flag = true;
		}
		return flag;
	}
}
