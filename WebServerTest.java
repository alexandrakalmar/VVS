package webserver;

import static org.junit.Assert.*;
import java.util.Map;

import java.io.IOException;

import org.junit.Test;

public class WebServerTest {

	@Test
	public void ServerStopStatus() throws IOException{
		if (WebServer.getInstance().getServerState().equals("Running")) 
		{
            WebServer.getInstance().stopServer();
        }
		assertEquals("Stopped", WebServer.getInstance().getServerState());
	}
	@Test
	public void ServerStartStatus() throws IOException{
		WebServer.getInstance().runningServer();
		assertEquals("Running",WebServer.getInstance().getServerState());
	}
	@Test
	public void ServerMaintananceStatus() throws IOException{
		WebServer.getInstance().maintananceServer();
		assertEquals("Maintanance",WebServer.getInstance().getServerState());
	}
	@Test
	  public void correctServerStartStatusChange(){
        if (WebServer.getInstance().getServerState().equals("Stopped")) {
            new Thread(
                    new Runnable() {
						@Override
						public void run() {
							WebServer.getInstance().runningServer();
						}
					}
            ).start();
        }
        else if(WebServer.getInstance().getServerState().equals("Maintenance")){
            WebServer.getInstance().runningServer();
        }

        while (WebServer.getInstance().getServerState().equals("Stopped")); //Wait for the server to start
        assertEquals("Running", WebServer.getInstance().getServerState());
    }

}
