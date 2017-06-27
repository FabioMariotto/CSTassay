/*****************************************************************************
 * Copyright 2007-2015 DCA-FEEC-UNICAMP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *    Klaus Raizer, Andre Paraense, Ricardo Ribeiro Gudwin
 *****************************************************************************/

package codelets.motor;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONException;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import static codelets.motor.LegsActionCodelet.log;
import java.util.Random;
import java.util.logging.Logger;
import ws3dproxy.model.Creature;

/**
 *  Hands Action Codelet monitors working storage for instructions and acts on the World accordingly.
 *  
 * @author klaus
 *
 */


public class UnifiedActionCodelet extends Codelet{

	private MemoryObject unifiedMO;
	private String previousUnifiedAction="";
        private double previousTargetx=0;
	private double previousTargety=0;
        double old_angle = 0;
        int k=0;
        private Creature c;
        private Random r = new Random();
        static Logger log = Logger.getLogger(UnifiedActionCodelet.class.getCanonicalName());

	public UnifiedActionCodelet(Creature nc) {
                c = nc;
	}
	
        @Override
	public void accessMemoryObjects() {
		unifiedMO=(MemoryObject)this.getInput("UNIFIED");
	}
	public void proc() {
            
                String command = (String) unifiedMO.getI();

		if(!command.equals("") && (!command.equals(previousUnifiedAction))){
			JSONObject jsonAction;
			try {
				jsonAction = new JSONObject(command);
                                //legs
                                int x=0,y=0;
                                String action=jsonAction.getString("ACTION");
                                if(action.equals("FORAGE")){
                                           //if (!comm.equals(previousLegsAction)) { 
                                           if (!command.equals(previousUnifiedAction)) 
                                                log.info("Sending Forage command to agent");
                                            try {  
                                                  c.rotate(2);     
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                    }
                                else if(action.equals("GOTO")){
                                    if (!command.equals(previousUnifiedAction)) {
                                        double speed=jsonAction.getDouble("SPEED");
                                        double targetx=jsonAction.getDouble("X");
                                        double targety=jsonAction.getDouble("Y");
                                        if (!command.equals(previousUnifiedAction))
                                            log.info("Sending move command to agent: ["+targetx+","+targety+"]");
                                        try {
                                             c.moveto(speed, targetx, targety);
                                        } catch(Exception e) {
                                            e.printStackTrace();
                                        }
                                        previousTargetx=targetx;
                                        previousTargety=targety;
                                    }

                                } 
//                                else {
//                                    log.info("Sending stop command to agent");
//                                    try {
//                                         c.moveto(0,0,0);
//                                    } catch(Exception e) {
//                                        e.printStackTrace();
//                                    }  
//                                }
                                //end legs
                                else if(jsonAction.has("ACTION") && jsonAction.has("OBJECT")){
					//String action=jsonAction.getString("ACTION");
					String objectName=jsonAction.getString("OBJECT");
					if(action.equals("PICKUP")){
                                                try {
                                                 c.putInSack(objectName);
                                                } catch (Exception e) {
                                                    
                                                } 
						log.info("Sending Put In Sack command to agent:****** "+objectName+"**********");							
						
						
						//							}
					}
					if(action.equals("EATIT")){
                                                try {
                                                 c.eatIt(objectName);
                                                } catch (Exception e) {
                                                    
                                                }
						log.info("Sending Eat command to agent:****** "+objectName+"**********");							
					}
					if(action.equals("BURY")){
                                                try {
                                                 c.hideIt(objectName);
                                                } catch (Exception e) {
                                                    
                                                }
						log.info("Sending Bury command to agent:****** "+objectName+"**********");							
					}
				}
                                else {
                                    log.info("Sending stop command to agent");
                                    try {
                                         c.moveto(0,0,0);
                                    } catch(Exception e) {
                                        e.printStackTrace();
                                    }  
                                }
//                                else if (jsonAction.has("ACTION")) {
//                                    int x=0,y=0;
//                                    String action=jsonAction.getString("ACTION");
//                                    if(action.equals("FORAGE")){
//                                                try {
//                                                      x = r.nextInt(600);
//                                                      y = r.nextInt(800);
//                                                 c.moveto(1, x,y );
//                                                } catch (Exception e) {
//                                                    
//                                                }
//						System.out.println("Sending Forage command to agent:****** ("+x+","+y+") **********");							
//					}
//                                }
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
//		System.out.println("OK_hands");
		previousUnifiedAction = (String) unifiedMO.getI();
	}//end proc

    @Override
    public void calculateActivation() {
        
    }


}
