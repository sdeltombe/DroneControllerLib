/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dronecontrollerlib;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


/**
 *
 * @author Seb
 */
public class ArDroneReceiver extends Thread {

    private DatagramSocket socket;
    private final Utility utility;
    private boolean listen=true;
    static final int NAVDATA_PORT = 5554;
    static final int DELAY_IN_MS = 30;
    
    static final int NAV_STATE_OFFSET = 4;
    
    final InetAddress inet_addr;
    ArDroneCommander commander;
    NavData navData;
    
    public ArDroneReceiver(NavData navData,InetAddress inet_addr,Utility utility)
    {
        this.inet_addr = inet_addr;
        this.utility = utility;
        this.commander = ArDroneCommander.getInstance();
        this.navData = navData;
        try{
            this.socket = new DatagramSocket();/*NAVDATA_PORT,inet_addr);*/
            socket.connect(inet_addr, NAVDATA_PORT);
            //configuration du timeout de reception
            this.socket.setSoTimeout(3000);
        }catch(SocketException ex)
        {
            utility.traceError("ArDroneReceiver error during socket creation:", ex);
        }
        
    }
    
    
    public void halt()
    {
        listen = false;
    }
    
    
    public void SendInitMessage()
    {
        //indicate to the drone to respond just to us
        byte[] bufInit = {0x01,0x00,0x00,0x00};
        DatagramPacket packetInit = new DatagramPacket(bufInit, bufInit.length);/*, inet_addr, NAVDATA_PORT);*/
        try{
            utility.trace("=>SendInitMessage to NAVDATA_PORT");
            socket.send(packetInit);
        }catch (java.io.IOException ex)
        {
            System.out.println(ex.getMessage());
        }	
    }
    
   
       
    
    public void connect()
    {
        SendInitMessage();
       
        utility.threadSleep(DELAY_IN_MS);
        commander.SendStopBOOTSTRAP();
        utility.threadSleep(DELAY_IN_MS);
        
    }
    private int get_int(byte[] data, int offset)
    {
        int value = 0;
        for(int i=3;i>=0;i--)
        {
            int shift = i * 8;
            value += (data[offset + i] & 0x000000FF) << shift;
        }
        return value;
    }
      
    
    @Override
    public void run() {
       
        byte[] buffer = new byte[65507];  
        connect();
        
     
        while(listen)
        {
            try{
                if(socket != null)
                {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    //utility.trace("NAVDATA received length :" + packet.getLength() + "\n");
                    int header = get_int(packet.getData(),0);
                    if(header != 0x55667788)
                    {
                         utility.trace("Error Parsing NavData header:" + header);
                    }else
                    {
                         //utility.trace("header ok");
                    }
                    
                    int state = get_int(packet.getData(),NAV_STATE_OFFSET);
                    //String message = new String(packet.getData(),0,packet.getLength());
                    //utility.trace("state:" + state);
                    navData.parseNavData(state);
                    if(navData.IsUpdate())
                    {
                        utility.trace("" + navData);
                    }else{
                        utility.trace("no change in navData...");
                    }
                    utility.threadSleep(2000);
                }
            }
            catch(java.io.IOException ex)
            {
                if(ex.getMessage().contains("Receive timed out"))
                {
                    connect();
                }
                utility.traceError("IOException during receive message", ex);
            }
            
        }
    }
    
}
