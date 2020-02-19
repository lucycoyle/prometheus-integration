package interfaces;

import java.util.ArrayList;
import java.util.Iterator;

public class Tuples implements Iterable {

	private ArrayList<Tuple> tupleList;


	public Tuples(ArrayList<Tuple> input) {
		this.tupleList = input;
	}
	public Tuples() {
		this.tupleList = new ArrayList<Tuple>();
	}

	public void add(String name, String[] labels, int[] data){
		Tuple t = new Tuple();
		t.setTuple(name, labels, data);
		tupleList.add(t);
	}

	@Override
	public Iterator<Tuple> iterator() {

		return new TuplesIterator(this);
	}

	class TuplesIterator implements Iterator<Tuple> {

		private int ind;

		// constructor
		public TuplesIterator(Tuples tuples) {
			// initialize cursor (return start pos)
			this.ind = 0;

		}

		// Checks if the next element exists
		@Override
		public boolean hasNext() {
			return (ind < tupleList.size());
		}

		// moves the cursor/iterator to next element
		@Override
		public Tuple next() {

			Tuple thisTuple = tupleList.get(ind);
			ind++;
			return thisTuple;

		}

		// Used to remove an element. Implemented only if needed
		public void remove() {
			// Default throws UnsupportedOperationException.
		}
	}

}