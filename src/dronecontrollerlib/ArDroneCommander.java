/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dronecontrollerlib;
import java.net.*;
import java.util.*;
import java.nio.*;
/**
 *
 * @author Seb
 */
public class ArDroneCommander {
    InetAddress inet_addr;
    DatagramSocket socket;
    int seq = 1; //Send AT command with sequence number 1 will reset the counter
    float speed = (float)0.1;
    boolean shift = false;
    FloatBuffer fb;
    IntBuffer ib;
    static final String DEFAULT_IP = "192.168.1.1";
    
    public ArDroneCommander()
    {
        StringTokenizer st = new StringTokenizer(DEFAULT_IP, ".");

	byte[] ip_bytes = new byte[4];
	if (st.countTokens() == 4){
 	    for (int i = 0; i < 4; i++){
		ip_bytes[i] = (byte)Integer.parseInt(st.nextToken());
	    }
	}
	else {
	    System.out.println("Incorrect IP address format: " + DEFAULT_IP);
	    System.exit(-1);
	}
	
	System.out.println("IP: " + DEFAULT_IP);
    	System.out.println("Speed: " + speed);    	

        ByteBuffer bb = ByteBuffer.allocate(4);
        fb = bb.asFloatBuffer();
        ib = bb.asIntBuffer();
        try{
            
            inet_addr = InetAddress.getByAddress(ip_bytes);
            socket = new DatagramSocket();
            socket.setSoTimeout(3000);

            send_at_cmd("AT*CONFIG=1,\"control:altitude_max\",\"1500\""); //altitude max 2m
            
        }catch(Exception ex)
        {
            
            
        }
    }
     public int intOfFloat(float f) {
        fb.put(0, f);
        return ib.get(0);
    }
    
    public void SendCommand(ArDroneCommand cmd)
    {
        send_at_cmd(cmd.at_cmd);
    }
     
    public void send_at_cmd(String at_cmd) {
    	System.out.println("AT command: " + at_cmd);    	
	byte[] buffer = (at_cmd + "\r").getBytes();
	DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inet_addr, 5556);
	try{
            socket.send(packet);
        }catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        
        }
	//socket.receive(packet); //AR.Drone does not send back ack message (like "OK")
	//System.out.println(new String(packet.getData(),0,packet.getLength()));   	
    }

    public void Landing()
    {
        try{
            //control(34);
        }catch(Exception ex)
        {
        
        }
    }
     public void TakeOff()
    {
        try{
            //control(33);
        }catch(Exception ex)
        {
        
        }
    }
    
   
    
    
}
