package com.philomathanuj.testserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestServer {

    private static TestServer server; 
    private ServerSocket serverSocket;

    /**
     * This executor service has 2 threads. 
     * So it means your server can process max 2 concurrent requests.
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(2);        

    public static void main(String[] args) throws IOException {
        server = new TestServer();
        server.runServer();
    }

    private void runServer() {        
        int serverPort = 8085;
        try {
            System.out.println("Starting Server");
            serverSocket = new ServerSocket(serverPort); 

            while(true) {
                System.out.println("Waiting for request");
                try {
                    Socket s = serverSocket.accept();
                    System.out.println("Processing request");
                    executorService.submit(new ServiceRequest(s));
                } catch(IOException ioe) {
                    System.out.println("Error accepting connection");
                    ioe.printStackTrace();
                }
            }
        }catch(IOException e) {
            System.out.println("Error starting Server on "+serverPort);
            e.printStackTrace();
        }
    }

    //Call the method when you want to stop your server
    private void stopServer() {
        //Stop the executor service.
        executorService.shutdownNow();
        try {
            //Stop accepting requests.
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error in server shutdown");
            e.printStackTrace();
        }
        System.exit(0);
    }

    class ServiceRequest implements Runnable {

        private Socket socket;

        public ServiceRequest(Socket connection) {
            this.socket = connection;
        }

        public void run() {
        	try {
        		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        		System.out.println("Thread Started with name :"+Thread.currentThread().getName());
        		String userInput;
        		while((userInput = in.readLine()) != null) {
        			userInput = userInput.replaceAll("[^A-Za-z0-9 ]","");
        			System.out.println("Received message from "+Thread.currentThread().getName()+" : "+userInput);
        			writer.write("You entered: "+userInput);
        			writer.newLine();
        			writer.flush();
        		}
            //Do your logic here. You have the `socket` available to read/write data.
        	/*ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        	String msg = (String) in.readObject();
			System.out.println("\nMessage received: "+msg+"\n");
			in.close();*/
			socket.close();
			System.out.println("Client closed.");
            }catch(IOException ioe) {
                System.out.println("Error closing client connection");
            }
        }        
    }
}