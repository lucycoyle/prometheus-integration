//robot with the AI

import java.util.concurrent.TimeUnit;

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
		int i = 0;
		double[] inputs = new double[this.sensors.length];
		for(Basic_Sensor sensor : this.sensors) {
			int[][] visible = getVisible(sensor);
			inputs[i] = sensor.score(world, visible);
			i++;
		}
		this.perceptronMatrix.setInputs(inputs);
		try {
			Action decision = this.perceptronMatrix.makeDecision();
			takeAction(decision);
		}
		catch(indecisiveException e) {
			throw e;
		}
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
