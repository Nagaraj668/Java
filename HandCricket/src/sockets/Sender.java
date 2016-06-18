package sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.google.gson.Gson;

import model.DataExchange;

public class Sender {
	public static DatagramSocket ds;
	public static int clientport = 789, serverport = 790;
	private Gson gson = new Gson();
	InetAddress is;

	public Sender() {
		try {
			ds = new DatagramSocket(serverport);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		try {
			is = InetAddress.getByName("localhost");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
	}

	public void send(DataExchange dataExchange) {
		String str = gson.toJson(dataExchange);
		byte buffer[] = new byte[1024];
		buffer = str.getBytes();
		try {
			ds.send(new DatagramPacket(buffer, str.length(), is, clientport));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}