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
 
    	
    	return tuples;	
    	//return tnnOutput;
    }
    
}
