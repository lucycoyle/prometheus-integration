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
    public void receiveDataStream(int nnID, int nnStruct, int data[]) {
    	for(int i = 0; i < data.length; i++) {
    		data[i] = 0;
    	}
    }
    
    public Tuple modifyOutputFormat(int[] data, String name) {
    	Tuple t = new Tuple();
    	String s[] = new String[1];
    	t.setTuple(name, s, data);
    	return t;
    }
    
    public int[] nn(int[] input) {
    	return input;
    }
    public Tuple[] think(int iterate, Tuple tuples[]) {
    	int[] data = new int[100];		
    	Tuple[] tnnOutput = new Tuple[numSensors];
    	
    	for(int i = 0; i < numSensors; i++) {
    		receiveDataStream(i, 0, data);
    		int[] nnOutput = nn(data);	
    		String name = "Sensor " + i;
    		tnnOutput[i] = modifyOutputFormat(nnOutput, name);
    	}
    	
    	for(Tuple t: tnnOutput) {
    		sendDataStream(t);						
    	}
    	return tnnOutput;				
    }
    
}
