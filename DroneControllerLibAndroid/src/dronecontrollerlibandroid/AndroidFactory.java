/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dronecontrollerlibandroid;
import dronecontrollerlib.*;
/**
 *
 * @author Seb
 */
public class AndroidFactory extends Factory{

    public AndroidFactory(Utility utility) {
        super(utility);
    }
    @Override
    public Controller createController(String type,Object[] args)
    {
        return null;
    }

    @Override
    public Utility createUtility() {
        return new AndroidUtility();
    }
    
}
