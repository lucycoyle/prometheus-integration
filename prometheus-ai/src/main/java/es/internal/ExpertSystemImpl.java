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
    	System.out.println("In the Expert System");
    	 Iterator<Tuple> iter= tuples.iterator();
    	   while(iter.hasNext()) {
    		Tuple t= iter.next();
    		if(t.getLabel()=="Fact") {
    			System.out.println("fact before replacing:");
    			System.out.println(t.getSParams()[0]);
    			
    			
    			String factString= t.getSParams()[0].replaceAll("\\[", "(");
    			factString= factString.replaceAll("\\]", ")");
    			factString= factString.replaceAll(" ", "");
    			System.out.println("fact string after replacing");
        		System.out.println(factString);
    			
    		Fact f = new Fact( factString);
    		
    		System.out.println(t.getSParams()[0]);
    		addFact(f);}
    		else if (t.getLabel()=="Rule") {
    			System.out.println("rule before replacing:");
    			System.out.println(t.getSParams()[0]);
    			String[] tokens= t.getSParams()[0].split("->");
    			String middle1= tokens[0].substring(2,tokens[0].length()-2).replaceAll("\\[", "(");
    			middle1= middle1.replaceAll("\\]", ")");
    			System.out.println(tokens[0]);
    			System.out.println(tokens[1]);
    			String newfirsttok= tokens[0].substring(1,2)+middle1+tokens[0].substring(tokens[0].length()-1,tokens[0].length());
    			
    			
    			String middle2= tokens[1].substring(2,tokens[1].length()-1).replaceAll("\\[", "(");
    			middle2= middle2.replaceAll("\\]", ")");
    			
    			String newsectok= tokens[1].substring(0,1)+middle2+tokens[1].substring(tokens[1].length()-1,tokens[1].length()-1);
    			newfirsttok= newfirsttok.replaceAll(" ", "");
    			newfirsttok= newfirsttok.replaceAll("\\),", ") ");
    			newsectok=newsectok.replaceAll(" ", "");
    			String ruleString= newfirsttok+" -> "+newsectok;
    			
    		
    		System.out.println("rule string after replacing");
    		System.out.println(ruleString);
        		Rule r = new Rule( ruleString);
        		
        		addReadyRule(r);}
        	
    	}
    	   
    	/************************************************/
    	   Fact[] testFacts = {
                   new Fact("Aardvark(brown,strange,speed=slow", 0.9),
                   new Fact("Bat(black,speed=10)", 0.6)
           };
           Recommendation recX = new Recommendation("Run(north,quickly,speed!=slow)", 0.2);
           Recommendation recY = new Recommendation("Hide(safelocation,manner=stealth)", 0.8);
           Recommendation recZ = new Recommendation("Fight(aggressive)", 0.8);
           Recommendation[] testRecommendations = {recX, recY};
           Predicate[] outputPredicates1 = {new Recommendation("Dog(friendly,breed=pug,age<2)", 0.3)};
           Fact[] outputPredicates2 = {new Fact("Elephant(&x,size=big,intelligent)", 0.1)};
           Fact[] outputPredicates3 = {new Fact("Frog(colour=green,slimy,sound=ribbit)", 0.8)};
           Fact[] outputPredicates4 = {new Fact("Hog(&x,size=huge,&y,big)", 0.5)};
          // Rule unactivatedRule = new Rule(new Fact[]{new Fact("Goose(loud,nationality=canadian,wingspan=4)", 0.5), new Fact("Aardvark(brown,?,speed=slow)", 0.8)}, testFacts);
           Rule rule1 = new Rule(new Fact[]{new Fact("Aardvark(brown,strange,?)", 0.2), new Fact("Bat(black,speed>9,*)", 0.7)}, outputPredicates1);
          // Rule rule2 = new Rule(new Fact[]{new Fact("Dog(&x,breed=pug,age=1)", 0.7), new Fact("Bat(*)", 1.0)}, outputPredicates2);
        //   Rule rule3 = new Rule(new Fact[]{new Fact("Dog(friendly,breed=pug,age=1)", 0.9), new Fact("Elephant(friendly,size=big,intelligent)", 0.8)}, outputPredicates3);
       //    Rule rule4 = new Rule(new Fact[]{new Fact("Frog(&x,slimy,&y)", 0.6), new Fact("Elephant(*)", 0.7)}, outputPredicates4);
        //   Rule rule5 = new Rule(new Fact[]{new Fact("Hog(*)", 0.2), new Fact("Frog(?,slimy,sound=ribbit)", 0.9)}, new Predicate[]{recZ}) ;
           Rule[] testRules = {
                   rule1,
               //    rule2,
               //    rule3,
                //   rule5,
                  // unactivatedRule,
                  // rule4
           };

           for (Fact fact : testFacts) {
               addFact(fact);
           }
           for (Rule rule : testRules) {
               addReadyRule(rule);
           }
          
/************************************************************/
    	   
    	   
    	   
    	   
    	   
    	   
    	   
    	   
    	   Set<Fact> initialFacts = getFacts();
    	   System.out.println("initial facts;");
    	   System.out.println(initialFacts);
    	   
    	   Set<Rule> rr = getReadyRules();
    	   System.out.println("ready rules:");
    	   System.out.println(rr);
    	   
    	Set<Recommendation> recommendations = think();
   /* 	for(Recommendation r: recommendations) {
    		addRecommendation(r);
    	} */
    //	recommendations = getRecommendations();
    	System.out.println("recommendations:");
    	System.out.println(recommendations);
    	Tuples recTuples = new Tuples();
    	
    	for(Recommendation r : recommendations) {
    		String [] sParams = new String[1];
    		int[] iParams = new int[1];
    		sParams[0] = "confidence";
    		iParams[0] = (int)(r.getConfidence()*100);
    		recTuples.add(r.toString(), sParams, iParams);
    	}
    	return recTuples;
    }
}
