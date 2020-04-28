package es.internal;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import com.google.inject.assistedinject.Assisted;
import es.api.ExpertSystem;
import interfaces.Tuple;
import interfaces.Tuples;
import tags.Fact;
import tags.Predicate;
import tags.Recommendation;
import tags.Rule;
import tags.Tag;

/**
 * Implementation of the ES.
 */
class ExpertSystemImpl implements ExpertSystem{
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

    public Tuples think(int iterate, Tuples tuples) {
    	if(text_simulator.Main.ioLayer==true) {
    	System.out.println("In the Expert System");
    	System.out.println("input tuples to ES:");
    	System.out.println(tuples.toSString());}
    	 Iterator<Tuple> iter= tuples.iterator();
    	   while(iter.hasNext()) {
    		Tuple t= iter.next();
    		if(t.getLabel()=="Fact") {
   
    			
    			String factString= t.getSParams()[0].replaceAll("\\[", "(");
    			factString= factString.replaceAll("\\]", ")");
    			factString= factString.replaceAll(" ", "");
   
    			
    		Fact f = new Fact( factString);
  
    		addFact(f);}
    		else if (t.getLabel()=="Rule") {
    
    			String[] tokens= t.getSParams()[0].split("->");
    			String middle1= tokens[0].substring(2,tokens[0].length()-2).replaceAll("\\[", "(");
    			middle1= middle1.replaceAll("\\]", ")");
 
    			String newfirsttok= tokens[0].substring(1,2)+middle1+tokens[0].substring(tokens[0].length()-1,tokens[0].length());
    			
    			
    			String middle2= tokens[1].substring(2,tokens[1].length()-1).replaceAll("\\[", "(");
    			middle2= middle2.replaceAll("\\]", ")");
    			
    			String newsectok= tokens[1].substring(0,1)+middle2+tokens[1].substring(tokens[1].length()-1,tokens[1].length()-1);
    			newfirsttok= newfirsttok.replaceAll(" ", "");
    			newfirsttok= newfirsttok.replaceAll("\\),", ") ");
    			newsectok=newsectok.replaceAll(" ", "");
    			String ruleString= newfirsttok+" -> "+newsectok;

        		Rule r = new Rule( ruleString);
        		
        		addReadyRule(r);}
        	
    	}
    	
    	 
    	   
    	Set<Recommendation> recommendations = think(); //diff than prometheus think
    	for(Recommendation r: recommendations) {
    		addRecommendation(r);
    	} 
    	if(text_simulator.Main.ioLayer==true) {
    	System.out.println("recommendations:");
    	System.out.println(recommendations);}
    	Tuples recTuples = new Tuples();
    	
    	for(Recommendation r : recommendations) {
    		String [] sParams = new String[1];
    		int[] iParams = new int[1];
    		sParams[0] = "confidence";
    		iParams[0] = (int)(r.getConfidence()*100);
    		recTuples.add(r.toString(), sParams, iParams);
    	}
    	if(text_simulator.Main.ioLayer==true) {
        
        	System.out.println("output tuples of ES:");
        	System.out.println(recTuples.toSString());}
    	return recTuples;
    }
}
