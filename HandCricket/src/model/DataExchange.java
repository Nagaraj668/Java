package model;

import java.util.HashMap;

public class DataExchange {

	private String exchangeType;
	private HashMap<String, String> data;

	public DataExchange() {
		super();
	}

	public DataExchange(String exchangeType, HashMap<String, String> data) {
		super();
		this.exchangeType = exchangeType;
		this.data = data;
	}

	public String getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}

	public HashMap<String, String> getData() {
		return data;
	}

	public void setData(HashMap<String, String> data) {
		this.data = data;
	}
}