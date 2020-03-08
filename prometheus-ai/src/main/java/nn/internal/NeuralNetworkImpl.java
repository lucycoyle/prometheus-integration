package nn.internal;

import javax.inject.Inject;

import interfaces.SensorInput;
import interfaces.Tuples;
import nn.api.NeuralNetwork;

/**
 * Implementation of the NN.
 */
class NeuralNetworkImpl implements NeuralNetwork, SensorInput {
	int numSensors = 5;
	
	@Inject
    NeuralNetworkImpl() {
    }
   
    public void receiveDataStream(int nnID, int nnStruct, double data[]) {
    	for(int i = 0; i < data.length; i++) {
    		data[i] = 0;
    	}
    }
    
    public double nn(double[] data, int sensorId) {
    	return 0.9;
    }
    
    public Tuples think(int iterate, Tuples tuples) {
    	System.out.println("In the Neural Network");
    	double[] data = new double[2];		
    	int[] output = new int[2];
    	Tuples tnnOutput = new Tuples();
    	String[] labels = new String[2];
    	
    	for(int i = 0; i < numSensors; i++) {
    		receiveDataStream(i, 0, data);
    		double nnOutput = nn(data, i);	
    		String name = "Sensor " + i;
    		labels[0] = "distance";
    		labels[1] = "probability";
    		output[0] = (int) data[0];
    		output[1] = (int) (nnOutput * 100);
    		tnnOutput.add(name, labels, output);
    	}
    	System.out.println(tnnOutput);
    	return tnnOutput;				
    }
    
}
