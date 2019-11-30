package nn.internal;

import javax.inject.Inject;

import interfaces.LayerOutput;
import interfaces.SensorInput;
import interfaces.Thinking;
import interfaces.Tuple;
import nn.api.NeuralNetwork;

/**
 * Implementation of the NN.
 */
class NeuralNetworkImpl implements NeuralNetwork, SensorInput, LayerOutput, Thinking {
	int numSensors = 5;
	
    @Inject
    NeuralNetworkImpl() {
    }
    public void sendDataStream(Tuple x) {
    	int[] data = x.getIParams();
    	for(int i = 0; i < data.length; i++) {
    		if(data[i] < -100) {
    			data[i] = -100;
    		}
    		if(data[i] > 100) {
    			data[i] = 100;
    		}
    	}
    	x.setTuple(x.getLabel(), x.getSParams(), data);
    	
    }
    public void receiveDataStream(int nnID, int nnStruct, double data[]) {
    	for(int i = 0; i < data.length; i++) {
    		data[i] = 0;
    	}
    }
    
    public Tuple modifyOutputFormat(String[] labels, int[] data, String name) {
    	Tuple t = new Tuple();
    	t.setTuple(name, labels, data);
    	return t;
    }
    
    public double nn(double[] data, int sensorId) {
    	return 0.9;
    }
    
    public Tuple[] think(int iterate, Tuple tuples[]) {
    	double[] data = new double[2];		
    	int[] output = new int[2];
    	Tuple[] tnnOutput = new Tuple[numSensors];
    	String[] labels = new String[2];
    	
    	for(int i = 0; i < numSensors; i++) {
    		receiveDataStream(i, 0, data);
    		double nnOutput = nn(data, i);	
    		String name = "Sensor " + i;
    		labels[0] = "distance";
    		labels[1] = "probability";
    		output[0] = (int) data[0];
    		output[1] = (int) (nnOutput * 100);
    		tnnOutput[i] = modifyOutputFormat(labels, output, name);
    	}
    	
    	for(Tuple t: tnnOutput) {
    		sendDataStream(t);						
    	}
    	return tnnOutput;				
    }
    
}
