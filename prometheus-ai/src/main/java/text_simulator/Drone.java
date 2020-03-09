package text_simulator;
//robot with the AI

import java.util.Iterator;

import com.google.inject.*;

import interfaces.Tuple;
import interfaces.Tuples;
import prometheus.api.Prometheus;
import prometheus.guice.PrometheusModule;


public class Drone {
	public PerceptronMatrix perceptronMatrix;
	private String name;
	private int xCoord;
	private int yCoord;
	private double direction;
	private GridWorld world;
	private Basic_Sensor[] sensors;
	private Action[] actions;
	private boolean mistake;
	private Prometheus prometheus;

	
	public Drone(String name, Action[] actions, Basic_Sensor[] sensors, GridWorld world, int x, int y, int direction, boolean mistake) {
		//constructor
		
		this.name = name;
		this.actions = actions;
		this.sensors = sensors;
		this.world = world;
		this.xCoord=x;
		this.yCoord=y;
		this.direction=direction;
		this.mistake = mistake;
		Robot drone = new Robot(this.name, xCoord, yCoord, false);		//creating robot first and then adding AI later
	
		drone.setDirection(direction);
		//this.world.addRobot(xCoord, yCoord, drone);
		this.perceptronMatrix = new PerceptronMatrix(this.sensors, this.actions);
		this.prometheus = Guice.createInjector(new PrometheusModule()).getInstance(Prometheus.class);
		
	}

	//setter and getter methods
	public String getName() {
		return this.name;
	}
	
	public int getX() {
		return this.xCoord;
	}
	
	public int getY() {
		return this.yCoord;
	}
	
	public double getDirection() {
		return this.direction;
	}
	
	public boolean getMistake() {
		return this.mistake;
	}
	
	public void setMistake(boolean m) {
		this.mistake = m;
	}
	
	public void setX(int x) {
		this.xCoord = x;
	}
	
	public void setY(int y) {
		this.yCoord = y;
	}
	
	public void setDirection(double dir) {
		this.direction = dir;
	}
	
	public void addSensor(Basic_Sensor sensor, double[][] training_data) {
		Basic_Sensor[] sensor_append = new Basic_Sensor[this.sensors.length+1];
		int i = 0;
		for(Basic_Sensor s : this.sensors) {
			sensor_append[i] = s;
			i++;
		}
		sensor_append[sensor_append.length-1] = sensor;
		this.perceptronMatrix.addSensor(sensor, training_data);
	}
	
	public void addAction(Action action, double[][] training_data) {
		Action[] action_append = new Action[this.actions.length+1];
		int i = 0;
		for(Action a : this.actions) {
			action_append[i] = a;
			i++;
		}
		action_append[action_append.length-1] = action;
		this.actions = action_append;
		this.perceptronMatrix.addAction(action, training_data);
	}
	
	//trains the drone's perceptron matrix
	public void trainDrone(double[][] training_data) {
		this.perceptronMatrix.learnAllWeights(training_data);
	}
	
	//gets the visible x, y coordinates based on drone's current orientation for one sensor
	public int[][] getVisible(Basic_Sensor sensor) {
		int [][] visible = new int[this.sensors.length][2];		//2 by 2 matrix
		int i = 0;
		for (int x : sensor.getX()) {
			for(int y : sensor.getY()) {
				if (this.direction < 0.25) {	//facing north
					visible[i][0] = this.xCoord + x;
					visible[i][1] = this.yCoord - y;
				}
				else if(this.direction < 0.5) {	//facing east
					visible[i][0] = this.xCoord + y;
					visible[i][1] = this.yCoord + x;
				}
				else if (this.direction < 0.75) {	//facing south
					visible[i][0] = this.xCoord - x;
					visible[i][1] = this.yCoord + y;
				}
				else if(this.direction < 1.0) {	//facing west
					visible[i][0] = this.xCoord - y;
					visible[i][1] = this.yCoord - x;
				}
			}
			i++;
		}
		return visible;
	}
	
	//gets all visible coordinates for the list of sensors
	public int[][] getAllVisible() {
		int [][] visible = new int[this.sensors.length][2];		//2 by 2 matrix
		int i = 0;
		for (Basic_Sensor sensor : this.sensors) {
			for(int x : sensor.getX()) {
				for(int y : sensor.getY()) {
					if (this.direction < 0.25) {	//facing north
						visible[i][0] = this.xCoord + x;
						visible[i][1] = this.yCoord - y;
					}
					else if(this.direction < 0.5) {	//facing east
						visible[i][0] = this.xCoord + y;
						visible[i][1] = this.yCoord + x;
					}
					else if (this.direction < 0.75) {	//facing south
						visible[i][0] = this.xCoord - x;
						visible[i][1] = this.yCoord + y;
					}
					else if(this.direction < 1.0) {	//facing west
						visible[i][0] = this.xCoord - y;
						visible[i][1] = this.yCoord - x;
					}
				}
			}
			i++;
		}
		return visible;		
	}
	
	//uses visible 2d array as inputs for perceptron matrix. 
	//matrix then makes a decision and outputs an action for the drone, which
	//the drone takes. If the drone is indecisive, will throw an exception
	public void makeDecision() throws indecisiveException {
		
		Tuples t = new Tuples();
		
		
		int i = 0;
		int[] inputs = new int[this.sensors.length];
		String[] labels= new String[this.sensors.length];
		for(Basic_Sensor sensor : this.sensors) {
			int[][] visible = getVisible(sensor);
			inputs[i] = (int)sensor.score(world, visible);
			labels[i]=sensor.getX().toString()+","+sensor.getY();
			i++;
		}

		
		t.add("Sensors score",labels,inputs);
		t = prometheus.think(t);
		System.out.println("prometheus think output:");
		
		Iterator<Tuple> iter= t.iterator();
 	   while(iter.hasNext()) {
 		Tuple tuple= iter.next();
		System.out.println(tuple.getLabel());
		}
		
		try {
			Action decision = convertToDecision(t);
			if(decision == null) {
				throw new indecisiveException("No decision found");
			}
			takeAction(decision);
		}
		catch(indecisiveException e) {
			throw e;
		}
	}
	
	private Action convertToDecision(Tuples t) throws indecisiveException{
		Iterator<Tuple> iter= t.iterator();
		while(iter.hasNext()) {
	 		Tuple tuple= iter.next();
	 		if(tuple.getLabel().equals("@move(right)")) {
	 			return this.actions[4];
	 		}
	 		else if(tuple.getLabel().equals("@move(left)")) {
	 			return this.actions[3];
	 		}
	 		else if(tuple.getLabel().equals("@move(front)")) {
	 			return this.actions[1];
	 		}
	 		else if(tuple.getLabel().equals("@move(back)")) {
	 			return this.actions[6];
	 		}
		}
		return null;
	}
	
	//takes an action and updates drone's position and orientation
	private void takeAction(Action action) {
		//If drone is not facing north, then have to translate what dx and dy means
		if (this.direction < 0.25) {
			this.xCoord += action.getXDelta();
			this.yCoord -= action.getYDelta();
		}
		else if(this.direction < 0.5) {
			this.yCoord += action.getXDelta();
			this.xCoord += action.getYDelta();
		}
		else if(this.direction < 0.75) {
			this.xCoord -= action.getXDelta();
			this.yCoord += action.getYDelta();
		}
		else if(this.direction < 0.75) {
			this.yCoord -= action.getXDelta();
			this.xCoord -= action.getYDelta();
		}
		this.direction += action.getDirectionDelta();
		int tmp = Math.floorMod((int) (100*this.direction), 100);
		this.direction = tmp/100.0;
	}
	
//runs the drones for n iterations	
}
