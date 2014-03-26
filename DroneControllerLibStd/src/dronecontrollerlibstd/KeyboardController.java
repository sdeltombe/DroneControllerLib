/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dronecontrollerlibstd;
import dronecontrollerlib.*;
import java.awt.*; 
import java.awt.event.*;

/**
 *
 * @author Seb
 */
public class KeyboardController extends Controller implements KeyListener {

    private Frame frame;
    int seq = 1; //Send AT command with sequence number 1 will reset the counter
  

    public KeyboardController(Utility utility, Object[] args) {
        super(utility, args);
        
       
    }
    
    
    @Override
    public void connect() {
       
        this.frame = (Frame)args[0];
        //On s'ajoute en tant que keylistener sur la frame
        if(frame != null)
        {
            utility.trace("Connect => add the keylistener\n");
            this.frame.addKeyListener(this); 
        }else{
            utility.trace("KeyboardController::Error frame is invalid");
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
        utility.trace("Key: " + keyCode + " (" + KeyEvent.getKeyText(keyCode) + ")");
        try {
            control(keyCode);
        } catch (Exception ex) {
            utility.traceError("Error during keyPressed:",ex);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
      int keyCode = e.getKeyCode();
      /*if (keyCode >= 49 && keyCode <= 57) cmd.speed = (float)0.1; //Reset speed
      if (keyCode == 16) cmd.shift = false; //Shift off*/
    }

    
     //Control AR.Drone via AT commands per key code
    public void control(int keyCode) {
    	
    	utility.trace("control");
        switch (keyCode) {
     	    case 49:	//1
    	        //cmd.speed = (float)0.05;
    	    	break;
    	    case 50:	//2
    	        //cmd.speed = (float)0.1;
    	    	break;
    	    case 51:	//3
    	        //cmd.speed = (float)0.15;
    	    	break;
    	    case 52:	//4
    	        //cmd.speed = (float)0.25;
    	    	break;
    	    case 53:	//5
    	       // cmd.speed = (float)0.35;
    	    	break;
    	    case 54:	//6
    	        //cmd.speed = (float)0.45;
    	    	break;
    	    case 55:	//7
    	        //cmd.speed = (float)0.6;
    	    	break;
    	    case 56:	//8
    	        //cmd.speed = (float)0.8;
    	    	break;
    	    case 57:	//9
    	        //cmd.speed = (float)0.99;
    	    	break;
    	    case 16:	//Shift
    	        //cmd.shift = true;
    	    	break;
    	    case 38:	//Up
                /*if (cmd.shift) {
    	    	    cmd.action = DroneAction.GO_UP;//"Go Up (gaz+)";
    	    	    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1,0,0," + intOfFloat(cmd.speed) + ",0";
    	    	} else {
    	    	    cmd.action = DroneAction.GO_FORWARD;//"Go Forward (pitch+)";
    	    	    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1," + intOfFloat(cmd.speed) + ",0,0,0";
    	    	}*/
                cmd.action = DroneAction.MOVING;
                cmd.setFrontBackTilt((float) 0.50);
                
    	    	break;
    	    case 40:	//Down
    	    	/*if (cmd.shift) {
    	    	    cmd.action = DroneAction.GO_DOWN;//"Go Down (gaz-)";
    	    	    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1,0,0," + intOfFloat(-cmd.speed) + ",0";
    	    	} else {
    	    	    cmd.action = DroneAction.GO_BACKWARD;//"Go Backward (pitch-)";
    	    	    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1," + intOfFloat(-cmd.speed) + ",0,0,0";
    	    	}*/
                 cmd.action = DroneAction.MOVING;
                cmd.setFrontBackTilt((float) -0.50);
       	    	break;
    	    case 37:	//Left 
    	        /*if (cmd.shift) {
    	            cmd.action = DroneAction.ROTATE_LEFT;//"Rotate Left (yaw-)";
		    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1,0,0,0," + intOfFloat(-cmd.speed);
		} else {
		    cmd.action = DroneAction.GO_LEFT;//"Go Left (roll-)";
		    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1,0," + intOfFloat(-cmd.speed) + ",0,0";
		}*/
                cmd.action = DroneAction.MOVING;
                cmd.setAngularSpeed((float) -0.50);
    	    	break;
    	    case 39:	//Right
    		/*if (cmd.shift) {
    		    cmd.action = DroneAction.ROTATE_RIGHT;//"Rotate Right (yaw+)";
		    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1,0,0,0," + intOfFloat(cmd.speed);
		} else {
		   cmd.action = DroneAction.GO_RIGHT;//"Go Right (roll+)";
		    cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1,0," + intOfFloat(cmd.speed) + ",0,0";
		}*/
                 cmd.action = DroneAction.MOVING;
                cmd.setAngularSpeed((float) 0.50);
    	    	break;
    	    case 32:	//SpaceBar
    	    	cmd.action = DroneAction.HOVERING;
    	    	//cmd.at_cmd = "AT*PCMD=" + (seq++) + ",1,0,0,0,0";
    	    	break;
    	    case 33:	//PageUp
                cmd.action = DroneAction.TAKE_OFF;//"Takeoff";
    	    	//cmd.at_cmd = "AT*REF=" + (seq++) + ",290718208";
    	    	break;
    	    case 34:	//PageDown
    	    	cmd.action = DroneAction.LANDING;
    	    	//cmd.at_cmd = "AT*REF=" + (seq++) + ",290717696";
    	    	break;
    	    default:
    	    	break;
    	}

    	//utility.trace("Speed: " + cmd.speed);    	
    	utility.trace("Action: " + cmd.action); 
        //send cmd to the ArDroneController
	notifySubscriber();
    }
    
   
    
    
}
