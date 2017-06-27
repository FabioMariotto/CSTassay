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

package codelets.sensors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import memory.CreatureInnerSense;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Thing;


/**
 *  This class reads information from this agent's state and writes it to an inner sense sensory buffer.
 * @author klaus
 *
 */

public class InnerSense extends Codelet {

	private MemoryObject innerSenseMO;
        private Creature c;
        private CreatureInnerSense cis;
        public List<String> Leaf = new ArrayList<>();

        
    public void removeLeaf(String color){
        if(Leaf.contains(color.toLowerCase())){
            Leaf.remove(color.toLowerCase());
        }
    }
        public InnerSense(Creature nc) {
		c = nc;
        }
        
	@Override
	public void accessMemoryObjects() {
		innerSenseMO=(MemoryObject)this.getOutput("INNER");
                cis = (CreatureInnerSense) innerSenseMO.getI();
	}
	
	public void proc() {
             cis.position = c.getPosition();
             cis.pitch = c.getPitch();
             cis.fuel = c.getFuel();
             cis.FOV = c.getFOV();
             int k=0;
             Leaf.clear();
             k=c.getLeaflets().get(0).getMissingNumberOfType("Green");
                for (int i = 0; i < k; i++) {
                Leaf.add("Green");
            }
                k=c.getLeaflets().get(0).getMissingNumberOfType("White");
                for (int i = 0; i < k; i++) {
                Leaf.add("White");
            }
                k=c.getLeaflets().get(0).getMissingNumberOfType("Magenta");
                for (int i = 0; i < k; i++) {
                Leaf.add("Magenta");
            }
                k=c.getLeaflets().get(0).getMissingNumberOfType("Blue");
                for (int i = 0; i < k; i++) {
                Leaf.add("Blue");
            }
                k=c.getLeaflets().get(0).getMissingNumberOfType("Yellow");
                for (int i = 0; i < k; i++) {
                Leaf.add("Yellow");
            }
               cis.LEAF = Leaf;  
                
            
             
             	}
        
        @Override
        public void calculateActivation() {
        
        }



}
