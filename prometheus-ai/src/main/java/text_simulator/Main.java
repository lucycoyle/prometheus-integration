package text_simulator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
//import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//import java.awt.*;                //graphics library

public class Main {

	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the name of your config file: ");
		String nameJSON = scan.next();
		scan.close();
		System.out.println(nameJSON);
		
		//to read the JSON file and instantiate all objects
		
		JSONParser jsonParser = new JSONParser();
		
		try (FileReader reader = new FileReader(nameJSON))
		{
			Object obj = jsonParser.parse(reader);
			JSONObject worldObj = (JSONObject) obj;
			simulator(parseWorld(worldObj));		//calling simulator
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
	}
	private static GridWorld parseWorld(JSONObject world) 
	{
		JSONObject worldObject = (JSONObject) world.get("world");
		
		long w = (long) worldObject.get("width"); 						//reading the dimensions of the grid world 
		Integer width = (int) (long) w;
		//System.out.println(width);
		
		long h = (long) worldObject.get("height");
		Integer height = (int) (long) h;
		//System.out.println(height);
		
		GridWorld gWorld = new GridWorld(width, height);
		
		//getting objects and instantiating non-autonomous objects
		JSONArray worldObjArray = (JSONArray) worldObject.get("objects");
		Iterator objItr = worldObjArray.iterator();
		while (objItr.hasNext()) {
			JSONObject innerObj = (JSONObject) objItr.next();
//			System.out.println(innerObj.get("objType")+ " " + innerObj.get("objName") + " "+innerObj.get("objX") + " " 
//					+ innerObj.get("objY")+" " + innerObj.get("xTransl") + " " + innerObj.get("yTransl"));
//			
			String type = (String) innerObj.get("objType");
			String name = (String) innerObj.get("objName");
			
			long tmpX = (long) innerObj.get("objX");
			Integer x = (int) (long) tmpX;
			
			long tmpY = (long) innerObj.get("objY");
			Integer y = (int) (long) tmpY;
			
			long tmpXT = (long) innerObj.get("xTransl");
			Integer xTransl = (int) (long) tmpXT;
			
			long tmpYT = (long) innerObj.get("yTransl");
			Integer yTransl = (int) (long) tmpYT;
			
			if(type.equals("river")) {
				ImmovableObj imObj = new ImmovableObj(type, 
						name, x, y, xTransl, yTransl);				//creating new immovable object
				gWorld.addImmovObj(x, y, xTransl, yTransl, imObj);
			}
			else if(type.equals("rock")) {
				ImmovableObj imObj2 = new ImmovableObj(type, 
						name, x, y, xTransl, yTransl);
				gWorld.addImmovObj(x, y, xTransl, yTransl, imObj2);
			}
			else if(type.equals("grass")) {
				ImmovableObj imObj2 = new ImmovableObj(type, 
						name, x, y, xTransl, yTransl);
				gWorld.addImmovObj(x, y, xTransl, yTransl, imObj2);
			}
			else if(type.equals("bird")) {
				MovableObj movObj = new MovableObj(type, 
						name, x, y);
				gWorld.addMovObj(x, y, movObj);
			}
			else if(type.equals("grasshopper")) {
				MovableObj movObj = new MovableObj(type, 
						name, x, y);
				gWorld.addMovObj(x, y, movObj);
			}
		}
		
		//getting robots and instantiating them in the world 
		JSONArray worldRobArray = (JSONArray) worldObject.get("robots");    //getting robots 
		Iterator robItr = worldRobArray.iterator();
		Action fl = new Action("fl",-1,1,0);
		Action f = new Action("f",0,1,0);
		Action fr = new Action("fr",1,1,0);
		Action l = new Action("l",-1,0,0);
		Action r = new Action("r",1,0,0);
		Action bl = new Action("bl",-1,-1,0);
		Action b = new Action("b",-1,0,0);
		Action br = new Action("br",-1,1,0);
		Action rl = new Action("rl",0,0,-.25);
		Action rr = new Action("rr",0,0,.25);
		Action[] actions = {fl,f,fr,l,r,bl,b,br,rl,rr};

		Basic_Sensor fl_Im = new Basic_Sensor(new ImmovableObj(),-1, 0);
		Basic_Sensor f_Im = new Basic_Sensor(new ImmovableObj(), 0, 1);
		Basic_Sensor fr_Im = new Basic_Sensor(new ImmovableObj(), 1,0);

		Basic_Sensor[] sensors = {fl_Im,f_Im,fr_Im};
		double[][] data = {	//training data
				{ 1, 1, 1,	 1, 1, 1,0,0,0,0,0,0,0},
				{-1,-1,-1,	-1,-1,-1,0,0,0,0,0,1,1},
				{ 0, 0, 0,	 0, 0, 0,0,0,0,0,0,0,0},

				{-1,-1, 1,	-1,-1, 1,0,0,0,0,0,0,0},
				{-1, 1, 1,	-1, 1, 1,0,0,0,0,0,0,0},
				{ 1, 1,-1,	 1, 1,-1,0,0,0,0,0,0,0},
				{ 1,-1,-1,	 1,-1,-1,0,0,0,0,0,0,0},
				{-1, 1,-1,	-1, 1,-1,0,0,0,0,0,0,0},
				{ 1,-1, 1,	 1,-1, 1,0,0,0,0,0,0,0},

				{ 0, 0, 1,	 0, 0, 1,0,0,0,0,0,0,0},
				{ 0, 1, 1,	 0, 1, 1,0,0,0,0,0,0,0},
				{ 1, 0, 0,	 1, 0, 0,0,0,0,0,0,0,0},
				{ 1, 1, 0,	 1, 1, 0,0,0,0,0,0,0,0},
				{ 1, 0, 1,	 1, 0, 1,0,0,0,0,0,0,0},
				{ 0, 1, 0,	 0, 1, 0,0,0,0,0,0,0,0},

				{-1,-1, 0,	-1,-1, 0,0,0,0,0,0,0,0},
				{-1, 0, 0,	-1, 0, 0,0,0,0,0,0,0,0},
				{ 0,-1,-1,	 0,-1,-1,0,0,0,0,0,0,0},
				{ 0, 0,-1,	 0, 0,-1,0,0,0,0,0,0,0},
				{-1, 0,-1,	-1, 0,-1,0,0,0,0,0,0,0},
				{ 0,-1, 0,	 0,-1, 0,0,0,0,0,0,0,0}
		};
		while (robItr.hasNext()) {
			JSONObject innerRob = (JSONObject) robItr.next();
//			System.out.println(innerRob.get("robName")+ " " + innerRob.get("AI") + " "+innerRob.get("robX") + " " 
//					+ innerRob.get("robY"));
			
			String name = (String) innerRob.get("robName");
			String ai = (String) innerRob.get("AI");
			
			long tmpX = (long) innerRob.get("robX");
			Integer x = (int) (long) tmpX;
			
			long tmpY = (long) innerRob.get("robY");
			Integer y = (int) (long) tmpY;
			
			//Robot robot = new Robot(name, x, y, false);
			//gWorld.addRobot(x, y, robot);
			
			Drone drone = new Drone(name, actions, sensors, gWorld, x, y, 0, false);
			gWorld.addDrone(x, y, drone);
			drone.trainDrone(data);
		}
		return gWorld;
	}
	private static void simulator(GridWorld world){
		world.createWorld(true);
		
		//getting all the data structures 
		
		final Robot[][] robots = world.getRobots();
		final ImmovableObj[][] immoveables = world.getImObj();
		final Drone[][] drones = world.getDrones();
		final ArrayList<Drone> droneList = world.getDroneList();
		final ArrayList<MovableObj> movList = world.getMovList();
		final MovableObj[][] movables = world.getMovObjs();
		final int[][] worldArray = world.getWorld();
		boolean c = false;
		
		for(int i = 0; i < 10; i++) {
			int k = 0;
			for(Drone drone : droneList) {
				int x0 = drone.getX();
				int y0 = drone.getY();
				System.out.println("old: " + x0 + "," + y0);
				int[][] visible = drone.getAllVisible();
				world.createWorld(false);		//not yet showing the visible squares
				try {
					int l = 0;
					for(MovableObj movObj : movList) {
					//	System.out.println("Inside movables");
						if (movObj.getType().equals("bird")) {
							//System.out.println("Inside bird");
							//bird moves east or west
							int x = movObj.getX();
							int y = movObj.getY();
							if(y-1 >= 0 && worldArray[x][y-1] == 0) {
								movObj.setX(x);
								movObj.setY(y-1);
								worldArray[x][y-1] = 2;
								movables[x][y-1] = movObj;
								world.setMove(x, y, x, y-1, movObj);
							}
							else if(y+1 < world.getHeight() && worldArray[x][y+1] == 0) {
								movObj.setX(x);
								movObj.setY(y+1);
								worldArray[x][y+1] = 2;
								movables[x][y+1] = movObj;
								world.setMove(x, y, x, y+1, movObj);
							}
							world.createWorld(false);
						}
						
						if (movObj.getType().equals("grasshopper")) {
							//bird moves east or west
							int x = movObj.getX();
							int y = movObj.getY();
							if(y-2 >= 0 && worldArray[x][y-2] == 0) {
								movObj.setX(x);
								movObj.setY(y-2);
								worldArray[x][y-2] = 2;
								movables[x][y-1] = movObj;
								world.setMove(x, y, x, y-2, movObj);
							}
							else if(y+2 < world.getHeight() && worldArray[x][y+2] == 0) {
								movObj.setX(x);
								movObj.setY(y+2);
								worldArray[x][y+2] = 2;
								movables[x][y+2] = movObj;
								world.setMove(x, y, x, y+2, movObj);
							}
							world.createWorld(false);
						}
						try {
							TimeUnit.SECONDS.sleep(1);
						}
						catch(InterruptedException e) {
							System.out.println("Wait issue");
						}
						l++;
					}
					drone.makeDecision();
				}
				catch(indecisiveException e) {
					drone.setMistake(false);
					System.out.println("Indecisive decision");
					break;
				}
				int x1 = drone.getX();
				int y1 = drone.getY();
				double direction = drone.getDirection();
				try {
					System.out.println(x1 + "," + y1);
					if(0 <= y1 && y1 < world.getWidth() && 0 <= x1 && x1 < world.getHeight() && worldArray[x1][y1] == 1) {
				
						//is immovable object
						drone.setMistake(true);
						crashException crash = new crashException("You crashed: immovable object");
						throw crash;
					
					}
					else if(0 <= y1 && y1 < world.getWidth() && 0 <= x1 && x1 < world.getHeight() && worldArray[x1][y1] == 2) {
					
						//is movable object
						drone.setMistake(true);
						crashException crash = new crashException("You crashed: movable object");
						throw crash;
				
					}
					else if(0 <= y1 && y1 < world.getWidth() && 0 <= x1 && x1 < world.getHeight() && worldArray[x1][y1] == 3) {
						
						//is robot object
						drone.setMistake(true);
						crashException crash = new crashException("You crashed: robot object");
						throw crash;
				
					}
					else if (0 <= y1 && y1 < world.getWidth() && 0 <= x1 && x1 < world.getHeight() && worldArray[x1][y1] == 0) {
						worldArray[x1][y1] = 3;
						drones[x1][y1] = drone;
						drone.setX(x1);
						drone.setY(y1);
						if(x0 != x1 || y0 != y1) {
							System.out.println(x0 + "," + y0);
							worldArray[x0][y0] = 0;
							drones[x0][y0] = null;
						}
						drone.setDirection(direction);
					}
					else if (y1 < 0 || y1 >= world.getHeight() || x1 < 0 || x1 >= world.getWidth()) {
						//out of bounds so do nothing
						drone.setX(x0);
						drone.setY(y0);
						worldArray[x0][y0] = 3;
						drones[x0][y0] = drone;
					}
					try {
						TimeUnit.SECONDS.sleep(1);
					}
					catch(InterruptedException e) {
						System.out.println("Wait issue");
					}
				}
				catch(crashException e) {
					System.out.println("Crash at (" + x1 + "," + y1 + ")");
					c = true;
					break;
				}
				if(c == false)
					k++;
				else
					break;
			}
			System.out.println(i);
		}
		
	
		/* runs the robots without the drones
		for(int k = 0; k < 10; k++) {	// 10 iterations; at each iteration, the robot moves one step in a random direction
			for(int i = 0; i < world.getWidth(); i++) {
				for(int j = 0; j < world.getHeight(); j++) {
					if(worldArray[i][j] == 3) {		//a robot is in this position
						double direction = robots[i][j].randDirection();
						robots[i][j].setDirection(direction);
						if (direction < 0.25) {			//north
							if(i == 0 || worldArray[i-1][j] != 0) {	//contains an object or cannot move because at border
								System.out.println("Inside if of north");
								world.getRobot(i, j).setMistake(true);
								world.createWorld(false);
							}
							else {	//empty spot and robot can move
								Robot newRob = new Robot(robots[i][j].getName(), i-1, j, false);
								world.updateWorld(newRob, i-1, j, i, j);
								world.createWorld(false);
							}
							try{
								TimeUnit.SECONDS.sleep(1);
							}
							catch(InterruptedException e){
								System.out.println("Wait issue");
							}		
						}
						else if (direction < 0.5) {			//east
							if(j == (world.getHeight() - 1) || worldArray[i][j+1] != 0) {	//contains an object
								world.getRobot(i, j).setMistake(true);
								world.createWorld(false);
							}
							else {	//empty spot and robot can move
								Robot newRob = new Robot(robots[i][j].getName(), i, j+1, false);
								world.updateWorld(newRob, i, j+1, i, j);
								world.createWorld(false);
							}
							try{
								TimeUnit.SECONDS.sleep(1);
							}
							catch(InterruptedException e){
								System.out.println("Wait issue");
							}	
						}
						else if (direction < 0.75) {			//south
							if(i == (world.getWidth()-1) || worldArray[i+1][j] != 0) {	//contains an object
								world.getRobot(i, j).setMistake(true);
								world.createWorld(false);
							}
							else {	//empty spot and robot can move
								Robot newRob = new Robot(robots[i][j].getName(), i+1, j, false);
								world.updateWorld(newRob, i+1, j, i, j);
								world.createWorld(false);
							}
							try{
								TimeUnit.SECONDS.sleep(1);
							}
							catch(InterruptedException e){
								System.out.println("Wait issue");
							}	
						}
						else if (direction < 1) {			//west
							if(j == 0 || worldArray[i][j-1] != 0) {	//contains an object
								world.getRobot(i, j).setMistake(true);
								world.createWorld(false);
							}
							else {	//empty spot and robot can move
								Robot newRob = new Robot(robots[i][j].getName(), i, j-1, false);
								world.updateWorld(newRob, i, j-1, i, j);
								world.createWorld(false);
							}
							try{
								TimeUnit.SECONDS.sleep(1);
							}
							catch(InterruptedException e){
								System.out.println("Wait issue");
							}	
						}
					}
				}
			}
		} */
		
		
	} 
	
}