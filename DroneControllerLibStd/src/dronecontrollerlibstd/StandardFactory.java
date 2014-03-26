/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dronecontrollerlibstd;
import dronecontrollerlib.*;
/**
 *
 * @author Seb
 */
public class StandardFactory extends Factory {
    
    public static final String KEYBOARD_TYPE = "KEYBOARD";

    public StandardFactory(Utility utility) {
        super(utility);
    }
    
    @Override
    public Controller createController(String type,Object[] args)
    {
        if(type == KEYBOARD_TYPE)
        {
            return new KeyboardController(utility,args);
        }
        
        return super.createController(type,args);
    }

    @Override
    public Utility createUtility() {
        return new StandardUtility();
    }
}
