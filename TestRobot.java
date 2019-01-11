package bc19;


import java.util.ArrayList;
import java.util.HashMap;

public class MyRobot extends BCAbstractRobot {
	public int turn;
	public int team;
	public ArrayList<Tuple> robotList;
	public ArrayList<Integer> fuelMines;
	public ArrayList<Integer> karbMines;

	
	
	
	
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
    		if (turn <7 || me.karbonite>20) {
    			log("Building a crusader.");
    			return buildUnit(SPECS.CRUSADER,0,1);
    		}
    	
    		
    	}

    	if (me.unit == SPECS.PILGRIM) {
    		if (turn == 1) {
    			log("I am pilgrim" + me.id);
                 
                //log(Integer.toString([0][getVisibleRobots()[0].castle_talk]));
    		}
    		//if its a pilgrim call findMine here
        	
    		
        	
    		boolean isFuel = true; 
    		
    		
    		if(pMine()!=null)
    		{
    			return pMine();
    		}
    		//MoveAction m = pilgrimMove(isFuel);    	
    		MoveAction m = findMove();
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
    		return findMove();
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
    
    /** finds the closest mine to robot
     *  x, y is current position of robot
     *  isFuel is true if finding a fuel mine, false if finding a karbonite mine
     *  return: array with x and y of nearest mine
     */
    public int[] findMine(int x, int y, boolean isFuel) {
    	 boolean[][] fm = getFuelMap();
    	 boolean[][] km = getKarboniteMap();
    	
    	 //closest mine and distance in units of r^2
    	 int[] mine = new int[2]; 
    	 int r2 = Integer.MAX_VALUE;
    	 
    	 
    	 //use a breadth-first search to find nearest mine within a 10x10 grid 
    	 int r = 1;
    	 while(r<5) {
    		 
      	 for(int i = -1*r; i<=r;i++)
    	 {
    		 for(int j = -1*r; j<=r; j++)
    		 {
    			
    			//if tile is a mine and is closest
    			if(isFuelMine(x+i,y+j)&&(i*i+j*j)<r2)
    			{
    				r2 = i*i+j*j;
    				mine = new int[] {x+1,y+j};
    				//return mine;
    			}
    		 }
    	 }
      	 if(r2<Integer.MAX_VALUE)
      	 {
      		 return mine;
      	 }
    	}
    	 
    	 
    	 return null;
    	 
    	 
    	 
    }
    
    
    
    // has to be a spot it can reach in one turn. if not, see if it can get as close to it as possible in one turn
    
    public MoveAction findMove()
    {
    
    	
    	//boolean[][] map = getPassableMap();
    	
    	int dx = (int)(Math.random()*3)-1;
		int dy = (int)(Math.random()*3)-1;
    	
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
    	int dx = 0;
    	int dy = 0;
    	//boolean[][] map = getPassableMap();
    	int[] mine = findMine(me.x,me.y,isFuel);
    	if(mine == null)
    	{
    		dx = (int)(Math.random()*3)-1;
    		dy = (int)(Math.random()*3)-1;
    		return move(dx,dy);
    	}
    	int signX = (int)(Math.signum(mine[0]-me.x));
    	int signY = (int)(Math.signum(mine[1]-me.y));
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
    			if(isPassable(me.x+i,me.y+j)&&!(i==0&&j==0))
    			{	
    				log("Build unit "+unit);
    				return buildUnit(unit,i,j);
    				}
    		}
    	}
    	return null;
    }
    
    public AttackAction findAttack()
    {
    	
    	return null; 
    }
    
    
    
}
