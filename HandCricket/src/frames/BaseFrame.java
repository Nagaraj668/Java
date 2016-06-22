package frames;

import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.google.gson.Gson;

import ifc.DataReceivedListener;
import model.DataExchange;
import model.GameScore;
import model.NewGame;
import model.Opponent;
import model.OpponentScore;
import model.You;
import sockets.ReceiverThread;
import sockets.Sender;
import utils.Dialogs;
import utils.Keys;
import utils.Utils;

public class BaseFrame extends JFrame implements ActionListener, DataReceivedListener {

	private static final long serialVersionUID = 1L;
	private JTextField ipAddress = new JTextField("192.168.1.4");
	private JTextField myipAddress = new JTextField("192.168.1.3");
	private JTextField name = new JTextField();
	private JButton sendGameRequestBtn = new JButton("Send Game Request");
	private JLabel yourName = new JLabel("");
	private JLabel enteryourName = new JLabel("Enter your name");
	private JLabel enterIp = new JLabel("Enter opponent's System IP Address");
	private JLabel entermyIp = new JLabel("Enter your System IP Address");

	private JLabel opponentName = new JLabel("");
	private JLabel status = new JLabel("");
	private JLabel yourScore = new JLabel("0/0, Overs 0.0");
	private JLabel opponentScore = new JLabel("0/0, Overs 0.0");
	private JLabel yourRR = new JLabel("");
	private JLabel opponentRR = new JLabel("");
	private You you;
	private Opponent opponent;
	private JButton checkStatusBtn = new JButton("Check Status");
	private GameScore gameScore = new GameScore();
	private OpponentScore opponentGameScore = new OpponentScore();
	private Sender sender;
	private ReceiverThread receiverThread = new ReceiverThread(this);
	private Gson gson = new Gson();
	DataExchange dataExchange;
	private boolean myStatus, iamBatting, iamBowling;

	private int opponentVal = -1, batsmanShot = -1;
	private JButton bt0 = new JButton("0");
	private JButton bt1 = new JButton("1");
	private JButton bt2 = new JButton("2");
	private JButton bt3 = new JButton("3");
	private JButton bt4 = new JButton("4");
	private JButton bt5 = new JButton("5");
	private JButton bt6 = new JButton("6");
	private JButton bt7 = new JButton("R");
	private TextArea ground = new TextArea();
	private JLabel counterLabel = new JLabel("Counter: ");
	private JButton counter = new JButton("");

	public BaseFrame() {
		super("Hand Cricket");
		setExtendedState(MAXIMIZED_BOTH);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		enteryourName.setBounds(50, 50, 100, 30);
		add(enteryourName);

		name.setBounds(50, 80, 200, 25);
		add(name);

		enterIp.setBounds(50, 110, 250, 30);
		add(enterIp);

		ipAddress.setBounds(50, 140, 200, 25);
		add(ipAddress);

		entermyIp.setBounds(50, 180, 250, 30);
		add(entermyIp);

		myipAddress.setBounds(50, 210, 200, 25);
		add(myipAddress);

		sendGameRequestBtn.setBounds(50, 260, 200, 30);
		add(sendGameRequestBtn);

		sendGameRequestBtn.addActionListener(this);
		sendGameRequestBtn.setActionCommand(Keys.NEW_GAME);

		ground.setBounds(380, 50, 300, 400);
		add(ground);

		bt0.setBounds(400, 500, 50, 50);
		add(bt0);
		bt1.setBounds(460, 500, 50, 50);
		add(bt1);
		bt2.setBounds(520, 500, 50, 50);
		add(bt2);
		bt3.setBounds(580, 500, 50, 50);
		add(bt3);

		bt4.setBounds(400, 560, 50, 50);
		add(bt4);
		bt5.setBounds(460, 560, 50, 50);
		add(bt5);
		bt6.setBounds(520, 560, 50, 50);
		add(bt6);
		bt7.setBounds(580, 560, 50, 50);
		add(bt7);

		int y = 300;
		yourName.setBounds(100, 640 - y, 200, 20);
		yourScore.setBounds(100, 670 - y, 200, 30);

		opponentName.setBounds(100, 720 - y, 200, 30);
		opponentScore.setBounds(100, 760 - y, 200, 30);

		add(yourName);
		add(yourScore);
		add(opponentName);
		add(opponentScore);

		bt0.addActionListener(this);
		bt1.addActionListener(this);
		bt2.addActionListener(this);
		bt3.addActionListener(this);
		bt4.addActionListener(this);
		bt5.addActionListener(this);
		bt6.addActionListener(this);
		bt7.addActionListener(this);

		receiverThread.start();

		askPlayerName();
	}

	private void askPlayerName() {
		you = new You(Dialogs.showInputBox("Your name", "Enter your name", this), "");
		if (you.getName() == null || you.getName().equals("")) {
			askPlayerName();
		}
		name.setText(you.getName());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case Keys.NEW_GAME:
			startNewGame();
			sendGameRequestBtn.setText(Keys.END_GAME);
			sendGameRequestBtn.setActionCommand(Keys.END_GAME);
			break;
		case Keys.END_GAME:
			resetMatch();
			break;
		}

		try {
			if (!myStatus) {
				return;
			}
			int scoreRun = Integer.parseInt(e.getActionCommand());
			updateScore(scoreRun);
		} catch (Exception e2) {
			System.out.println(e2);
		}
	}

	private void updateScore(int run) {
		if (batsmanShot != -1) {
			updateGround("hey, you already entered it, Please wait for opponent");
		}
		batsmanShot = run;
		stopThread();
		if (iamBatting) {
			if (gameScore.isThereWickets()) {
				if (opponentVal != -1) {
					if (opponentVal != run) {
						updateScoreboard(run);
						opponentVal = -1;
						batsmanShot = -1;
					} else
						wicket();
					startCounter();
				} else {
					stopThread();
					updateGround("Counter stopped, Please wait for bowler");
				}
			} else {
				inningsOver(Keys.ALL_OUT);
			}
		} else if (iamBowling) {
			if (opponentGameScore.isThereWickets()) {
				if (opponentVal != -1) {
					if (opponentVal != run) {
						updateOpponentScoreboard(run);
						opponentVal = -1;
						batsmanShot = -1;
					} else
						opponentWicket();
					startCounter();
				} else {
					stopThread();
					updateGround("Counter stopped, Please wait for batsman");
				}
			} else {
				inningsOver(Keys.ALL_OUT);
			}
		}
	}

	private void updateScoreboard(int run) {
		gameScore.setRuns(run + gameScore.getRuns());
		boolean flag = gameScore.updateOver();
		yourScore.setText(
				String.valueOf(gameScore.getRuns()) + "/" + gameScore.getWickets() + " Overs: " + gameScore.getOvers());
		HashMap<String, String> data = new HashMap<>();
		data.put(Keys.OPPO_SCORE, gson.toJson(gameScore));
		data.put(Keys.OPPO_RUN, String.valueOf(run));
		sender.send(new DataExchange(Keys.OPPO_HIT, data));
		if (flag) {
			inningsOver(Keys.OVER_COMPLETE);
		}
	}

	private void updateOpponentScoreboard(int run) {
		opponentGameScore.setRuns(run + opponentGameScore.getRuns());
		boolean flag = opponentGameScore.updateOver();
		opponentScore.setText(String.valueOf(opponentGameScore.getRuns()) + "/" + opponentGameScore.getWickets()
				+ " Overs: " + opponentGameScore.getOvers());
		HashMap<String, String> data = new HashMap<>();
		data.put(Keys.BALL, run + "");
		DataExchange dataExchange = new DataExchange(Keys.OPPO_BOWLING, data);
		sender.send(dataExchange);
		if (flag) {
			inningsOver(Keys.OVER_COMPLETE);
		}
	}

	private void inningsOver(String inningOverType) {

		switch (inningOverType) {

		}

		boolean temp = iamBatting;
		iamBatting = iamBowling;
		iamBowling = temp;
		gameScore.setInnings();
		if (iamBatting) {
			Dialogs.alert("You are batting now");
		} else {
			Dialogs.alert("You are bowling now");
		}
	}

	private void startNewGame() {

		if (name.getText().toString() == null || name.getText().toString().equals("")) {
			Dialogs.alert("Please enter your name");
			return;
		} else if (ipAddress.getText().toString() == null || ipAddress.getText().toString().equals("")) {
			Dialogs.alert("Please enter friend's ip address");
			return;
		} else if (myipAddress.getText().toString() == null || myipAddress.getText().toString().equals("")) {
			Dialogs.alert("Please enter your system ip address");
			return;
		}

		resetMatch();
		you.setName(name.getText().toString());
		opponent = new Opponent();
		opponent.setIpAddress(ipAddress.getText().toString());
		you.setIpAddress(myipAddress.getText().toString());
		yourName.setText(you.getName());
		HashMap<String, String> data = new HashMap<>();
		data.put(Keys.NEW_GAME, gson.toJson(new NewGame(3, 3)));
		data.put(Keys.USER, gson.toJson(you));
		sender = Sender.getInstance(ipAddress.getText().toString());
		dataExchange = new DataExchange(Keys.NEW_GAME, data);
		sender.send(dataExchange);
		updateGround("You have sent game request, Please wait...");
		you.setIpAddress("");
	}

	private void updateGround(String msg) {
		ground.setText(ground.getText().toString() + "\n" + msg);
	}

	@Override
	public void newGame(DataExchange dataExchange) {
		if (myStatus) {
			dataExchange = new DataExchange(Keys.AUTO_REJECT, null);
			sender.send(dataExchange);
		} else {
			opponent = gson.fromJson(dataExchange.getData().get(Keys.USER), Opponent.class);
			NewGame game = gson.fromJson(dataExchange.getData().get(Keys.NEW_GAME), NewGame.class);
			gameScore.setTotalOvers(game.getOvers());
			gameScore.setTotalWickets(game.getWickets());
			opponentGameScore.setTotalOvers(game.getOvers());
			opponentGameScore.setTotalWickets(game.getWickets());
			sender = Sender.getInstance(opponent.getIpAddress());
			if (Dialogs.confirm(opponent.getName() + " is sending you the game Request\n" + "Overs: " + game.getOvers()
					+ "\n" + "Wickets: " + game.getWickets() + "\n" + "Do you want to play?")) {
				HashMap<String, String> data = new HashMap<>();
				data.put(Keys.OPPO_NAME, you.getName());
				dataExchange = new DataExchange(Keys.REQ_ACCEPTED, data);
				sender.send(dataExchange);
				myStatus = true;
				sendGameRequestBtn.setText(Keys.END_GAME);
				sendGameRequestBtn.setActionCommand(Keys.END_GAME);
				toss(opponent);
			} else {
				dataExchange = new DataExchange(Keys.REJECTED, null);
				sender.send(dataExchange);
			}
		}
	}

	private void toss(Opponent user) {
		String[] choices = { Keys.TAILS, Keys.HEADS };
		String tossSelection = Dialogs.showDropdown(user.getName() + " is spinning the coin, Please tell your choice",
				"TOSS", choices, this);
		if (tossSelection == null)
			toss(user);
		int rand = Utils.randInt(1, 10);
		if (choices[rand % 2].equals(tossSelection)) {
			youWonToss();
		} else {
			youLostToss();
		}
	}

	@Override
	public void rejected() {
		hideTossArea();
		sendGameRequestBtn.setText(Keys.NEW_GAME);
		sendGameRequestBtn.setActionCommand(Keys.NEW_GAME);
		Dialogs.alert("Opponent rejected your request");
	}

	@Override
	public void noball() {
		updateScoreboard(1);
	}

	@Override
	public void bowl(DataExchange dataExchange) {
		if (batsmanShot == -1) {
			updateGround("hey, opponent sent number/ball, hit...");
		}
		opponentVal = Integer.parseInt(dataExchange.getData().get(Keys.BALL));
	}

	@Override
	public void hit(DataExchange dataExchange) {
		ground.setCaretPosition(ground.getText().length());
	}

	@Override
	public void autoReject() {
		System.out.println("auto rejected");
		updateGround("Player is already playing with someone");
	}

	@Override
	public void matchWin(DataExchange dataExchange) {
		// TODO Auto-generated method stub
		hideTossArea();
	}

	@Override
	public void matchLose(DataExchange dataExchange) {
		// TODO Auto-generated method stub
		hideTossArea();
	}

	@Override
	public void matchDraw(DataExchange dataExchange) {
		// TODO Auto-generated method stub
		hideTossArea();
	}

	private void hideTossArea() {

	}

	@Override
	public void requestAccepted(DataExchange dataExchange) {
		myStatus = true;
		sendGameRequestBtn.setText(Keys.END_GAME);
		sendGameRequestBtn.setActionCommand(Keys.END_GAME);
		opponent.setName(dataExchange.getData().get(Keys.OPPO_NAME));
		updateGround("Your request accepted, and you spinned the coin.");
	}

	@Override
	public void declareOpponentWin() {
		// TODO Auto-generated method stub
		hideTossArea();
	}

	@Override
	public void tossFinished(DataExchange dataExchange) {

	}

	@Override
	public void youWonToss() {
		yourName.setText(you.getName());
		opponentName.setText(opponent.getName());
		updateGround("You won toss");
		String[] choices = { Keys.BATTING, Keys.BOWLING };
		String s = Dialogs.showDropdown("\nWhat would you like to do ?\n", "You won the toss", choices, this);
		if (s == null) {
			youWonToss();
		} else if (s.equals(Keys.BATTING)) {
			youBatFirst();
		} else if (s.equals(Keys.BOWLING)) {
			youBowlFirst();
		}
		gameScore.setInnings();
	}

	@Override
	public void youLostToss() {
		updateGround("You lost toss, Please wait for opponent to choose");
		sender = Sender.getInstance(opponent.getIpAddress());
		sender.send(new DataExchange(Keys.OPPO_WON_TOSS, null));
	}

	@Override
	public void youBatFirst() {
		updateGround("You are batting first");
		sender.send(new DataExchange(Keys.OPPO_BOWL, null));
		Dialogs.alert("Counter is going to start");
		gameScore.setInnings();
		iamBatting = true;
		startCounter();
	}

	@Override
	public void youBowlFirst() {
		updateGround("You are bowling first");
		sender.send(new DataExchange(Keys.YOU_BOWL, null));
		Dialogs.alert("Counter is going to start");
		gameScore.setInnings();
		iamBowling = true;
		startCounter();
	}

	@Override
	public void resetMatch() {
		iamBatting = false;
		iamBowling = false;
		ground.setText("");
		gameScore = new GameScore();
		opponentGameScore = new OpponentScore();
		yourName.setText("");
		yourScore.setText("0/0, Overs 0.0");
		opponentScore.setText("0/0, Overs 0.0");
		myStatus = false;
		sendGameRequestBtn.setText(Keys.NEW_GAME);
		sendGameRequestBtn.setActionCommand(Keys.NEW_GAME);
	}

	@Override
	public void youAreRequestToBatFirst() {
		updateGround("You are requested to bat first");
		Dialogs.alert("Counter is going to start");
		startCounter();
		iamBatting = true;
		iamBowling = false;
	}

	@Override
	public void youAreRequestToBowlFirst() {
		updateGround("You are requested to bowl first");
		Dialogs.alert("Counter is going to start");
		startCounter();
		iamBatting = false;
		iamBowling = true;
	}

	Counter counterThread;

	private void startCounter() {
		counterThread = new Counter();
		counterThread.start();
	}

	private void stopThread() {
		counterThread.stop();
	}

	class Counter extends Thread {
		int i;

		@Override
		public void run() {
			System.out.println("thread started");
			i = 0;
			while (i < 3) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				updateGround("Counter: " + i);
				i++;
			}
			if (iamBatting)
				wicket();
			else
				putnoball();
		}
	}

	private void endGame() {

	}

	public void putnoball() {
		sender.send(new DataExchange(Keys.NO_BALL, null));
	}

	@Override
	public void opponentWonToss() {
		youWonToss();
	}

	@Override
	public void opponentBowling(DataExchange dataExchange) {

	}

	@Override
	public void wicket() {
		if (gameScore.setWickets(gameScore.getWickets() + 1))
			inningsOver(Keys.ALL_OUT);
	}

	@Override
	public void opponentWicket() {
		if (opponentGameScore.setWickets(opponentGameScore.getWickets() + 1))
			inningsOver(Keys.ALL_OPPO_OUT);
	}

	@Override
	public void opponentHit(DataExchange dataExchange) {
		opponentVal = Integer.parseInt(dataExchange.getData().get(Keys.OPPO_HIT));
		opponentGameScore = gson.fromJson(dataExchange.getData().get(Keys.OPPO_SCORE), OpponentScore.class);

		if (opponentGameScore.isThereWickets()) {
			if (batsmanShot != -1) {
				if (batsmanShot != opponentVal) {
					opponentScore.setText(String.valueOf(opponentGameScore.getRuns()) + "/"
							+ opponentGameScore.getWickets() + " Overs: " + opponentGameScore.getOvers());

					opponentVal = -1;
					batsmanShot = -1;
				} else
					opponentWicket();
				startCounter();
			} else {
				stopThread();
				updateGround("Counter stopped, Please wait for bowler");
			}
		} else {
			inningsOver(Keys.ALL_OUT);
		}
		opponentScore.setText(opponentGameScore.getRuns() + "/" + opponentGameScore.getWickets() + ", Overs: "
				+ opponentGameScore.getOvers());
	}
}