/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dronecontrollerlibstd;

import dronecontrollerlib.Utility;
import java.util.*;
/**
 *
 * @author Seb
 */
public class StandardUtility implements Utility {
    
    public StandardUtility()
    {
      
    }
    
    @Override
    public void trace(String trace) {
        Date now = new Date();
        System.out.println(now + "|" +  trace);
    }

    @Override
    public void traceError(String trace,Exception ex) {
        Date now = new Date();
        System.out.println(now + "|" +  trace);
        ex.printStackTrace();
    }

    @Override
    public void threadSleep(int milliseconds) {
       try{
              Thread.sleep(milliseconds);
        }catch(InterruptedException exInterrupted)
        {
           this.traceError("InterruptedIOException during receive message", exInterrupted);
        }
    }

  
    
}
