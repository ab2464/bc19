package bc19;

import bc19.Robot;
import java.util.ArrayList;
import java.util.HashMap;



public class MyRobot extends BCAbstractRobot {
	public int turn;
	public int color;
	public ArrayList<Integer> robotID;
	public HashMap<Integer,Integer> robotUnit;
	public ArrayList<Integer> fuelMines;
	public ArrayList<Integer> karbMines;
	public ArrayList<Integer> castleList;
	public int[] des = new int[] {-1, -1};
	public int[] spawn = new int[2];
	
	
	/** stores information on a robot
	 *  u is the unit type (1 to 5)
	 *  i is the unit id
	 *
	 */
	private class Tuple {
		private int u;
		private int i;
		public Tuple(int unit, int id) {
			u = unit;
			i = id;
		}
		public int getUnit() {
			return u;
		}
		public int getID() {
			return i;
		}
	}
	

	/** completes a turn of the robot
	 * 
	 * @return
	 */
    public Action turn() {
    	turn++;
    	boolean[][] fm = getFuelMap();
       	boolean[][] km = getKarboniteMap();
    	//Moves for castle
    	if (me.unit == SPECS.CASTLE) {
    		log("Turn " + turn);
    		if(turn==1) {
    			//castleList.add(me.id);	
    		}
//    		if(turn<5 && build(2)!=null)
//    			return build(2);
//    		if(turn<15 || me.karbonite>20)
//    			return build(3);
    		if (turn < 3 || (karbonite>=10 && karbonite<20 ) ) {
    			log("Building a pilgrim.");	
    			return build(2);    			
    			//return buildUnit(SPECS.PILGRIM,1,0);
    		}
    		if (karbonite>=20 && fuel > 1000 && getVisibleRobots().length<25) {
    			log("Building a crusader.");
    			log("Karbonite: "+karbonite+"  Fuel: "+ fuel);
    			return build(3);
    			//return buildUnit(SPECS.CRUSADER,0,1);
    		}
//    		if (karbonite>=30 && fuel > 1000) {
//    			log("Building a preacher.");
//    			log("Karbonite: "+karbonite+"  Fuel: "+ fuel);
//    			return build(5);
//    			//return buildUnit(SPECS.CRUSADER,0,1);
//    		}
    	}
    	if (me.unit == SPECS.PILGRIM) {
    		boolean isFuel = false;
    		
    		if (turn == 1) {
    			log("I am pilgrim " + me.id);
                spawn[1] = me.x;
                spawn[0] = me.y;
                des = new int[] {0,0};
                //log(Integer.toString([0][getVisibleRobots()[0].castle_talk]));
    		}
    		//if its a pilgrim call findMine here
        	GiveAction g = deposit();
    		
    		//if no destination, find a mine 
    		if(me.karbonite<20 && des[0]<0 && des[1]<0) {
    			des = findMine(me.y,me.x,isFuel);
    		}
    		//if on a mine, mine
    		if((km[me.y][me.x]&&me.karbonite<20) || (fm[me.y][me.x]&&me.fuel<100))
    		{
    			log("MINING ("+me.y+","+me.x+")");
    			return mine();
    		}
    		//if full of a resource, return home
    		if(me.karbonite ==20 || me.fuel ==100)
    		{
    			des[0] = spawn[0];
    			des[1] = spawn[1];
    		}
    		//if full and can deposit resources, deposit
    		if(isFull() && g!=null) {
    			log("Give resources Karbonite: "+me.karbonite+" Fuel: "+me.fuel);
    			des = findMine(me.y,me.x,isFuel);
    			return g;
    		}
    		//if back at base, find another mine
    		if(me.y==spawn[0] && me.x==spawn[1]) {
    			log("HOME");
    			des = findMine(me.y,me.x,isFuel);
    		}
    		
    		MoveAction m = findMove(4, des); 	
    		
    		//TESTING MOVE
    		if(m==null) {
    			des = findMine(me.y,me.x,isFuel);
    		} 
//    		{
//    			if(des[0] ==spawn[0])
//    			{
//    				des = new int[] {0,0};
//    			}
//    			else {
//    			des[0] = spawn[0];
//    			des[1] = spawn[1];
//    			}
//    			m = findMove(4, des); 
//    		}
    		if(fuel>10) {
    			return m;
    		}
    	}
    	
    	if (me.unit == SPECS.CHURCH) {
    		if (turn == 1  ) {
    			log("Building a pilgrim.");	
    			return build(2);    			
    			//return buildUnit(SPECS.PILGRIM,1,0);
    		}
    		if (karbonite>=20 && fuel > 1000) {
    			log("Building a crusader.");
    			log("Karbonite: "+karbonite+"  Fuel: "+ fuel);
    			return build(3);
    			//return buildUnit(SPECS.CRUSADER,0,1);
    		}
    	}
    	
    	if (me.unit == SPECS.CRUSADER) {
    		if (turn == 1) {
    			log("I am crusader " + me.id);
    			spawn[1] = me.x;
                spawn[0] = me.y;
    			//return buildUnit(SPECS.PILGRIM,1,0);
    		}
    		AttackAction a = findAttack(1,16);
    		if(a!=null && fuel>10) {
    			log("Attack!");
    			return a;
    		}
    		if(fuel>10) {
    			return findMove(9, des);
    		}
    	}
    	
    	if (me.unit == SPECS.PROPHET) {
    		if (turn == 1) {
    			log("Prophet " + me.id);
    			spawn[1] = me.x;
                spawn[0] = me.y;
    			//return buildUnit(SPECS.PILGRIM,1,0);
    		}
    		AttackAction a = findAttack(16,64);
    		if(a != null && fuel > 25) {
    			log("Attack!");
    			return a;
    		}
    		if(fuel>10) {
    			return findMove(4, des);
    		}
    	}
    	
    	if (me.unit == SPECS.PREACHER) {
    		if (turn == 1) {
    			log("Preacher "+ me.id);
    			spawn[1] = me.x;
                spawn[0] = me.y;
    			//return buildUnit(SPECS.PILGRIM,1,0);
    		}
    		AttackAction a = findAttack(1,16);
    		if(a != null && fuel > 15) {
    			log("Attack!");
    			return a;
    		}
    		if(fuel>12) {
    			return findMove(4, des);
    		}
    	}
    	return null;
	}
    
    
    /** Returns true if the specified square of the grid is passable and empty.
     *  Returns false otherwise.
     */
    public boolean isPassableEmpty(int y, int x) {
    	boolean[][] m = getPassableMap();
    	int[][] r = getVisibleRobotMap();
    	if(x<0 || y<0 || x>m.length-1 || y>m.length-1) {
    		return false;
    	}
    	if (m[y][x] == true && r[y][x] == 0) {
    		return true;
    	}
    	return false;
    }
    
    
    /** Returns true if the specified square of the grid is passable.
     *  Returns false otherwise.
     */
    public boolean isPassable(int y, int x) {
    	boolean[][] m = getPassableMap();
    	if(x<0 || y<0 || x>m.length-1 || y>m.length-1) {
    		return false;
    	}
    	if (m[y][x] == true) {
    		return true;
    	}
    	return false;
    }
    
    
    /** checks if point is a fuel mine
     *  
     */
    public boolean isFuelMine(int y, int x)
    {
    	boolean[][] fuel = getFuelMap(); 
    	if(fuel[y][x]) {
    		return true;
    	}
    	return false;
    }
    
    
    /** checks if point is a karbonite mine
     * 
     */
    public boolean isKarboniteMine(int y, int x)
    {
    	boolean[][] karb = getKarboniteMap(); 
    	if(karb[y][x]) {
    		return true;
    	}
    	return false;
    }
    
    
    /** 
     * finds the closest mine to robot
     *  x, y is current position of robot
     *  isFuel is true if finding a fuel mine, false if finding a karbonite mine
     *  return: array with x and y of nearest mine
     */
    public int[] findMine(int y, int x, boolean isFuel) {
   	 
    	boolean[][] m; 
    	if(isFuel)
    	{
    		m = getFuelMap();
    	}
    	else {
    		m = getKarboniteMap();
    	}
    	
    
   	 int[][] visible = getVisibleRobotMap();
   	 
   	 //closest mine and distance in units of r^2
   	 int[] mine = new int[2]; 
   	 int r2 = Integer.MAX_VALUE;
   	 int d = 0;
   	 
   	 //brute force find closest unoccupied mine
// 	 int r = 1;
   	 for(int i = 0; i<m.length;i++){
//   		 if((i-x)*(i-x)>r2) {
//   			 continue;
//   		 }
   		 for(int j = 0; j<m.length;j++) {
//   			 if((j-y)*(j-y)>r2) {
//   				 continue;
//   			 }
   			 d =(i-x)*(i-x)+(j-y)*(j-y);
   			 if(m[j][i]&&d<r2 && isPassable(j,i) && visible[j][i]<=0){
	    				r2 = d;
	    				mine[0] = j;
	    				mine[1] = i;
	    				//return mine;
   			 }
   		 }
   	 }
  
   	 if(r2<Integer.MAX_VALUE) {
   		 log("Mine at ("+mine[1]+","+mine[0]+")");
   		 return mine;
   	 }
   	 return null;
   	 
   }
    
    
    
//    /** finds the correct square?? DOES NOT CHECK CORNER SQUARES 
//     * 
//     */
//    public MoveAction adjustMove(int range, int[] dest, int dy, int dx) {
//    	if(dy*dy + dx*dx <= range && isPassableEmpty(dy, dx)) {
//			return move(dy, dx);
//		}
//		else { // recursive once you find the correct OR use helper function
//			int dy2 = dy - 1;
//			int dx2 = dx - 1;
//			if ((dy2*dy2 + dx*dx) >= (dy*dy + dx2*dx2)) {
//				
//			}
//			// if one is better than the other, check if it's passable and in the range
//			// if so, return it
//			// if not, check if the other is passable and in the range
//			// if so, return it
//			// if not, then recursively call 
//			
//		}
//    }
    
    
    /** picks a semi-random place to move to - default case of findMove just abstracted out
     * change parameters????????????
     */
    public MoveAction findMoveHelperRandom(int dy, int dx) {
    	int a;
		int b;
		if(Math.random()>0.5) {
			a = 1;
		}
		else {
			a = -1;
		}
		if(Math.random()>0.5) {
			b = 1;
		}
		else{
			b = -1;
		}
		for(int i = -1 ; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				dx = i*a;
				dy = j*b;
				if(!(i == 0 && j == 0) && isPassableEmpty(me.y + dy, me.x + dx)) {
		    		return move(dx,dy);
				}
			}
		}
		log("Can't move!");
		return null;
    }
    
   
    /**
     * 
     * @param range r^2 range of unit
     * @param dest destination in int[y][x] format
     * @return null if dest is in range and occupied, or at dest
     * 
     */
    // has to be a spot it can reach in one turn. if not, see if it can get as close to it as possible in one turn
    
    public MoveAction findMove(int range, int[] dest) //range = r^2, des = destination calculated by another function
    {
    	// if there is no unit to go to, it can move randomly 
    	//boolean[][] map = getPassableMap();
    	
    	int dx = 0;
    	int dy = 0;
    	
    	//default move at semi-random 
    	//check if there is a dest
    	if (dest[0] == -1 || dest[1] == -1)
    	{
    		return findMoveHelperRandom(dy, dx);
    	}
    	dy = dest[0] - me.y;
    	dx = dest[1] - me.x;
    	
//    KEEP EVERYTHING ABOVE - BASIC FUNCTIONALITY - WORKS
    	
    	
    	
    	//method 7

    	int ddy = dy;
    	int ddx = dx;
    	int minimum = dy*dy + dx*dx; // r^2
    	int count = 0;
    	//ArrayList<Integer[]> possibleMoves = new ArrayList<Integer[]>();
    	
    	//if unit is at destination return null
    	if(me.x == dest[1] && me.y == dest[0]){
    		 log("arrived!");
    		 return null; 
    	 }
    	//if unit is within range of destination move there directly
    	 if(dx*dx+dy*dy<=range) {
    		 if( isPassableEmpty(dest[1],dest[0])) {
    			 return move(dx,dy);
    		 }
    		 //if destination is in range and is not empty return null
//    		 else {
//    			 return null;
//    		 }
//    			
    	 }
    	// top right quadrant, including axis
    	if(dy >= 0 && dx >= 0) { 
    		for(int i = 0; i <= dy; i++) {
        		for(int j = 0; j <= dx; j++) {
        			if (isPassableEmpty(me.y + i, me.x + j) && (i*i + j*j) <= range) {
        				// [y, x, r^2]
        				int rr1 = (dy-i)*(dy-i) + (dx-j)*(dx-j);
        				if(rr1 < minimum) {
        					minimum = rr1;
        					ddy = i;
        					ddx = j;
        				}
        				//possibleMoves.add(new Integer[] {(Integer) i, (Integer) j, (Integer)(rr1)});
        				count++;
        			}
        		}
        	}
    	}
    	// bottom right quadrant 
    	else if (dy < 0 && dx >= 0) {
    		for(int k = dy; k != 1; k++) {
        		for(int l = 0; l <= dx; l++) {
        			if (isPassableEmpty(me.y + k, me.x + l) && (k*k + l*l) <= range) {
        				int rr2 = (dy-k)*(dy-k) + (dx-l)*(dx-l);
        				if(rr2 < minimum) {
        					minimum = rr2;
        					ddy = k;
        					ddx = l;
        				}
        				//possibleMoves.add(new Integer[] {(Integer) k, (Integer) l, (Integer)(rr2)});
        				count++;
        			}
        		}
        	}
    	}
    	// top left quadrant 
    	else if (dy >= 0 && dx < 0) {
    		for(int m = 0; m <= dy; m++) {
        		for(int n = dx; n != 1; n++) {
        			if (isPassableEmpty(me.y + m, me.x + n) && (m*m + n*n) <= range) {
        				int rr3 = (dy-m)*(dy-m) + (dx-n)*(dx-n);
        				if(rr3 < minimum) {
        					minimum = rr3;
        					ddy = m;
        					ddx = n;
        				}
        				//possibleMoves.add(new Integer[] {(Integer) m, (Integer) n, (Integer)(rr3)});
        				count++;
        			}
        		}
        	}
    	}
    	// bottom left quadrant 
    	else {
    		for(int p = dy; p != 1; p++) {
        		for(int q = dx; q != 1; q++) {
        			if (isPassableEmpty(me.y + p, me.x + q) && (p*p + q*q) <= range) {
        				int rr4 = (dy-p)*(dy-p) + (dx-q)*(dx-q);
        				if(rr4 < minimum) {
        					minimum = rr4;
        					ddy = p;
        					ddx = q;
        				}
        				//possibleMoves.add(new Integer[] {(Integer) p, (Integer) q, (Integer)(rr4)});
        				count++;
        			}
        		}
        	}
    	}
    	if (count == 0) {
    		return findMoveHelperRandom(dy, dx);
    	}
    	dy = ddy;
    	dx = ddx;
    	// what to do if it doesn't move???????????
    	
    	
    	
    	
    	
    	
    	//6. simple search of all nearby nodes 
    	/*
    	 *  * */
//    	 if(me.x == dest[1] && me.y == dest[0]){
//    		 log("arrived!");
//    		 return null; 
//    	 }
//    	 if(dx*dx+dy*dy<=range) {
//    		 if( isPassableEmpty(dest[1],dest[0])) {
//    			 return move(dx,dy);
//    		 }
//    	 }
//    	 int r2 = Integer.MAX_VALUE;
//    	 int d = r2;
//    	 int[] move = new int[2];
//    	 for(int i = -3;i<=3;i++) {
//    	 	if(i*i>range) {
//    	 		continue;
//    	 	}
//    	 	for(int j = -3;j<=3;j++) {
//    	 		if(j*j>range) {
//    	 			continue;
//    	 		}
//    	 		if(isPassableEmpty(me.y+j,me.x+i) && !(i==0 && j==0)) {
//    	 			d = (dx-i)*(dx-i)+(dy-j)*(dy-j);
//    	 			if(d<=r2 && (i*i+j*j)<=range) {
//    	 				move[0] = j;
//    	 				move[1] = i;
//    	 				r2=d;
//    	 			}
//    	 		}
//    	 	}
//    	 }
//    	 
//    	 dx = move[1];
//    	 dy = move[0];
//    	 if(dx!=0 || dy!=0) {
//    		 log("found move");
//    	 }
//    	 else {
//    		 return null;
//    	 }
    	
    	
//    	
//    	// 5. list of all possible moves with given range - find the one that is closest to destination
//    	int a = Math.abs(dest[0] - me.y);
//    	int b = Math.abs(dest[1] - me.x); 
//    	int minimum = a*a + b*b;
//    	int ddy = dest[0] - me.y;
//    	int ddx = dest[1] - me.x;
//    	//ArrayList<Integer[]> possibleMoves = new ArrayList<Integer[]>();
//    	// top right quadrant, including axis
//    	for(int i = 0; i <= a; i++) {
//    		for(int j = 0; j <= b; j++) {
//    			if ((i*i + j*j) <= range && isPassableEmpty(i, j)) {
//    				// [y, x, r^2]
//    				int rr1 = (dy-i)*(dy-i) + (dx-j)*(dx-j);
//    				if(rr1 < minimum) {
//    					minimum = rr1;
//    					ddy = i;
//    					ddx = j;
//    				}
//    				//possibleMoves.add(new Integer[] {(Integer) i, (Integer) j, (Integer)(rr1)});
//    			}
//    		}
//    	}
//    	// bottom right quadrant 
//    	for(int k = -a; k != 0; k++) {
//    		for(int l = 0; l <= b; l++) {
//    			if ((k*k + l*l) <= range && isPassableEmpty(k, l)) {
//    				int rr2 = (dy-k)*(dy-k) + (dx-l)*(dx-l);
//    				if(rr2 < minimum) {
//    					minimum = rr2;
//    					ddy = k;
//    					ddx = l;
//    				}
//    				//possibleMoves.add(new Integer[] {(Integer) k, (Integer) l, (Integer)(rr2)});
//    			}
//    		}
//    	}
//    	// top left quadrant
//    	for(int m = 0; m <= a; m++) {
//    		for(int n = -b; n != 0; n++) {
//    			if ((m*m + n*n) <= range && isPassableEmpty(m, n)) {
//    				int rr3 = (dy-m)*(dy-m) + (dx-n)*(dx-n);
//    				if(rr3 < minimum) {
//    					minimum = rr3;
//    					ddy = m;
//    					ddx = n;
//    				}
//    				//possibleMoves.add(new Integer[] {(Integer) m, (Integer) n, (Integer)(rr3)});
//    			}
//    		}
//    	}
//    	// bottom left quadrant
//    	for(int p = -a; p != 0; p++) {
//    		for(int q = -b; q != 0; q++) {
//    			if ((p*p + q*q) <= range && isPassableEmpty(p, q)) {
//    				int rr4 = (dy-p)*(dy-p) + (dx-q)*(dx-q);
//    				if(rr4 < minimum) {
//    					minimum = rr4;
//    					ddy = p;
//    					ddx = q;
//    				}
//    				//possibleMoves.add(new Integer[] {(Integer) p, (Integer) q, (Integer)(rr4)});
//    			}
//    		}
//    	}
//    	dy = ddy;
//    	dx = ddx;
//    	//int[] closest = findMin(possibleMoves);
//    	//dy = closest[0];
//    	//dx = closest[1]; 
//    	// what if the closest one is not possible? 
//    	
    	
    	
    	// 1. basic method
//    	while (Math.abs(dx) != 0 || Math.abs(dy) != 0) {
//    		if ( !(dx*dx + dy*dy <= range && isPassableEmpty(me.y + dy, me.x + dx))) {
//    			dy--;
//    			dx--;
//    		}
//    		else {
//    			break;
//    		}	
//    	}
    	
    	
    	// 2. furthest one can move iterative 
    	// intended direction while loop MAYBE FIX THIS???
//    	while(Math.abs(dx) != 0 || Math.abs(dy) != 0) {
//    		if ( !(dx*dx + dy*dy <= range && isPassableEmpty(me.y + dy, me.x + dx))) {
//    			int dy2 = dy - 1;
//    			int dx2 = dx - 1;
//    			if ((dy2*dy2 + dx*dx) >= (dy*dy + dx2*dx2)) {
//    				if(isPassableEmpty(me.y + dy2, me.x + dx)) {
//    					dy = dy2;
//    				}
//    				else if () {
//    					dx = dx2;
//    				}
//    			}
//    			else {
//    				dx = dx2;
//    			}
//    		}
//    	}
    	// another while loop for another direction
    	

//  	3. boolean indicator does not work - times out
//    	
//    	boolean change_dx = true;
//    	while ((!(dx*dx + dy*dy <= range) || !isPassableEmpty(me.x + dx, me.y + dy)) && (Math.abs(dx) > 0 || Math.abs(dy) > 0)) { 
//    		if (change_dx && dx > 0) {
//    			dx--;
//    			change_dx = false;
//    		}
//    		else if (!change_dx && dy > 0) {
//    			dy--;
//    			change_dx = true;
//    		}
//    		else if (change_dx && dx < 0) {
//    			dx++;
//    			change_dx = false;
//    		}
//    		else if (!change_dx && dy < 0) {
//    			dy++;
//    			change_dx = true;
//    		}
//    	}
//    	
//		
    	
    	// 4. random method - very basic - works
//		int dx = (int)(Math.random()*3)-1;
//		int dy = (int)(Math.random()*3)-1;
//    	
//    	//while(!isPassableEmpty(me.x+dx,me.y+dy) && (dx*dx+dy*dy <=range)
//    	while(!isPassableEmpty(me.x+dx,me.y+dy))
//    	{
//    		dx = (int)(Math.random()*3)-1;
//    		dy = (int)(Math.random()*3)-1;
//    	}
//		return move(dx,dy);
//    	
//    	//return null;
    	
    	
    	return move(dx,dy);
    }
    

    
    /**
     * 
     * @param unit
     * @return
     */
    public BuildAction build(int unit) {
    	int[][] robotMap = getVisibleRobotMap();
    	for(int i = -1; i <= 1; i++) {
    		for(int j = -1; j <= 1; j++) {
    			if(isPassableEmpty(me.y + j, me.x + i) && !(i == 0 && j == 0) && robotMap[me.y + j][me.x + i] == 0) {	
    				log("Build unit "+unit);
    				return buildUnit(unit,i,j);
    			}
    		}
    	}
    	return null;
    }
    
    
    /**
     * 
     * @return
     */
    public GiveAction deposit() {
    	Robot r = null;
    	int[][] robotMap = getVisibleRobotMap();
    	for(int i = -1; i <= 1; i++) {
    		for(int j = -1; j <= 1; j++) {
    			if(isPassable(me.y + j, me.x + i) && robotMap[me.y + j][me.x + i] > 0){	
    				r = getRobot(robotMap[me.y+j][me.x+i]);
    				if((r.unit==0 || r.unit == 1) && me.team==r.team ) {
    					return give(i,j,me.karbonite,me.fuel);
    				}
    			}
    		}
    	}
    	return null;
    }
    
    
    /**returns true if unit is at max carrying capacity for either fuel/karbonite
     * 
     */
    public boolean isFull() {
    	return me.fuel==100 || me.karbonite == 20;
    }
    
 
    /**
     * 
     * @param minRange
     * @param maxRange
     * @return
     */
    public AttackAction findAttack(int minRange, int maxRange){
    	AttackAction attack = null;
    	Robot[] robots = getVisibleRobots();
    	int d = 0;
    	//attack on first sight
    	for(int i = 0; i < robots.length; i++) {
    		Robot r = robots[i];
    		d = (r.x-me.x)*(r.x-me.x)+(r.y-me.y)*(r.y-me.y);
    		if(r.team != me.team && d<=maxRange && d>=minRange ) {
    			log("Attack! "+r.team);
    			attack =  attack(r.x-me.x,r.y-me.y);
    			return attack;
    		}
    	}
//    	//prioritize attacking units and castles in vicinity
//    	for(int i =0; i<robots.length;i++)
//    	{
//    		Robot r = robots[i];
//    		d = (r.x-me.x)*(r.x-me.x)+(r.y-me.y)*(r.y-me.y);
//    		if(r.team != me.team && d<=maxRange && d>=minRange )
//    		{
//    			log("Attack! "+r.team);
//    			
//    			attack =  attack(r.x-me.x,r.y-me.y);
//    			return attack;
//    		}
//    	}
    	return attack; 
    }
    
    
    /**returns true if the map is vertically symmetric
     * 
     */
    public boolean isVertical() {
    	boolean[][] map = getPassableMap();
    	for(int i = 0; i < 5; i++) {
    		for(int j = 0; j < map.length; j++) {
    			if(map[i][j] != map[map.length - 1 - i][j]){
    				return false;
    			}
    		}
    	}
    	return true;
    }
}
