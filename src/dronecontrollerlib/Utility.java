/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dronecontrollerlib;

/**
 *
 * @author Seb
 */
public interface Utility {
    public void trace(String trace);
    public void traceError(String trace,Exception ex);
    public void threadSleep(int milliseconds);
   
}
