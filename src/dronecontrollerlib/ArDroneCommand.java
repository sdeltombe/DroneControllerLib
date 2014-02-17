/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dronecontrollerlib;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 *
 * @author Seb
 */



public class ArDroneCommand implements Cloneable {
    public DroneAction action;
    private float leftRightTilt;
    private float frontBackTilt;
    private float verticalSpeed;
    private float angularSpeed;
    private static FloatBuffer fb;
    private static IntBuffer ib;
    
    public ArDroneCommand()
    {
        ByteBuffer bb = ByteBuffer.allocate(4);
        fb = bb.asFloatBuffer();
        ib = bb.asIntBuffer();
    }
    /* accesseur */
    
    public int getLeftRightTilt()
    {
        return intOfFloat(leftRightTilt);
    }
    public int getFrontBackTilt()
    {
        return intOfFloat(frontBackTilt);
    }
     public int getVerticalSpeed()
    {
        return intOfFloat(verticalSpeed);
    }
      public int getAngularSpeed()
    {
        return intOfFloat(angularSpeed);
    }
    
    /*mutateur*/
    
    public void setLeftRightTilt(float leftRightTilt)
    {
        if(controlValue(leftRightTilt))
        {
            this.leftRightTilt = leftRightTilt;
        }else{
            //TODO throw Exception
        }
    }
     public void setFrontBackTilt(float frontBackTilt)
    {
        if(controlValue(frontBackTilt))
        {
            this.frontBackTilt = frontBackTilt;
        }else{
            //TODO throw Exception
        }
    }
      public void setVerticalSpeed(float verticalSpeed)
    {
        if(controlValue(verticalSpeed))
        {
            this.verticalSpeed = verticalSpeed;
        }else{
            //TODO throw Exception
        }
    }
       public void setAngularSpeed(float angularSpeed)
    {
        if(controlValue(angularSpeed))
        {
            this.angularSpeed = angularSpeed;
        }else{
            //TODO throw Exception
        }
    }
    
    private boolean controlValue(float value)
    {
        return (value >= -1 && value <= 1);
    }
    
    
    @Override
    public ArDroneCommand clone()
    {
        ArDroneCommand cmd = new ArDroneCommand();
        cmd.leftRightTilt = leftRightTilt;
        cmd.frontBackTilt = frontBackTilt;
        cmd.verticalSpeed = verticalSpeed;
        cmd.angularSpeed = angularSpeed;
        cmd.action = action;
        return cmd;
    }
      
    public static int intOfFloat(float f) {
        fb.put(0, f);
        return ib.get(0);
    }

    
    public static float floatOfInt(int i) {
        ib.put(0, i);
        return fb.get(0);
    }
}
