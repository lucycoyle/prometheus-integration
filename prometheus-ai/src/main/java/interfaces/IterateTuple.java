package interfaces;

public interface IterateTuple {
	Tuple firstTuple(); // Returns NULL when list is empty
	Tuple nextTuple(); // Returns NULL when at end of list
	boolean isEmptyTuple(); // False when nextTuple() is NULL
}
