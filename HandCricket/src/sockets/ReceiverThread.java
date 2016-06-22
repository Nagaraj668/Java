package sockets;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.google.gson.Gson;

import frames.BaseFrame;
import ifc.DataReceivedListener;
import model.DataExchange;
import utils.Keys;

public class ReceiverThread extends Thread {

	public static DatagramSocket ds;
	public static byte buffer[] = new byte[1024];
	public static int clientport = 789;
	private Gson gson = new Gson();
	DataReceivedListener dataReceivedListener;

	public ReceiverThread(BaseFrame dataReceivedListener) {
		this.dataReceivedListener = dataReceivedListener;
	}

	@Override
	public void run() {
		receive();
	}

	public void receive() {
		try {
			ds = new DatagramSocket(clientport);
			while (true) {
				DatagramPacket p = new DatagramPacket(buffer, buffer.length);
				ds.receive(p);
				String psx = new String(p.getData(), 0, p.getLength());
				System.out.println(psx);
				DataExchange dataExchange = gson.fromJson(psx, DataExchange.class);
				dispatch(dataExchange);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dispatch(DataExchange dataExchange) {
		switch (dataExchange.getExchangeType()) {
		case Keys.NEW_GAME:
			dataReceivedListener.newGame(dataExchange);
			break;
		case Keys.REQ_ACCEPTED:
			dataReceivedListener.requestAccepted(dataExchange);
			break;
		case Keys.AUTO_REJECT:
			dataReceivedListener.autoReject();
			break;
		case Keys.REJECTED:
			dataReceivedListener.rejected();
			break;
		case Keys.OPPO_BAT:
			dataReceivedListener.youAreRequestToBatFirst();
			break;
		case Keys.OPPO_BOWL:
			dataReceivedListener.youAreRequestToBowlFirst();
			break;
		case Keys.OPPO_WON_TOSS:
			dataReceivedListener.opponentWonToss();
			break;
		case Keys.OPPO_BOWLING:
			dataReceivedListener.bowl(dataExchange);
			break;
		case Keys.OPPO_HIT:
			dataReceivedListener.opponentHit(dataExchange);
			break;
		case Keys.NO_BALL:
			dataReceivedListener.noball();
			break;
		default:
			break;
		}
	}

}