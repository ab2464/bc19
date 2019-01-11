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
	

	
	

    public Action turn() {
    	turn++;
    	boolean[][] fm = getFuelMap();
       	boolean[][] km = getKarboniteMap();
    	
    	
    	//Moves for castle
    	if (me.unit == SPECS.CASTLE) {
    		log("Turn " + turn);
    		
    		if(turn==1)
    		{
    			//castleList.add(me.id);
    			
    		}
    		
    		/*
    		if(turn<5 && build(2)!=null)
    			return build(2);
    		if(turn<15 || me.karbonite>20)
    			return build(3);
    			*/
    		
    		if (turn < 4 || (karbonite>=10 && karbonite<20 ) ) {
    			log("Building a pilgrim.");	
    			return buildUnit(SPECS.PILGRIM,1,0);
    		}
    		if (karbonite>20) {
    			log("Building a crusader.");
    			return buildUnit(SPECS.CRUSADER,0,1);
    		}
    	
    		
    	}

    	if (me.unit == SPECS.PILGRIM) {
    		if (turn == 1) {
    			log("I am pilgrim" + me.id);
                spawn[1] = me.x;
                spawn[0] = me.y;
                //log(Integer.toString([0][getVisibleRobots()[0].castle_talk]));
    		}
    		//if its a pilgrim call findMine here
        	
    		
        	
    		boolean isFuel = true; 
    		
    		if(me.karbonite<20 && des[0]<0 && des[1]<0) {
    			//des = findMine(me.x,me.y,false);
    		}
    		if(km[me.y][me.x]&&me.karbonite<20)
    		{
    			log("MINING ("+me.y+","+me.x+")");
    			return mine();
    		}
    		if(me.karbonite ==20)
    		{
    			des = spawn;
    		}
    		MoveAction m = findMove(4, des); 	
    		
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
    			spawn[1] = me.x;
                spawn[0] = me.y;
    			//return buildUnit(SPECS.PILGRIM,1,0);
    		}
    		AttackAction a = findAttack(16);
    		if(a!=null)
    		{
    			log("Attack!");
    			return a;
    		}
    		return findMove(9, des);
    	}
    	
    	if (me.unit == SPECS.PROPHET) {
    		if (turn == 1) {
    			log("Prophet.");
    			spawn[1] = me.x;
                spawn[0] = me.y;
    			//return buildUnit(SPECS.PILGRIM,1,0);
    		}
    	}
    	
    	if (me.unit == SPECS.PREACHER) {
    		if (turn == 1) {
    			log("Preacher");
    			spawn[1] = me.x;
                spawn[0] = me.y;
    			//return buildUnit(SPECS.PILGRIM,1,0);
    		}
    	}
    	
    	return null;
    	
	}
    
    /** Returns true if the specified square of the grid is passable.
     *  Returns false otherwise.
     */
    public boolean isPassable(int y, int x) {
    	boolean[][] m = getPassableMap();
    	int[][] r = getVisibleRobotMap();
    	if(x<0 || y<0 || x>m.length || y>m.length)
    	{
    		return false;
    	}
    	if (m[y][x] == true && r[y][x] == 0) {
    		return true;
    	}
    	return false;
    }
   
    /*
    
    public ArrayList<Tuple> getRobotList() {
    	return robotList;
    }
    
    
    public void addRobotList(int unit, int id) {
    	robotList.add(new Tuple(unit, id));
    }
    
    */
    
    /** checks if point is a fuel mine
     *  
     */
    public boolean isFuelMine(int y, int x)
    {
    	boolean[][] fuel = getFuelMap(); 
    	if(fuel[y][x])
    	{
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
    	if(karb[y][x])
    	{
    		return true;
    	}
    	return false;
    }
    
    
    /** HAS OVERTIME ISSUES RN 
     * finds the closest mine to robot
     *  x, y is current position of robot
     *  isFuel is true if finding a fuel mine, false if finding a karbonite mine
     *  return: array with x and y of nearest mine
     */
    public int[] findMine(int y, int x, boolean isFuel) {
   	 boolean[][] fm = getFuelMap();
   	 boolean[][] km = getKarboniteMap();
   	 boolean[][] visited = new boolean[fm.length][fm.length];
   	
   	 //closest mine and distance in units of r^2
   	 int[] mine = new int[2]; 
   	 int r2 = Integer.MAX_VALUE;
   	 int d = 0;
   	 
   	 //use a breadth-first search to find nearest mine within a 10x10 grid 
   	 int r = 1;
   	 for(int i = 0; i<km.length;i++){
   		 if((i-x)*(i-x)>r2) {
   			 continue;
   		 }
   		 for(int j = 0; j<km.length;j++) {
   			 if((j-y)*(j-y)>r2) {
   				 continue;
   			 }
   			 d =(i-x)*(i-x)+(j-y)*(j-y);
   			 if(km[j][i]&&d<r2)
	    			{
	    				r2 = d;
	    				mine = new int[] {y+j,x+i};
	    				//return mine;
	    			}
   		 }
   	 }
  
     	 if(r2<Integer.MAX_VALUE)
     	 {
     		 log("Mine at ("+mine[1]+","+mine[0]);
     		 return mine;
     	 }
   	 
   	 return null;
   	 
   }
    
    
    // has to be a spot it can reach in one turn. if not, see if it can get as close to it as possible in one turn
    
    public MoveAction findMove(int range, int[] dest) //range = r^2, des = destination calculated by another function
    {
    	// if there is no unit to go to, it can move randomly 
    	//boolean[][] map = getPassableMap();
    	
    	int dx = 0;
    	int dy = 0;
    	
    	// check if passable???
    	if (dest[0] == -1 || dest[1] == -1) {
    		dx = (int)(Math.random()*3)-1;
    		dy = (int)(Math.random()*3)-1;
    		return move(dx,dy);
    	}
    	
    	dx = dest[1] - me.x;
    	dy = dest[0] - me.y;
  
    	boolean change_dx = true;
    	while ((!(dx*dx + dy*dy <= range) || !isPassable(me.x + dx, me.y + dy)) && (Math.abs(dx) > 0 || Math.abs(dy) > 0)) { 
    		if (change_dx && dx > 0) {
    			dx--;
    			change_dx = false;
    		}
    		else if (!change_dx && dy > 0) {
    			dy--;
    			change_dx = true;
    		}
    		else if (change_dx && dx < 0) {
    			dx++;
    			change_dx = false;
    		}
    		else if (!change_dx && dy < 0) {
    			dy++;
    			change_dx = true;
    		}
    	}
    	
		return move(dx,dy);
    	
//		int dx = (int)(Math.random()*3)-1;
//		int dy = (int)(Math.random()*3)-1;
//    	
//    	//while(!isPassable(me.x+dx,me.y+dy) && (dx*dx+dy*dy <=range)
//    	while(!isPassable(me.x+dx,me.y+dy))
//    	{
//    		dx = (int)(Math.random()*3)-1;
//    		dy = (int)(Math.random()*3)-1;
//    	}
//		return move(dx,dy);
//    	
//    	//return null;
    }

    
    
    //only for pilgrims - might integrate into findMove() later
    //has to be a spot it can reach in one turn. if not, see if it can get as close to it as possible in one turn
    
    public MoveAction pilgrimMove(boolean isFuel)
    {
    	//destination
    	int[] dest;
    	int dx = 0;
    	int dy = 0;
    	if(isFull())
    	{
    		Robot[] robots = getVisibleRobots();
    		int r2 = Integer.MAX_VALUE;
    		
    		for(int i =0;i<robots.length;i++)
    		{
    			Robot r = robots[i];
    			
    			/**/
    			if(r.unit==0 &&castleList.contains(r.id))
    			{
    				if((r.x-me.x)*(r.x-me.x)+(r.y-me.y)*(r.y-me.y)<r2)
    				{
    					r2 = (r.x-me.x)*(r.x-me.x)+(r.y-me.y)*(r.y-me.y);
    					dest = new int[] {r.y,r.x};
    					
    				}
    				
    			}
    		
    		}
    		/*
    		if(Math.abs(dest[0]-me.x)<2 && Math.abs(dest[1]-me.y)<2)
    		{
    			dx = dest[0]-me.x;
    			dy = dest[1]-me.y;
    			return give(int dx, int dy, me.karbonite, me.fuel);
    		}
    		*/
    	}
    	else
    	{
    		//dest = findMine(me.x,me.y,isFuel);
    	}
    	
    
    	//boolean[][] map = getPassableMap();
    	
    	
    	
    	if(dest == null) {
    		dx = (int)(Math.random()*3)-1;
    		dy = (int)(Math.random()*3)-1;
    		return move(dx,dy);
    	}
    	int signX = (int)(Math.signum(dest[1]-me.x));
    	int signY = (int)(Math.signum(dest[0]-me.y));
    	if(signX != 0) {
    		if(signY==0)
    		{
    			dx = signX*2;
    			dy = 0;
    		}
    		else
    		{
    			dx = signX*1;
    			dy = signY*1;
    		}
    	}
    	else
    	{
			dx = 0;
			dy = signY*2;
    	}
    	
    	return move(dx,dy);
    }
    
    public MineAction pMine()
    {
    	if((isFuelMine(me.x,me.y)&&me.fuel<=90) || (isKarboniteMine(me.x,me.y)&&me.karbonite<=18))
    	{
    		return mine();
    	}
    	return null;
    }
    
   
    public BuildAction build(int unit)
    {
    	int[][] robotMap = getVisibleRobotMap();
    	for(int i=-1;i<=1;i++)
    	{
    		for(int j = -1;j<=1;j++)
    		{
    			if(isPassable(me.x+i,me.y+j)&&!(i==0&&j==0)&& robotMap[me.y+j][me.x+i]==0)
    			{	
    				log("Build unit "+unit);
    				return buildUnit(unit,i,j);
    				}
    		}
    	}
    	return null;
    }
    
    /**returns true if unit is at max carrying capacity for either fuel/karbonite
     * 
     */
    public boolean isFull()
    {
    	return me.fuel==100 || me.karbonite == 20;
    }
    
 
    
    public AttackAction findAttack(int range)
    {
    	
    	Robot[] robots = getVisibleRobots();
    	for(int i =0; i<robots.length;i++)
    	{
    		Robot r = robots[i];
    		if(r.team != me.team && (r.x-me.x)*(r.x-me.x)+(r.y-me.y)*(r.y-me.y)<=range)
    		{
    			log("Attack! "+r.team);
    			
    			return attack(r.x-me.x,r.y-me.y);
    		}
    	}
    	return null; 
    }
    
    
    
}
