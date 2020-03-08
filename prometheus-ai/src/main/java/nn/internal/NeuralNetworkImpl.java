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
    	double[] data = new double[1];		
    	int[] output = new int[1];
    	Tuples tnnOutput = new Tuples();
    	String[] labels = new String[2];
    	//toCharge?
    	//proximity
    	String[] proximitysensors= {"front sensor","right sensor","left sensor","back sensor"};
    	for(int i = 0; i < 4; i++) {

    		double nnOutput = nn(data, i);	
    		String name = proximitysensors[i];
    		labels[0] = "probability";
    	
    		output[0] = 50;

    		tnnOutput.add(name, labels, output);
    	}
    
    	tnnOutput.add("Charge",labels,output);
    	System.out.println(tnnOutput);
    	return tnnOutput;				
    }
    
}
