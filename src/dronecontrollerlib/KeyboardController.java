/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dronecontrollerlib;
import java.awt.*; 
import java.awt.event.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
/**
 *
 * @author Seb
 */
public class KeyboardController extends Controller implements KeyListener {

    private Frame frame;
    int seq = 1; //Send AT command with sequence number 1 will reset the counter
    FloatBuffer fb;
    IntBuffer ib;
    
    KeyboardController(Object[] args)
    {
        
        this.frame = (Frame)args[0];
        cmd = new ArDroneCommand();
       
        
    }
    
    
    @Override
    public void connect() {
        //On s'ajoute en tant que keylistener sur la frame
        if(frame != null)
        {
            System.out.printf("Connect => add the keylistener\n");
            this.frame.addKeyListener(this); 
        }else{
            System.out.printf("KeyboardController::Error frame is invalid");
        }
    }

    @Override
    public void listen() {
       //nothing to do here in this case
    }

    @Override
    public void disconnect() {
        //nothing to do here in this case
        this.frame.removeKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
      // not use in this case 
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println("Key: " + keyCode + " (" + KeyEvent.getKeyText(keyCode) + ")");
        try {
            control(keyCode);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error during keyPressed:" + ex.getMessage() + "\n");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
      int keyCode = e.getKeyCode();
      if (keyCode >= 49 && keyCode <= 57) cmd.speed = (float)0.1; //Reset speed
      if (keyCode == 16) cmd.shift = false; //Shift off
    }

    public int intOfFloat(float f) {
        fb.put(0, f);
        return ib.get(0);
    }
    
     //Control AR.Drone via AT commands per key code
    public void control(int keyCode) {
    	
    	 System.out.println("control");
        switch (keyCode) {
     	    case 49:	//1
    	        cmd.speed = (float)0.05;
    	    	break;
    	    case 50:	//2
    	        cmd.speed = (float)0.1;
    	    	break;
    	    case 51:	//3
    	        cmd.speed = (float)0.15;
    	    	break;
    	    case 52:	//4
    	        cmd.speed = (float)0.25;
    	    	break;
    	    case 53:	//5
    	        cmd.speed = (float)0.35;
    	    	break;
    	    case 54:	//6
    	        cmd.speed = (float)0.45;
    	    	break;
    	    case 55:	//7
    	        cmd.speed = (float)0.6;
    	    	break;
    	    case 56:	//8
    	        cmd.speed = (float)0.8;
    	    	break;
    	    case 57:	//9
    	        cmd.speed = (float)0.99;
    	    	break;
    	    case 16:	//Shift
    	        cmd.shift = true;
    	    	break;
    	    case 38:	//Up
    	    	if (cmd.shift) {
    	    	    cmd.action = "Go Up (gaz+)";
    	    	    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1,0,0," + intOfFloat(cmd.speed) + ",0";
    	    	} else {
    	    	    cmd.action = "Go Forward (pitch+)";
    	    	    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1," + intOfFloat(cmd.speed) + ",0,0,0";
    	    	}
    	    	break;
    	    case 40:	//Down
    	    	if (cmd.shift) {
    	    	    cmd.action = "Go Down (gaz-)";
    	    	    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1,0,0," + intOfFloat(-cmd.speed) + ",0";
    	    	} else {
    	    	    cmd.action = "Go Backward (pitch-)";
    	    	    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1," + intOfFloat(-cmd.speed) + ",0,0,0";
    	    	}
       	    	break;
    	    case 37:	//Left 
    	        if (cmd.shift) {
    	            cmd.action = "Rotate Left (yaw-)";
		    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1,0,0,0," + intOfFloat(-cmd.speed);
		} else {
		    cmd.action = "Go Left (roll-)";
		    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1,0," + intOfFloat(-cmd.speed) + ",0,0";
		}
    	    	break;
    	    case 39:	//Right
    		if (cmd.shift) {
    		    cmd.action = "Rotate Right (yaw+)";
		    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1,0,0,0," + intOfFloat(cmd.speed);
		} else {
		   cmd.action = "Go Right (roll+)";
		    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1,0," + intOfFloat(cmd.speed) + ",0,0";
		}
    	    	break;
    	    case 32:	//SpaceBar
    	    	cmd.action = "Hovering";
    	    	cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1,0,0,0,0";
    	    	break;
    	    case 33:	//PageUp
    	    	cmd.action = "Takeoff";
    	    	cmd.at_cmd = "AT*REF=" + (seq++) + ",290718208";
    	    	break;
    	    case 34:	//PageDown
    	    	cmd.action = "Landing";
    	    	cmd.at_cmd = "AT*REF=" + (seq++) + ",290717696";
    	    	break;
    	    default:
    	    	break;
    	}

    	System.out.println("Speed: " + cmd.speed);    	
    	System.out.println("Action: " + cmd.action); 
        //send cmd to the ArDroneController
	notifySubscriber();
    }
    
   
    
    
}
