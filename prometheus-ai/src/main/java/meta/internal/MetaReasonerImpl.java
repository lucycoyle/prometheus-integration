package meta.internal;

import javax.inject.Inject;

import interfaces.LayerInput;
import interfaces.MotorOutput;
import interfaces.Tuple;
import meta.api.MetaReasoner;

/**
 * Implementation of the META.
 */
class MetaReasonerImpl implements MetaReasoner, LayerInput, MotorOutput {
    @Inject
    MetaReasonerImpl() {
    }

    public void receiveDataStream(Tuple x) {
	
	}
    public void sendCommand(String motor, String command) {
    	
    }

}
