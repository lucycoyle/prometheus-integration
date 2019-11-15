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
    @Inject
    NeuralNetworkImpl() {
    }
    public void sendDataStream(Tuple x) {
    	
    }
    public void receiveDataStream(int nnID, int nnStruct, int data[]) {
    	
    }
    
    public Tuple[] modifyOutputFormat(int[] data) {
    	return new Tuple[10];
    }
    
    public int[] nn(int[] input) {
    	return input;
    }
    public Tuple[] think(int iterate, Tuple tuples[]) {
    	int[] data = new int[1];					//TODO: find proper size for data array
    	receiveDataStream(0, 0, data);				//TODO: find proper nnID and nnStruct values
    	
    	int[] nnOutput = nn(data);					//TODO: find real method signature for NN
    	Tuple[] tnnOutput = modifyOutputFormat(nnOutput);
    	for(Tuple t: tnnOutput) {
    		sendDataStream(t);						
    	}
    	return tnnOutput;				
    }
    
}
