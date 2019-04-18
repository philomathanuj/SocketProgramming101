package com.philomathanuj.testclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClient {
	
	public static void main(String[] args) {
		String host = "localhost";
		int port = 8085;
		try {
			Socket socket = new Socket(host,port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String userInput;
    		while((userInput = stdIn.readLine()) != null) {
    			out.println(userInput);
    			System.out.println("test client: "+in.readLine());
    		}
		}catch(IOException ie) {
			ie.printStackTrace();
		}
	}

}
