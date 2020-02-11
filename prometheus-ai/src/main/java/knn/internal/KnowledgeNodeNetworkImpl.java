package knn.internal;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import com.google.inject.assistedinject.Assisted;
import interfaces.Tuple;
import interfaces.LayerInput;
import interfaces.LayerOutput;

import interfaces.Tuples;
import interfaces.Thinking;
import knn.api.KnowledgeNode;
import knn.api.KnowledgeNodeNetwork;
import knn.api.KnowledgeNodeParseException;
import tags.Fact;
import tags.Tag;

/**
 * Implementation of the KNN.
 */
class KnowledgeNodeNetworkImpl implements KnowledgeNodeNetwork, Thinking {

    private final Map<Tag, KnowledgeNode> mapKN;
    private final Set<Tag> activeTags;
    private final TreeSet<KnowledgeNode> ageSortedKNs;

    private final DirectSearcher directSearcher;
    private final ForwardSearcher forwardSearcher;
    private final BackwardSearcher backwardSearcher;
    private final LambdaSearcher lambdaSearcher;

    @Inject
    KnowledgeNodeNetworkImpl(
            @Assisted("mapKN") final Map<Tag, KnowledgeNode> mapKN,
            @Assisted("activeTags") final Set<Tag> activeTags,
            @Assisted("ageSortedKNs") final TreeSet<KnowledgeNode> ageSortedKNs,
            @Assisted("backwardSearchMatchRatio") final
            double backwardSearchMatchRatio,
            @Assisted("backwardSearchAgeLimit")
            final long backwardSearchAgeLimit,
            final DirectSearcherFactory directSearcherFactory,
            final ForwardSearcherFactory forwardSearcherFactory,
            final BackwardSearcherFactory backwardSearcherFactory,
            final LambdaSearcherFactory lambdaSearcherFactory) {
        this.mapKN = mapKN;
        this.activeTags = activeTags;
        this.ageSortedKNs = ageSortedKNs;
        this.directSearcher =
                directSearcherFactory.create(mapKN, activeTags, ageSortedKNs);
        this.forwardSearcher = forwardSearcherFactory.create(directSearcher);
        this.backwardSearcher = backwardSearcherFactory.create(
                activeTags, ageSortedKNs, backwardSearchMatchRatio,
                backwardSearchAgeLimit);
        this.lambdaSearcher =
                lambdaSearcherFactory.create(forwardSearcher, backwardSearcher);
    }

    @Override
    public void resetEmpty() {
        mapKN.clear();
        activeTags.clear();
        ageSortedKNs.clear();
    }

    @Override
    public void clearActiveTags() {
        activeTags.clear();
    }

    @Override
    public void addKnowledgeNode(final KnowledgeNode kn) {
        mapKN.put(kn.getInputTag(), kn);
        ageSortedKNs.add(kn);
    }

    @Override
    public void deleteExpiredKnowledgeNodes() {
        final Set<Tag> tagsToDelete = new HashSet<>();
        for (final KnowledgeNode kn : mapKN.values()) {
            if (kn.isExpired()) {
                tagsToDelete.add(kn.getInputTag());
                ageSortedKNs.remove(kn);
            }
        }
        for (final Tag t : tagsToDelete) {
            mapKN.remove(t);
            activeTags.remove(t);
        }
    }

    @Override
    public void deleteKnowledgeNode(final Tag tag) {
        mapKN.remove(tag);
    }

    @Override
    public void addActiveTag(final Tag tag) {
        activeTags.add(tag);
    }

    @Override
    public void addActiveTags(final Tag... tags) {
        activeTags.addAll(Arrays.asList(tags));
    }

    @Override
    public Set<Tag> getActiveTags() {
        return Collections.unmodifiableSet(activeTags);
    }

    @Override
    public KnowledgeNode getKnowledgeNode(final Tag tag) {
        return mapKN.get(tag);
    }

    @Override
    public Set<KnowledgeNode> getKnowledgeNodes() {
        return Collections.unmodifiableSet(ageSortedKNs);
    }

    @Override
    public Set<Tag> directSearch(final Tag inputTag) {
        return directSearcher.search(inputTag);
    }

    @Override
    public Set<Tag> forwardSearch(final Set<Tag> inputTags, final int ply) {
        return forwardSearcher.search(inputTags, ply);
    }

    @Override
    public Set<Tag> forwardThink(final int ply) {
        return forwardSearcher.search(activeTags, ply);
    }

    @Override
    public Set<Tag> backwardSearch(final Set<Tag> inputTags, final int ply) {
        return backwardSearcher.search(inputTags, ply);
    }

    @Override
    public Set<Tag> backwardThink(final int ply) {
        return backwardSearcher.search(activeTags, ply);
    }

    @Override
    public void setBackwardSearchMatchRatio(final double ratio) {
        backwardSearcher.setPartialMatchRatio(ratio);
    }

    @Override
    public Set<Tag> lambdaSearch(final Set<Tag> inputTags, final int ply) {
        return lambdaSearcher.search(inputTags, ply);
    }

    @Override
    public Set<Tag> lambdaThink(final int ply) {
        return lambdaSearcher.search(activeTags, ply);
    }

    @Override
    public List<KnowledgeNode> loadData(final String filename) {
        final List<KnowledgeNode> knowledgeNodes = new ArrayList<>();
        resetEmpty();
        try {
            final BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filename),
                            "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                final String[] info = line.split(";\\s+");
                final KnowledgeNode kn = new KnowledgeNode(info);
                knowledgeNodes.add(kn);
            }
            br.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        for (final KnowledgeNode knowledgeNode : knowledgeNodes) {
            addKnowledgeNode(knowledgeNode);
        }
        return knowledgeNodes;
    }

    @Override
    public void reset(final String dbFilename) {
    }

    @Override
    public void save(final String dbFilename) {
    }

    public void processInputTuple(Tuple x) {
    	String [] info = new String [1];
    	String thisTag= "Object(";
    	for (int i=0;i<x.getIParams().length;i++) {
    		thisTag+= x.getSParams()[i]+"="+x.getIParams()[i]+",";
    		
    	}
    	thisTag+=")";
    	info[0]=thisTag;
    	KnowledgeNode kn= null;
		try {
			kn = new KnowledgeNode(info);
		} catch (KnowledgeNodeParseException e) {
			
			e.printStackTrace();
		}
    	addKnowledgeNode(kn);
    	
    }
public Tuples think(int iterate, Tuples tuples) {
    Iterator<Tuple> iter= tuples.iterator();
    while(iter.hasNext()) {
    	Tuple t= iter.next();
    	processInputTuple(t);
    }
    
    	Set<Tag> knOutputTags = lambdaThink(0);		//0 for ply		
    	Tuples knOutput= new Tuples();
    	int i=0;
    	for(Tag t: knOutputTags) {
    		Tuple newTuple= new Tuple();
    		String[] sparams= {t.toString()};
    		int[] iparams= {}; //paired?
    		knOutput.add("Fact",sparams,iparams);
    		i++;
    	}
    	return knOutput;					
    }
    
}
