package bc19;

public class TestRobot extends BCAbstractRobot {
	public int turn;

    public Action turn() {
    	turn++;

    	//Moves for castle
    	if (me.unit == SPECS.CASTLE) {
    		if (turn == 1) {
    			log("Building a pilgrim.");
    			return buildUnit(SPECS.PILGRIM,1,0);
    		}
    		if (turn == 2) {
    			log("Building a crusader.");
    			return buildUnit(SPECS.CRUSADER,0,1);
    		}
    		
    	}

    	if (me.unit == SPECS.PILGRIM) {
    		if (turn == 1) {
    			log("I am pilgrim" + me.id);
                 
                //log(Integer.toString([0][getVisibleRobots()[0].castle_talk]));
    		}
    		int dx = 1;
    		int dy = 1;
    		MoveAction m = move(dx,dy)
    		return m;
    	}
    	
    	if (me.unit == SPECS.CHURCH) {
    		if (turn == 1) {
    			log("Building a crusader.");
    			return buildUnit(SPECS.CRUSADER,1,0);
    		}
    	}
    	
    	if (me.unit == SPECS.CRUSADER) {
    		if (turn == 1) {
    			log("I am crusader" + me.id);
    			//return buildUnit(SPECS.PILGRIM,1,0);
    		}
    		
    	}
    	
    	if (me.unit == SPECS.PROPHET) {
    		if (turn == 1) {
    			log("Prophet.");
    			//return buildUnit(SPECS.PILGRIM,1,0);
    		}
    	}
    	
    	if (me.unit == SPECS.PREACHER) {
    		if (turn == 1) {
    			log("Preacher");
    			//return buildUnit(SPECS.PILGRIM,1,0);
    		}
    	}
    	
    	return null;
    	
	}
    
    /** finds the closest mine to robot
     *  x, y is current position of robot
     */
    public  findMine(int x, int y) {
    	
    	 protected final boolean isMinHeap;
    	 protected VP[] d;
    	 protected int size;
    	 protected HashMap<E, Integer> map;
    	 
    	 
    }
    // has to be a spot it can reach in one turn. if not, see if it can get as close to it as possible in one turn
    //helper method to check if it's a passable spot
    public MoveAction findMove()
    {
    	boolean[][] map = getPassableMap();
    	return null;
    }
    
    
    
}