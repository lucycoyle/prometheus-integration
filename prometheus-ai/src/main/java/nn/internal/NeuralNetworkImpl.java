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
  
    	
    	int[] output2 = new int[1];
    	String[] labels2 = new String[1];
    	Tuples tnnOutput = new Tuples();
    	labels2[0]="probability";
    	output2[0]=56;
    	//toCharge?
    	//proximity
    	String[] proximitysensors= {"frontSensor","rightSensor","leftSensor","backSensor"};
    	for(int i = 0; i < 4; i++) {
    		int[] output = new int[1];
        	
        	String[] labels = new String[1];
    		double nnOutput = nn(data, i);	
    		String name = proximitysensors[i];
    		labels[0] = "probability";
    		output[0] = 50+i;
    	
    		tnnOutput.add(name, labels, output);
    	
    	}
    
    	tnnOutput.add("charge",labels2,output2);
 
    	
    	return tnnOutput;				
    }
    
}
