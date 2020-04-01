package interfaces;

import java.util.Arrays;

public class Tuple {

	final int minScore = -100;
	final int maxScore = 100;
	String label; // The “label” from (label, params)
	String sParams[]; // The “parameters” from (label,params)
	int iParams[]; // int parameters from (label, params)	
	
	public void setTuple(String label, String[] sParams, int[] iParams) {
		this.label = label;
		this.sParams = sParams;
		this.iParams = iParams;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public String[] getSParams() {
		return this.sParams;
	}
	
	public int[] getIParams() {
		return this.iParams;
	}
	
	@Override
	public String toString() {
		return this.getLabel() + Arrays.toString(this.getIParams()) + Arrays.toString(this.getSParams());
	}
}
