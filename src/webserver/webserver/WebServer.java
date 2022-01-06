package webserver;

import java.net.*;
import java.io.*;
//import java.util.Map;
//import java.awt.color.*;

public class WebServer extends Thread {
	protected Socket clientSocket; //mostenita de la Thread => de aceea avem protected
	private String state;
	private ServerSocket serverSocket;
	private static WebServer instance;
	static int portNumber;
	
	private WebServer(Socket clientSoc) {
		clientSocket = clientSoc;
		start(); //metoda mostenita din clasa Thread trebuie mentionata in constructor
	}
	public WebServer()
	{
		state="Stopped";
	}
	
	public static WebServer getInstance()
	{
		if(instance==null)
		{
			instance = new WebServer(); //creeaza un obiect nou de tip WebServer
		}
		return instance;
	}
	
	public void maintananceServer()
	{
		state="Maintanance";
	}
	public void startServer() {
        state = "Running";

        try {
            serverSocket = new ServerSocket(9999);  //initializez webserve-ul cu portul 9999
            System.out.println("Connection Socket Created");
            try {
                while (true) {
                    System.out.println("Waiting for Connection");
                    new WebServer(serverSocket.accept()).start(); //asteapta sa se creeze o conexiune pt socket-ul asta 
                    //si o accepta => apoi start() face ca thread-ul sa inceapa executia
                }
            } catch (SocketException e) { 
                if (serverSocket.isClosed()) //returneaza true daca socket-ul a fost inchis
                    System.out.println("Connection Closed.");
            } catch (IOException e) { 
                System.err.println("Accept failed.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 9999.");
            System.exit(1);
        } finally {
            try {
                if (serverSocket != null) { //finally se executa indiferent de exceptia pe care a prins-o din try-uri
                    serverSocket.close(); 
                }
            } catch (IOException e) {
                System.err.println("Could not close port: 9999.");
                System.exit(1);
            }
        }
    }
	public void stopServer() throws IOException
	{
	state="Stopped";
	serverSocket.close();
	}
	public void startServerMaintenance() {
        state = "Maintenance";
    }

    public void endServerMaintenance() {
        state = "Running";
    }
    public void runningServer() {
        state = "Running";
    }
	public void run() {
		System.out.println("New Communication Thread Started");

		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			 Html.getInstance().html(in.readLine(), clientSocket.getOutputStream());
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) {
				System.out.println("Server: " + inputLine);
				out.println(inputLine);

				if (inputLine.trim().equals(""))
					break;
			}

			//out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Problem with Communication Server");
			System.exit(1);
		}
	}
	public void setPortNumber(int portNumber)
	{
		this.portNumber=portNumber;
	}
	public String getServerState()
	{
		return state;
	}
	
}
