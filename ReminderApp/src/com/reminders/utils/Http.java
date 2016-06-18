package com.reminders.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Http {

	private final String USER_AGENT = "Mozilla/5.0";

	static String folder = "C:\\Users\\NAGARAJ SRJ\\git\\ReminderApp\\codes\\";

	public static void main(String[] args) throws Exception {

		try {
			File file = new File(folder);
			file.mkdirs();
		} catch (Exception e) {
			// TODO: handle exception
		}

		Http http = new Http();

		System.out.println("Testing 1 - Send Http GET request");
		http.sendGet();

		// System.out.println("\nTesting 2 - Send Http POST request");
		// http.sendPost();

	}

	// HTTP GET request
	private void sendGet() throws Exception {

		String url = "http://myapp.freeoda.com/files/get-file.php?name=t";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

		String[] fileObj = response.toString().split("thisIsFileSeperator");

		try {
			String path = folder + fileObj[3] + "\\dgdfg.txt" ;
			System.out.println(path);
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(path));
				writer.write(fileObj[2]);

			} catch (IOException e) {
			} finally {
				try {
					if (writer != null)
						writer.close();
				} catch (IOException e) {
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// HTTP POST request
	private void sendPost() throws Exception {

		String url = "https://selfsolve.apple.com/wcResults.do";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

	}

}