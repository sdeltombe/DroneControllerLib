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

    protected ArDroneCommander commander;
    protected Controller controller;
    protected Factory factory;
    protected Utility utility;
    private ArDroneCommand cmd;
    
    public DCManager(Factory factory,String defaultControllerType, Object[] args)
    {
        this.factory = factory;
        this.utility = factory.createUtility();
        this.commander = ArDroneCommander.getInstance();
        this.controller = factory.createController(defaultControllerType, args);
        
    }
    public void run()
    {
       this.commander.init(factory.getUtility());
       this.commander.start();
       this.controller.subscribe(this);
       this.controller.connect();
    }
    
    @Override
    public void onReceivedCommand(ArDroneCommand cmd) {
        this.cmd = cmd.clone();
        commander.sendCommand(this.cmd);
    }
    
}
