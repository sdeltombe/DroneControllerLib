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
public class DCManager implements ISubscriber {

    ArDroneCommander commander;
    Controller controller;
    public DCManager(String defaultControllerType, Object[] args)
    {
        commander = new ArDroneCommander();
        controller = ControllerFactory.CreateController(defaultControllerType, args);
        controller.subscribe(this);
        controller.connect();
    }
    
    
    @Override
    public void onReceivedCommand(ArDroneCommand cmd) {
        commander.SendCommand(cmd);
    }
    
}
