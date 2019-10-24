package nn.internal;

import javax.inject.Inject;

import interfaces.LayerOutput;
import interfaces.SensorInput;
import interfaces.Tuple;
import nn.api.NeuralNetwork;

/**
 * Implementation of the NN.
 */
class NeuralNetworkImpl implements NeuralNetwork, SensorInput, LayerOutput {
    @Inject
    NeuralNetworkImpl() {
    }
    public void sendDataStream(Tuple x) {
    	
    }
    public void receiveDataStream(int nnID, int nnStruct, int data[]) {
    	
    }
}
