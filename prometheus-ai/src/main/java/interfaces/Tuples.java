package interfaces;

import java.util.ArrayList;
import java.util.Iterator;

public class Tuples implements Iterable {

	private ArrayList<Tuple> tupleList;


	public Tuples(ArrayList<Tuple> input) {
		this.tupleList = input;
	}

	public void add(Tuple t) {
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
			return thisTuple;

		}

		// Used to remove an element. Implemented only if needed
		public void remove() {
			// Default throws UnsupportedOperationException.
		}
	}

}