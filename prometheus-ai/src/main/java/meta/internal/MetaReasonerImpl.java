package meta.internal;

import javax.inject.Inject;

import interfaces.Tuples;
import meta.api.MetaReasoner;

/**
 * Implementation of the META.
 */
class MetaReasonerImpl implements MetaReasoner {
    @Inject
    MetaReasonerImpl() {
    }
    
    public Tuples think(int iterate, Tuples tuples) {
    	System.out.println("In the Meta Reasoner");
    	return tuples;	
    }
}
