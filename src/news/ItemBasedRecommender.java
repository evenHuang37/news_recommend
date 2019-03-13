package news;

import java.util.Collection;
import java.util.List;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.Rescorer;

public class ItemBasedRecommender implements Recommender {


	// EmbededItemBasedRecommender类的定义
	private static final class EmbededItemBasedRecommender implements
			Recommender {

		// 包含一个GenericItemBasedRecommender实例；
		private final GenericItemBasedRecommender recommender;

		private EmbededItemBasedRecommender(
				GenericItemBasedRecommender recommender) {
			this.recommender = recommender;
		}

		public List<RecommendedItem> recommend(long userID, int howMany,
				Rescorer<Long> rescorer) throws TasteException {
			FastIDSet itemIDs = recommender.getDataModel().getItemIDsFromUser(
					userID);
			return recommender.mostSimilarItems(itemIDs.toArray(), howMany,
					null);
		}

		@Override
		public float estimatePreference(long arg0, long arg1)
				throws TasteException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public DataModel getDataModel() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<RecommendedItem> recommend(long arg0, int arg1)
				throws TasteException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void removePreference(long arg0, long arg1)
				throws TasteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setPreference(long arg0, long arg1, float arg2)
				throws TasteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void refresh(Collection<Refreshable> arg0) {
			// TODO Auto-generated method stub
			
		}

	}

	@Override
	public float estimatePreference(long arg0, long arg1) throws TasteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DataModel getDataModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RecommendedItem> recommend(long arg0, int arg1,
			Rescorer<Long> arg2) throws TasteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removePreference(long arg0, long arg1) throws TasteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPreference(long arg0, long arg1, float arg2)
			throws TasteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh(Collection<Refreshable> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<RecommendedItem> recommend(long arg0, int arg1)
			throws TasteException {
		// TODO Auto-generated method stub
		return null;
	}
}