/* From Chris's project, with minor modifications.
"This class is used to give actions to the drone.
Each action has a name associated with it, how that action changes the robot's position, and how it changes it's direction.
All values assume that the robot currently has direction=0 (i.e the drone is facing due north)

An example action would be:
name = "Forward Left"
x_delta = "-1"
y_delta = "1"
direction_delta = "0"

Alternatively, an action can throw an exception to the drone saying that it is in a situation that it does not know what to do.
This is used to be able to force the robot to learn."
 */

public class Action {
	private String name;
	private int x_delta = 0;
	private int y_delta = 0;
	private double direction_delta = 0;
	private indecisiveException exception;
	
	public Action(String name, int x, int y, double dir) {
		this.name = name;									// requires name and delta values, but not exception
		this.x_delta = x;
		this.y_delta = y;
		this.direction_delta = dir;
		this.exception = null;
	}
	public Action(String exception_message){	//constructor for exception action
		this.name = "Exception";				// only requires the message the exception delivers to initialize
		this.exception = new indecisiveException(exception_message);
	}
	public void exception() throws indecisiveException{ //throws the action's exception
		throw this.exception;
	}
	// getter methods
	public int getXDelta(){
		return x_delta;
	}
	public int getYDelta(){
		return y_delta;
	}
	public double getDirectionDelta(){
		return direction_delta;
	}

	public String getName() {
		return this.name;
	}
}