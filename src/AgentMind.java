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

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.entities.Mind;
import codelets.behaviors.EatClosestApple;
import codelets.behaviors.getClosestJewel;
import codelets.behaviors.Forage;
import codelets.behaviors.GoToClosestJewel;
import codelets.behaviors.GoToClosestApple;
import codelets.motor.HandsActionCodelet;
import codelets.motor.LegsActionCodelet;
import codelets.motor.UnifiedActionCodelet;
import codelets.perception.AppleDetector;
import codelets.perception.ClosestAppleDetector;
import codelets.perception.JewelDetector;
import codelets.perception.ClosestJewelDetector;
import codelets.sensors.InnerSense;
import codelets.sensors.Vision;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import memory.CreatureInnerSense;
import support.MindView;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Thing;

/**
 *
 * @author rgudwin
 */
public class AgentMind extends Mind {
    
    private static int creatureBasicSpeed=3;
    private static int reachDistance=50;
    private Random rand;
    
        
	
                
                
	
    
    public AgentMind(Environment env) {
                super();
                
                
                
                
                // Declare Memory Objects
	        //MemoryObject legsMO;
	        //MemoryObject handsMO;
                MemoryObject unifiedMO;
                MemoryObject visionMO;
                MemoryObject innerSenseMO;
                MemoryObject closestAppleMO;
                MemoryObject closestJewelMO;
                MemoryObject knownApplesMO;
                MemoryObject knownJewelsMO;
                
                //Initialize Memory Objects
                //legsMO=createMemoryObject("LEGS", "");
		//handsMO=createMemoryObject("HANDS", "");
                unifiedMO=createMemoryObject("UNIFIED", "");
                
                List<Thing> vision_list = Collections.synchronizedList(new ArrayList<Thing>());
		visionMO=createMemoryObject("VISION",vision_list);
                CreatureInnerSense cis = new CreatureInnerSense();
		innerSenseMO=createMemoryObject("INNER", cis);
                
                Thing closestApple = null;
                closestAppleMO=createMemoryObject("CLOSEST_APPLE", closestApple);
                List<Thing> knownApples = Collections.synchronizedList(new ArrayList<Thing>());
                knownApplesMO=createMemoryObject("KNOWN_APPLES", knownApples);
                
                Thing closestJewel = null;
                closestJewelMO=createMemoryObject("CLOSEST_JEWEL", closestJewel);
                List<Thing> knownJewels = Collections.synchronizedList(new ArrayList<Thing>());
                knownJewelsMO=createMemoryObject("KNOWN_JEWELS", knownJewels);
                
                //Create and Populate MindViewer
//                MindView mv = new MindView("MindView");
//                mv.addMO(knownApplesMO);
//                mv.addMO(knownJewelsMO);
//                mv.addMO(visionMO);
//                mv.addMO(closestAppleMO);
//                mv.addMO(closestJewelMO);
//                mv.addMO(innerSenseMO);
//                //mv.addMO(handsMO);
//                //mv.addMO(legsMO);
//                mv.StartTimer();
//                mv.setVisible(true);
		
		// Create Sensor Codelets	
		Codelet vision=new Vision(env.c);
		vision.addOutput(visionMO);
                insertCodelet(vision); //Creates a vision sensor
		
		Codelet innerSense=new InnerSense(env.c);
		innerSense.addOutput(innerSenseMO);
                insertCodelet(innerSense); //A sensor for the inner state of the creature
		
		// Create Actuator Codelets
//		Codelet legs=new LegsActionCodelet(env.c);
//		legs.addInput(legsMO);
//                insertCodelet(legs);
//
//		Codelet hands=new HandsActionCodelet(env.c);
//		hands.addInput(handsMO);
//                insertCodelet(hands);
                
                Codelet unified=new UnifiedActionCodelet(env.c);
		unified.addInput(unifiedMO);
                insertCodelet(unified);
		
		// Create Perception Codelets
                Codelet ad = new AppleDetector();
                ad.addInput(visionMO);
                ad.addOutput(knownApplesMO);
                insertCodelet(ad);
                Codelet closestAppleDetector = new ClosestAppleDetector();
		closestAppleDetector.addInput(knownApplesMO);
		closestAppleDetector.addInput(innerSenseMO);
		closestAppleDetector.addOutput(closestAppleMO);
                insertCodelet(closestAppleDetector);
                
                // Create JEWEL Perception Codelets
                Codelet jd = new JewelDetector();
                jd.addInput(visionMO);
                jd.addOutput(knownJewelsMO);
                insertCodelet(jd);
                Codelet closestJewelDetector = new ClosestJewelDetector();
		closestJewelDetector.addInput(knownJewelsMO);
		closestJewelDetector.addInput(innerSenseMO);
		closestJewelDetector.addOutput(closestJewelMO);
                insertCodelet(closestJewelDetector);
                
		// Create Behavior Codelets
		Codelet goToClosestApple = new GoToClosestApple(creatureBasicSpeed,reachDistance);
		goToClosestApple.addInput(closestAppleMO);
		goToClosestApple.addInput(innerSenseMO);
		goToClosestApple.addOutput(unifiedMO);
                insertCodelet(goToClosestApple);
		
		Codelet eatApple=new EatClosestApple(reachDistance);
		eatApple.addInput(closestAppleMO);
		eatApple.addInput(innerSenseMO);
		eatApple.addOutput(unifiedMO);
                eatApple.addOutput(knownApplesMO);
                insertCodelet(eatApple);
                
                // Jewel codelets
                Codelet goToClosestJewel = new GoToClosestJewel(creatureBasicSpeed,reachDistance);
		goToClosestJewel.addInput(closestJewelMO);
		goToClosestJewel.addInput(innerSenseMO);
		goToClosestJewel.addOutput(unifiedMO);
                insertCodelet(goToClosestJewel);
		
		Codelet getJewel=new getClosestJewel(reachDistance);
		getJewel.addInput(closestJewelMO);
		getJewel.addInput(innerSenseMO);
		getJewel.addOutput(unifiedMO);
                getJewel.addOutput(knownJewelsMO);
                insertCodelet(getJewel);
                
                //Forage codelets
                Codelet forage=new Forage();
		forage.addInput(knownApplesMO);
                forage.addInput(knownJewelsMO);
                forage.addOutput(unifiedMO);
                insertCodelet(forage);
                
                // sets a time step for running the codelets to avoid heating too much your machine
//                for (Codelet c : this.getCodeRack().getAllCodelets())
//                    c.setTimeStep(50);
		
		// Start Cognitive Cycle
		start(); 
    }             
    
}
