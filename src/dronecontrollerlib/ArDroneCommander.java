/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dronecontrollerlib;
import java.net.*;
import java.util.*;
/**
 *
 * @author Seb
 */
public class ArDroneCommander /*extends Thread*/ {
    InetAddress inet_addr;
    DatagramSocket socket;
    int seq = 1; //Send AT command with sequence number 1 will reset the counter
    
    static final String DEFAULT_IP = "192.168.1.1";
    static final int DEFAULT_PORT = 5556;
    static final int DELAY_IN_MS = 30;
    private Utility utility;
    
    private ArDroneCommand cmd;
    private ArDroneReceiver receiver;
    private Boolean send = true;
    private Boolean isLanding=false;
    private Boolean isMoving=false;
    NavData navData = new NavData();
    
    /** Holder */
    private static class SingletonHolder
    {	
    /** Instance unique non préinitialisée */
        private final static ArDroneCommander instance = new ArDroneCommander();
    }

    /** Point d'accès pour l'instance unique du singleton
     * @return  */
    public static ArDroneCommander getInstance()
    {
        return SingletonHolder.instance;
    }
    
    private ArDroneCommander()
    {
        
    }
    
    public void init(Utility utility)
    {
        this.utility = utility;
       
        StringTokenizer st = new StringTokenizer(DEFAULT_IP, ".");

	byte[] ip_bytes = new byte[4];
	if (st.countTokens() == 4){
 	    for (int i = 0; i < 4; i++){
		ip_bytes[i] = (byte)Integer.parseInt(st.nextToken());
	    }
	}
	else {
	    utility.trace("Incorrect IP address format: " + DEFAULT_IP);
	    System.exit(-1);
	}
	
	utility.trace("IP: " + DEFAULT_IP); 	

        try{
            inet_addr = InetAddress.getByAddress(ip_bytes);
            socket = new DatagramSocket();
        }
        catch(SocketException ex)
        {
            utility.traceError("SocketException during send AT*CONFIG:", ex);
            
        }
        catch(UnknownHostException ex)
        {
            utility.traceError("UnknownHostException during send AT*CONFIG:", ex);
            
        }        
    }
    
    // @Override
    public void start()
    {
        send_at_cmd("AT*CONFIG=1,\"control:altitude_max\",\"1500\""); //altitude max 1.5m
        receiver = new ArDroneReceiver(navData,inet_addr,utility);
        //on lance le thread du receiver
        receiver.start();
        //super.start();
    }
    
    // @Override
    public void run() {
        /*while(send)
        {
            if(navData.getState(NavData.FLYING) && !isLanding)
            {
                if(!isMoving)
                {
                    utility.trace("===hovering===");
                    hovering();
                }else{
                    send_at_cmd("AT*PCMD=" + (seq++) + ",1," + 
                            cmd.getLeftRightTilt() + "," + //tourner à gauche/tourner à droite
                            cmd.getFrontBackTilt() + "," + //avant/arriere à droite
                            cmd.getVerticalSpeed() + "," + //haut/bas
                            cmd.getAngularSpeed()); //rotation gauche/droite
                    
                }
            }
            utility.threadSleep(DELAY_IN_MS);
        }*/
    }
    
    public void sendCommand(ArDroneCommand cmd)
    {
        this.cmd = cmd;
        switch(cmd.action)
        {
            case TAKE_OFF: takeOff(); break;
            case LANDING : landing(); break;
            case HOVERING: hovering(); break;
            case MOVING : isMoving = true; break;    
            default:break;
        }
    }
     
    public void send_at_cmd(String at_cmd) {
    	utility.trace("AT command: " + at_cmd);    	
	byte[] buffer = (at_cmd + "\r").getBytes();
	DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inet_addr, DEFAULT_PORT);
	try{
            socket.send(packet);
            utility.threadSleep(DELAY_IN_MS);
        }catch (java.io.IOException ex)
        {
            System.out.println(ex.getMessage());
        }
      	
    }

    public void SendStopBOOTSTRAP()
    {
          //specify now to exit of the BOOTSTRAP mode
          utility.trace("=>SendStopBOOTSTRAP to DEFAULT_PORT");
          send_at_cmd("AT*CONFIG=\"general:navdata_demo\",\"TRUE\"");
          utility.threadSleep(DELAY_IN_MS);
          send_at_cmd("AT*CTRL=0");
        
    }
    
    
    public void hovering()
    {
        isMoving = false;
        send_at_cmd("AT*PCMD=" + (seq++) + ",1,0,0,0,0");
    }
    
    public void landing()
    {
        isLanding = true;
        isMoving = false;
        send_at_cmd("AT*REF=" + (seq++) + ",290717696");
    }
    //a appeler avant le decollage pour indiquer au drone qu'il est 
    //su un plan horizontal de reference
    public void sendFlatTrim()
    {
        send_at_cmd("AT*FTRIM=" + (seq++) + ",");
    }
    
     public void takeOff()
    {
        int i=0;
        isLanding = false;
        isMoving = false;
        sendFlatTrim();
        while(!navData.getState(NavData.FLYING))
        {
            send_at_cmd("AT*REF=" + (seq++) + ",290718208");
            i++;
            if(i>200)
            {
               utility.trace(" takeOff timeout"); 
               break;
            }
        }    
        
    }
    
   
    
    
}
