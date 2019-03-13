// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GenericItemBasedRecommender.java

package org.apache.mahout.cf.taste.impl.recommender;

import java.util.*;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.*;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.Rescorer;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package org.apache.mahout.cf.taste.impl.recommender:
//			AbstractRecommender, TopItems

public class GenericItemBasedRecommender extends AbstractRecommender
	implements ItemBasedRecommender
{
	private class RecommendedBecauseEstimator
		implements TopItems.Estimator
	{

		private final long userID;
		private final long recommendedItemID;
		private final ItemSimilarity similarity;
		final GenericItemBasedRecommender this$0;

		public double estimate(Long itemID)
			throws TasteException
		{
			Float pref = getDataModel().getPreferenceValue(userID, itemID.longValue());
			if (pref == null)
			{
				return (0.0D / 0.0D);
			} else
			{
				double similarityValue = similarity.itemSimilarity(recommendedItemID, itemID.longValue());
				return (1.0D + similarityValue) * (double)pref.floatValue();
			}
		}

		public volatile double estimate(Object x0)
			throws TasteException
		{
			return estimate((Long)x0);
		}

		private RecommendedBecauseEstimator(long userID, long recommendedItemID, ItemSimilarity similarity)
		{
			this$0 = GenericItemBasedRecommender.this;
			super();
			this.userID = userID;
			this.recommendedItemID = recommendedItemID;
			this.similarity = similarity;
		}

		RecommendedBecauseEstimator(long x1, long x2, ItemSimilarity x3, 1 x4)
		{
			this(x1, x2, x3);
		}
	}

	private static class MultiMostSimilarEstimator
		implements TopItems.Estimator
	{

		private final long toItemIDs[];
		private final ItemSimilarity similarity;
		private final Rescorer rescorer;

		public double estimate(Long itemID)
			throws TasteException
		{
			RunningAverage average = new FullRunningAverage();
			long arr$[] = toItemIDs;
			int len$ = arr$.length;
			for (int i$ = 0; i$ < len$; i$++)
			{
				long toItemID = arr$[i$];
				LongPair pair = new LongPair(toItemID, itemID.longValue());
				if (rescorer != null && rescorer.isFiltered(pair))
					continue;
				double estimate = similarity.itemSimilarity(toItemID, itemID.longValue());
				if (rescorer != null)
					estimate = rescorer.rescore(pair, estimate);
				average.addDatum(estimate);
			}

			return average.getAverage();
		}

		public volatile double estimate(Object x0)
			throws TasteException
		{
			return estimate((Long)x0);
		}

		private MultiMostSimilarEstimator(long toItemIDs[], ItemSimilarity similarity, Rescorer rescorer)
		{
			this.toItemIDs = toItemIDs;
			this.similarity = similarity;
			this.rescorer = rescorer;
		}

	}

	private final class Estimator
		implements TopItems.Estimator
	{

		private final long userID;
		final GenericItemBasedRecommender this$0;

		public double estimate(Long itemID)
			throws TasteException
		{
			return (double)doEstimatePreference(userID, itemID.longValue());
		}

		public volatile double estimate(Object x0)
			throws TasteException
		{
			return estimate((Long)x0);
		}

		private Estimator(long userID)
		{
			this$0 = GenericItemBasedRecommender.this;
			super();
			this.userID = userID;
		}

		Estimator(long x1, 1 x2)
		{
			this(x1);
		}
	}

	public static class MostSimilarEstimator
		implements TopItems.Estimator
	{

		private final long toItemID;
		private final ItemSimilarity similarity;
		private final Rescorer rescorer;

		public double estimate(Long itemID)
			throws TasteException
		{
			LongPair pair = new LongPair(toItemID, itemID.longValue());
			if (rescorer != null && rescorer.isFiltered(pair))
			{
				return (0.0D / 0.0D);
			} else
			{
				double originalEstimate = similarity.itemSimilarity(toItemID, itemID.longValue());
				return rescorer != null ? rescorer.rescore(pair, originalEstimate) : originalEstimate;
			}
		}

		public volatile double estimate(Object x0)
			throws TasteException
		{
			return estimate((Long)x0);
		}

		public MostSimilarEstimator(long toItemID, ItemSimilarity similarity, Rescorer rescorer)
		{
			this.toItemID = toItemID;
			this.similarity = similarity;
			this.rescorer = rescorer;
		}
	}


	private static final Logger log = LoggerFactory.getLogger(org/apache/mahout/cf/taste/impl/recommender/GenericItemBasedRecommender);
	private final ItemSimilarity similarity;
	private final RefreshHelper refreshHelper;

	public GenericItemBasedRecommender(DataModel dataModel, ItemSimilarity similarity)
	{
		super(dataModel);
		if (similarity == null)
		{
			throw new IllegalArgumentException("similarity is null");
		} else
		{
			this.similarity = similarity;
			refreshHelper = new RefreshHelper(null);
			refreshHelper.addDependency(dataModel);
			refreshHelper.addDependency(similarity);
			return;
		}
	}

	public ItemSimilarity getSimilarity()
	{
		return similarity;
	}

	public List recommend(long userID, int howMany, Rescorer rescorer)
		throws TasteException
	{
		if (howMany < 1)
			throw new IllegalArgumentException("howMany must be at least 1");
		log.debug("Recommending items for user ID '{}'", Long.valueOf(userID));
		if (getNumPreferences(userID) == 0)
		{
			return Collections.emptyList();
		} else
		{
			FastIDSet allItemIDs = getAllOtherItems(userID);
			TopItems.Estimator estimator = new Estimator(userID);
			List topItems = TopItems.getTopItems(howMany, allItemIDs.iterator(), rescorer, estimator);
			log.debug("Recommendations are: {}", topItems);
			return topItems;
		}
	}

	public float estimatePreference(long userID, long itemID)
		throws TasteException
	{
		DataModel model = getDataModel();
		Float actualPref = model.getPreferenceValue(userID, itemID);
		if (actualPref != null)
			return actualPref.floatValue();
		else
			return doEstimatePreference(userID, itemID);
	}

	public List mostSimilarItems(long itemID, int howMany)
		throws TasteException
	{
		return mostSimilarItems(itemID, howMany, null);
	}

	public List mostSimilarItems(long itemID, int howMany, Rescorer rescorer)
		throws TasteException
	{
		TopItems.Estimator estimator = new MostSimilarEstimator(itemID, similarity, rescorer);
		return doMostSimilarItems(itemID, howMany, estimator);
	}

	public List mostSimilarItems(long itemIDs[], int howMany)
		throws TasteException
	{
		return mostSimilarItems(itemIDs, howMany, null);
	}

	public List mostSimilarItems(long itemIDs[], int howMany, Rescorer rescorer)
		throws TasteException
	{
		DataModel model = getDataModel();
		TopItems.Estimator estimator = new MultiMostSimilarEstimator(itemIDs, similarity, rescorer);
		FastIDSet allItemIDs = new FastIDSet(model.getNumItems());
		for (LongPrimitiveIterator it = model.getItemIDs(); it.hasNext(); allItemIDs.add(it.nextLong()));
		allItemIDs.removeAll(itemIDs);
		return TopItems.getTopItems(howMany, allItemIDs.iterator(), null, estimator);
	}

	public List recommendedBecause(long userID, long itemID, int howMany)
		throws TasteException
	{
		if (howMany < 1)
			throw new IllegalArgumentException("howMany must be at least 1");
		DataModel model = getDataModel();
		TopItems.Estimator estimator = new RecommendedBecauseEstimator(userID, itemID, similarity);
		PreferenceArray prefs = model.getPreferencesFromUser(userID);
		int size = prefs.length();
		FastIDSet allUserItems = new FastIDSet(size);
		for (int i = 0; i < size; i++)
			allUserItems.add(prefs.getItemID(i));

		allUserItems.remove(itemID);
		return TopItems.getTopItems(howMany, allUserItems.iterator(), null, estimator);
	}

	private List doMostSimilarItems(long itemID, int howMany, TopItems.Estimator estimator)
		throws TasteException
	{
		DataModel model = getDataModel();
		FastIDSet allItemIDs = new FastIDSet(model.getNumItems());
		for (LongPrimitiveIterator it = model.getItemIDs(); it.hasNext(); allItemIDs.add(it.nextLong()));
		allItemIDs.remove(itemID);
		return TopItems.getTopItems(howMany, allItemIDs.iterator(), null, estimator);
	}

	protected float doEstimatePreference(long userID, long itemID)
		throws TasteException
	{
		double preference = 0.0D;
		double totalSimilarity = 0.0D;
		PreferenceArray prefs = getDataModel().getPreferencesFromUser(userID);
		int size = prefs.length();
		for (int i = 0; i < size; i++)
		{
			double theSimilarity = similarity.itemSimilarity(itemID, prefs.getItemID(i));
			if (!Double.isNaN(theSimilarity))
			{
				theSimilarity++;
				preference += theSimilarity * (double)prefs.getValue(i);
				totalSimilarity += theSimilarity;
			}
		}

		return totalSimilarity != 0.0D ? (float)(preference / totalSimilarity) : (0.0F / 0.0F);
	}

	private int getNumPreferences(long userID)
		throws TasteException
	{
		return getDataModel().getPreferencesFromUser(userID).length();
	}

	public void refresh(Collection alreadyRefreshed)
	{
		refreshHelper.refresh(alreadyRefreshed);
	}

	public String toString()
	{
		return (new StringBuilder()).append("GenericItemBasedRecommender[similarity:").append(similarity).append(']').toString();
	}

}
