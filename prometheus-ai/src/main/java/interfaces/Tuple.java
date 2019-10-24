package interfaces;

public abstract class Tuple {
	// All AI knowledge structures will be based on this class.
	// Based on the following format: (label, parameters)
	// Where “label” is a string
	// Where “parameters” are either strings or integers
	// The integers range from -100 to +100 and are a score or
	// a measurement
	final int minScore = -100;
	final int maxScore = 100;
	String label; // The “label” from (label, params)
	String sParams[]; // The “parameters” from (label,params)
	int iParams[]; // int parameters from (label, params)
	abstract void setTuple(String label, String[] sParams, int[] iParams);
	abstract String getLabel();
	abstract String[] getSParams();
	abstract int[] getIParams();
}
