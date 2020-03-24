package interfaces;

import java.util.Iterator;

import interfaces.Tuples.TuplesIterator;

public class TestTuples {

	public static void main(String args[]) {
		Tuples tuples = new Tuples();
		String name1 = "Name 1";
		String name2 = "Name 2";
		
		String[] labels1 = new String[2];
		labels1[0] = "1";
		labels1[1] = "1";
		String[] labels2 = new String[2];
		labels2[0] = "2";
		labels2[1] = "2";
		
		int[] data1 = new int[2];
		data1[0] = 1;
		data1[1] = 1;
		
		int[] data2 = new int[2];
		data2[0] = 2;
		data2[1] = 2;
		
		
		tuples.add(name1, labels1, data1);
		tuples.add(name2, labels2, data2);

		Iterator<Tuple> tuplesI = tuples.iterator();
		while(tuplesI.hasNext()) {
			Tuple t = tuplesI.next();
			System.out.println(t.label);
			System.out.println(t.iParams[0] + "  " +t.iParams[1] + " "+ t.sParams[0] + " " + t.sParams[1]);
		}
	}
}
