package ifc;

import model.DataExchange;

public interface DataReceivedListener {
	void newGame(DataExchange dataExchange);
	void rejected();
	void noball();
	void bowl(DataExchange dataExchange);
	void opponentBowling(DataExchange dataExchange);
	void hit(DataExchange dataExchange);
	void opponentHit(DataExchange dataExchange);
	void wicket();
	void opponentWicket();
	void autoReject();
	void matchWin(DataExchange dataExchange);
	void matchLose(DataExchange dataExchange);
	void matchDraw(DataExchange dataExchange);
	void resetMatch();
	void requestAccepted();
	void declareOpponentWin();
	void tossFinished(DataExchange dataExchange);
	void opponentWonToss();
	void youWonToss();
	void youLostToss();
	void youBatFirst();
	void youBowlFirst();
	void youAreRequestToBatFirst();
	void youAreRequestToBowlFirst();
}
