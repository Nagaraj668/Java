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
	private JTextField ipAddress = new JTextField();
	private JTextField name = new JTextField();
	private JButton sendGameRequestBtn = new JButton("Send Game Request");
	private JLabel yourName = new JLabel("");
	private JLabel enteryourName = new JLabel("Enter your name");
	private JLabel enterIp = new JLabel("Enter opponent's System IP Address");

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
	private Sender sender = new Sender();
	private ReceiverThread receiverThread = new ReceiverThread(this);
	private Gson gson = new Gson();
	DataExchange dataExchange;
	private boolean myStatus, iamBatting, iamBowling;

	private int opponentVal = -1;
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

		sendGameRequestBtn.setBounds(50, 200, 200, 30);
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

		yourName.setBounds(400, 640, 200, 20);
		yourScore.setBounds(400, 670, 200, 30);

		opponentName.setBounds(400, 720, 200, 30);
		opponentScore.setBounds(400, 760, 200, 30);

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
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case Keys.NEW_GAME:
			startNewGame();
			break;
		default:
			Dialogs.alert(e.getActionCommand());
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
		if (iamBatting) {
			if (gameScore.isThereWickets()) {
				if (opponentVal != -1) {
					if (opponentVal != run) {
						gameScore.setRuns(run + gameScore.getRuns());
						yourScore.setText(String.valueOf(gameScore.getRuns()));
						opponentVal = -1;
					}
					else
						wicket();
				} else {
					updateGround("Please wait for bowler");
				}
					
			}
		} else if (iamBowling) {

		}
	}

	private void startNewGame() {
		resetMatch();
		you = new You();
		you.setName(name.getText().toString());
		yourName.setText(you.getName());
		you.setIpAddress(ipAddress.getText().toString());
		HashMap<String, String> data = new HashMap<>();
		data.put(Keys.NEW_GAME, gson.toJson(new NewGame(3, 3)));
		data.put(Keys.USER, gson.toJson(you));
		dataExchange = new DataExchange(Keys.NEW_GAME, data);
		sender.send(dataExchange);
		updateGround("You spinned the coin, Please wait...");

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
			gameScore.setOvers(game.getOvers());
			gameScore.setWickets(game.getWickets());
			opponentGameScore.setOvers(game.getOvers());
			opponentGameScore.setWickets(game.getWickets());
			if (Dialogs.confirm(opponent.getName() + " is sending you the game Request\n" + "Overs: " + game.getOvers()
					+ "\n" + "Wickets: " + game.getWickets() + "\n" + "Do you want to play?")) {
				dataExchange = new DataExchange(Keys.REQ_ACCEPTED, null);
				sender.send(dataExchange);
				sendGameRequestBtn.disable();
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

		int rand = Utils.randInt(1, 10);
		System.out.println(rand);
		System.out.println(choices[rand % 2]);
		System.out.println(tossSelection);

		if (choices[rand % 2].equals(tossSelection)) {
			youWonToss();
		} else {
			youLostToss();
		}
	}

	@Override
	public void rejected() {
		hideTossArea();
		Dialogs.alert("Opponent rejected your request");
	}

	@Override
	public void noball() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bowl(DataExchange dataExchange) {

	}

	@Override
	public void hit(DataExchange dataExchange) {
		ground.setCaretPosition(ground.getText().length());
	}

	@Override
	public void autoReject() {
		System.out.println("auto rejected");
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
	public void requestAccepted() {
		sendGameRequestBtn.disable();
		updateGround("Your request accepted");
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
		if (s.equals(Keys.BATTING)) {
			youBatFirst();
		} else if (s.equals(Keys.BOWLING)) {
			youBowlFirst();
		}
	}

	@Override
	public void youLostToss() {
		updateGround("You lost toss, Please wait for opponent to choose");
		sender.send(new DataExchange(Keys.OPPO_WON_TOSS, null));
	}

	@Override
	public void youBatFirst() {
		updateGround("You are batting first");
		Dialogs.alert("Counter is going to start");
		startCounter();
		iamBatting = true;
		sender.send(new DataExchange(Keys.OPPO_BOWL, null));
	}

	@Override
	public void youBowlFirst() {
		updateGround("You are bowling first");
		Dialogs.alert("Counter is going to start");
		startCounter();
		iamBowling = true;
		sender.send(new DataExchange(Keys.YOU_BOWL, null));
	}

	@Override
	public void resetMatch() {
		iamBatting = false;
		iamBowling = false;
		ground.setText("");
		ipAddress.setText("");
		myStatus = false;
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
			wicket();
		}
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
		gameScore.setWickets(gameScore.getWickets() + 1);
	}

	@Override
	public void opponentWicket() {

	}

	@Override
	public void opponentHit(DataExchange dataExchange) {
		// TODO Auto-generated method stub

	}

}
