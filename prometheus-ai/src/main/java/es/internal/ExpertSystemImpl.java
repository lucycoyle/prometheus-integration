package es.internal;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;
import com.google.inject.assistedinject.Assisted;
import es.api.ExpertSystem;
import interfaces.LayerInput;
import interfaces.LayerOutput;
import interfaces.Thinking;
import interfaces.Tuple;
import tags.Fact;
import tags.Recommendation;
import tags.Rule;
import tags.Tag;

/**
 * Implementation of the ES.
 */
class ExpertSystemImpl implements ExpertSystem, LayerInput, LayerOutput, Thinking {
    private final Thinker thinker;
    private final Teacher teacher;
    private final Rester rester;
    private final Set<Rule> readyRules;
    private final Set<Rule> activeRules;
    private final Set<Fact> facts;
    private final Set<Recommendation> recommendations;

    @Inject
    ExpertSystemImpl(
            @Assisted("readyRules") final Set<Rule> readyRules,
            @Assisted("activeRules") final Set<Rule> activeRules,
            @Assisted("facts") final Set<Fact> facts,
            @Assisted("recommendations")
            final Set<Recommendation> recommendations,
            final ThinkerFactory thinkerFactory,
            final TeacherFactory teacherFactory,
            final ResterFactory resterFactory) {
        this.readyRules = readyRules;
        this.activeRules = activeRules;
        this.facts = facts;
        this.recommendations = recommendations;
        this.thinker = thinkerFactory
                .create(readyRules, activeRules, facts, recommendations);
        this.teacher = teacherFactory.create(readyRules);
        this.rester = resterFactory.create(readyRules);
    }

    @Override
    public Set<Recommendation> think() {
        return thinker.think(false, Integer.MAX_VALUE);
    }

    @Override
    public Set<Recommendation> think(final boolean generateRule) {
        return thinker.think(generateRule, Integer.MAX_VALUE);
    }

    @Override
    public Set<Recommendation> think(final boolean generateRule,
                                     final int numberOfCycles) {
        return thinker.think(generateRule, numberOfCycles);
    }

    @Override
    public void teach(final String sentence) {
        teacher.teach(sentence);
    }

    @Override
    public void rest(final int numberOfCycles) {
        rester.rest(numberOfCycles);
    }

    @Override
    public void reset() {
        activeRules.clear();
        readyRules.clear();
        facts.clear();
        recommendations.clear();
    }

    @Override
    public void deactivateRules() {
        readyRules.addAll(activeRules);
        activeRules.clear();
    }

    @Override
    public void addTags(final Set<Tag> tags) {
        for (final Tag t : tags) {
            addTag(t);
        }
    }

    @Override
    public boolean addTag(final Tag tag) {
        if (tag instanceof Rule) {
            return addReadyRule((Rule) tag);
        } else if (tag instanceof Fact) {
            return addFact((Fact) tag);
        } else if (tag instanceof Recommendation) {
            return addRecommendation((Recommendation) tag);
        }
        return false;
    }

    @Override
    public boolean addReadyRule(final Rule rule) {
        return readyRules.add(rule);
    }

    @Override
    public boolean removeReadyRule(final Rule rule) {
        return readyRules.remove(rule);
    }

    @Override
    public boolean addFact(final Fact fact) {
        return facts.add(fact);
    }

    @Override
    public boolean removeFact(final Fact fact) {
        return facts.remove(fact);
    }

    @Override
    public boolean addRecommendation(final Recommendation rec) {
        return recommendations.add(rec);
    }

    @Override
    public Set<Rule> getReadyRules() {
        return Collections.unmodifiableSet(readyRules);
    }

    @Override
    public Set<Rule> getActiveRules() {
        return Collections.unmodifiableSet(activeRules);
    }

    @Override
    public Set<Fact> getFacts() {
        return Collections.unmodifiableSet(facts);
    }

    @Override
    public Set<Recommendation> getRecommendations() {
        return Collections.unmodifiableSet(recommendations);
    }
    public void receiveDataStream(Tuple x) {
    	Fact f = new Fact(x.getSParams()[0]);
    	addTag(f);
    }

    public void sendDataStream(Tuple x) {
    	
    }
    public Tuple[] think(int iterate, Tuple tuples[]) {
    	for(Tuple t: tuples) {
    		receiveDataStream(t);
    	}
    	Set<Recommendation> recommendations = think();
    	for(Recommendation r: recommendations) {
    		addRecommendation(r);
    	}
    	recommendations = getRecommendations();
    	Tuple[] recTuples = new Tuple[recommendations.size()];
    	int tupleIndex = 0;
    	
    	for(Recommendation r : recommendations) {
    		Tuple t = new Tuple();
    		String [] sParams = new String[1];
    		int[] iParams = new int[1];
    		sParams[0] = "confidence";
    		iParams[0] = (int)(r.getConfidence()*100);
    		t.setTuple(r.toString(), sParams, iParams);
    		recTuples[tupleIndex] = t;
    		tupleIndex++;
    	}
    	return recTuples;
    }
}
