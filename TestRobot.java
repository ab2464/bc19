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

    	//Moves for castle
    	if (me.unit == SPECS.CASTLE) {
    		log("Turn " + turn);
    		
    		if(turn==1)
    		{
    			castleList.add(me.id);
    			
    		}
    		
    		/*
    		if(turn<5 && build(2)!=null)
    			return build(2);
    		if(turn<15 || me.karbonite>20)
    			return build(3);
    			*/
    		
    		if (turn < 4) {
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
                 
    			
    			//test
    			findMine(me.x,me.y,false);
    			
    			
                //log(Integer.toString([0][getVisibleRobots()[0].castle_talk]));
    		}
    		//if its a pilgrim call findMine here
        	
    		
        	
    		boolean isFuel = true; 
    		
    		
    		if(isFuelMine(me.x,me.y)||isKarboniteMine(me.x,me.y))
    		{
    			//return pMine();
    		}
   
    		MoveAction m = pilgrimMove(isFuel);    	
    		
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
    		AttackAction a = findAttack(16);
    		if(a!=null)
    		{
    			log("Attack!");
    			return a;
    		}
    		return findMove(9);
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
    
    /** Returns true if the specified square of the grid is passable.
     *  Returns false otherwise.
     */
    public boolean isPassable(int x, int y) {
    	boolean[][] m = getPassableMap();
    	if(x<0 || y<0 || x>m.length || y>m.length)
    	{
    		return false;
    	}
    	if (m[x][y] == true) {
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
    public boolean isFuelMine(int x, int y)
    {
    	boolean[][] fuel = getFuelMap(); 
    	if(isPassable(x,y) && fuel[x][y])
    	{
    		return true;
    	}
    	return false;
    }
    
    
    /** checks if point is a karbonite mine
     * 
     */
    public boolean isKarboniteMine(int x, int y)
    {
    	boolean[][] karb = getKarboniteMap(); 
    	if(isPassable(x,y) && karb[x][y])
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
    public int[] findMine(int x, int y, boolean isFuel) {
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
    			 if(km[i][j]&&d<r2)
	    			{
	    				r2 = d;
	    				mine = new int[] {x+i,y+j};
	    				//return mine;
	    			}
    		 }
    	 }
    	 /*
    	 while(r<5) {} 
      	 for(int i = -1*r; i<=r;i++)
    	 {
    		 for(int j = -1*r; j<=r; j++)
    		 {
	    		if(!visited[x+i][y+j])
	    		{
	    			//if tile is a mine and is closest
	    			if(isFuelMine(x+i,y+j)&&(i*i+j*j)<r2)
	    			{
	    				r2 = i*i+j*j;
	    				mine = new int[] {x+i,y+j};
	    				//return mine;
	    			}
	    			visited[x+i][y+j] = true;
	    		}
    		 }
    	 }
    	 
    	 */
      	 if(r2<Integer.MAX_VALUE)
      	 {
      		 log("Mine at ("+mine[0]+","+mine[1]);
      		 return mine;
      	 }
    	
    	
    	 
    	 
    	 return null;
    	 
    	 
    	 
    }
    
    
    
    // has to be a spot it can reach in one turn. if not, see if it can get as close to it as possible in one turn
    
    public MoveAction findMove(int range)
    {
    
    	
    	//boolean[][] map = getPassableMap();
    	
    	int dx = (int)(Math.random()*3)-1;
		int dy = (int)(Math.random()*3)-1;
    	
    	//while(!isPassable(me.x+dx,me.y+dy) && (dx*dx+dy*dy <=range)
    	while(!isPassable(me.x+dx,me.y+dy))
    	{
    		dx = (int)(Math.random()*3)-1;
    		dy = (int)(Math.random()*3)-1;
    	}
		return move(dx,dy);
    	
    	//return null;
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
    					dest = new int[] {r.x,r.y};
    					
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
    	
    	
    	
    	if(dest == null)
    	{
    		dx = (int)(Math.random()*3)-1;
    		dy = (int)(Math.random()*3)-1;
    		return move(dx,dy);
    	}
    	int signX = (int)(Math.signum(dest[0]-me.x));
    	int signY = (int)(Math.signum(dest[1]-me.y));
    	if(signX != 0)
    	{
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
    			if(isPassable(me.x+i,me.y+j)&&!(i==0&&j==0)&& robotMap[me.x+i][me.y+j]==0)
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
