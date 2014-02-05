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
public class ControllerFactory {
 
    public static final String KEYBOARD_TYPE = "KEYBOARD";
    
    
    public static Controller CreateController(String type,Object[] args)
    {
        if(type == KEYBOARD_TYPE)
        {
            return new KeyboardController(args);
        }
        return null;
    }
    
    
    
}
