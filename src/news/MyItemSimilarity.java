package news;

import java.util.Collection;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class MyItemSimilarity implements ItemSimilarity {
	public double itemSimilarity(long itemID1, long itemID2) { 
	   // MyMovie movie1 = lookupMyMovie(itemID1); 
	   // MyMovie movie2 = lookupMyMovie(itemID2); 
	    double similarity = 0.0; 
	   /* if (movie1.getGenre().equals(movie2.getGenre()) { 
	      similarity += 0.1; 
	    } 
	    if (movie1.getDirector().equals(movie2.getDirector())) { 
	      similarity += 0.5; 
	    } */
	    return similarity; 
	  }

	@Override
	public void refresh(Collection<Refreshable> arg0) {
		// TODO Auto-generated method stub

	}
}
