// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GenericUserBasedRecommender.java

package org.apache.mahout.cf.taste.impl.recommender;

import java.util.*;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.*;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Rescorer;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package org.apache.mahout.cf.taste.impl.recommender:
//			AbstractRecommender, TopItems

public class GenericUserBasedRecommender extends AbstractRecommender
	implements UserBasedRecommender
{
	private final class Estimator
		implements TopItems.Estimator
	{

		private final long theUserID;
		private final long theNeighborhood[];
		final GenericUserBasedRecommender this$0;

		public double estimate(Long itemID)
			throws TasteException
		{
			return (double)doEstimatePreference(theUserID, theNeighborhood, itemID.longValue());
		}

		public volatile double estimate(Object x0)
			throws TasteException
		{
			return estimate((Long)x0);
		}

		Estimator(long theUserID, long theNeighborhood[])
		{
			this$0 = GenericUserBasedRecommender.this;
			super();
			this.theUserID = theUserID;
			this.theNeighborhood = theNeighborhood;
		}
	}

	private static class MostSimilarEstimator
		implements TopItems.Estimator
	{

		private final long toUserID;
		private final UserSimilarity similarity;
		private final Rescorer rescorer;

		public double estimate(Long userID)
			throws TasteException
		{
			if (userID.longValue() == toUserID)
				return (0.0D / 0.0D);
			if (rescorer == null)
				return similarity.userSimilarity(toUserID, userID.longValue());
			LongPair pair = new LongPair(toUserID, userID.longValue());
			if (rescorer.isFiltered(pair))
			{
				return (0.0D / 0.0D);
			} else
			{
				double originalEstimate = similarity.userSimilarity(toUserID, userID.longValue());
				return rescorer.rescore(pair, originalEstimate);
			}
		}

		public volatile double estimate(Object x0)
			throws TasteException
		{
			return estimate((Long)x0);
		}

		private MostSimilarEstimator(long toUserID, UserSimilarity similarity, Rescorer rescorer)
		{
			this.toUserID = toUserID;
			this.similarity = similarity;
			this.rescorer = rescorer;
		}

		MostSimilarEstimator(long x0, UserSimilarity x1, Rescorer x2, 1 x3)
		{
			this(x0, x1, x2);
		}
	}


	private static final Logger log = LoggerFactory.getLogger(org/apache/mahout/cf/taste/impl/recommender/GenericUserBasedRecommender);
	private final UserNeighborhood neighborhood;
	private final UserSimilarity similarity;
	private final RefreshHelper refreshHelper;

	public GenericUserBasedRecommender(DataModel dataModel, UserNeighborhood neighborhood, UserSimilarity similarity)
	{
		super(dataModel);
		if (neighborhood == null)
		{
			throw new IllegalArgumentException("neighborhood is null");
		} else
		{
			this.neighborhood = neighborhood;
			this.similarity = similarity;
			refreshHelper = new RefreshHelper(null);
			refreshHelper.addDependency(dataModel);
			refreshHelper.addDependency(similarity);
			refreshHelper.addDependency(neighborhood);
			return;
		}
	}

	public UserSimilarity getSimilarity()
	{
		return similarity;
	}

	public List recommend(long userID, int howMany, Rescorer rescorer)
		throws TasteException
	{
		if (howMany < 1)
			throw new IllegalArgumentException("howMany must be at least 1");
		log.debug("Recommending items for user ID '{}'", Long.valueOf(userID));
		long theNeighborhood[] = neighborhood.getUserNeighborhood(userID);
		if (theNeighborhood.length == 0)
		{
			return Collections.emptyList();
		} else
		{
			FastIDSet allItemIDs = getAllOtherItems(theNeighborhood, userID);
			TopItems.Estimator estimator = new Estimator(userID, theNeighborhood);
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
		{
			return actualPref.floatValue();
		} else
		{
			long theNeighborhood[] = neighborhood.getUserNeighborhood(userID);
			return doEstimatePreference(userID, theNeighborhood, itemID);
		}
	}

	public long[] mostSimilarUserIDs(long userID, int howMany)
		throws TasteException
	{
		return mostSimilarUserIDs(userID, howMany, null);
	}

	public long[] mostSimilarUserIDs(long userID, int howMany, Rescorer rescorer)
		throws TasteException
	{
		TopItems.Estimator estimator = new MostSimilarEstimator(userID, similarity, rescorer);
		return doMostSimilarUsers(howMany, estimator);
	}

	private long[] doMostSimilarUsers(int howMany, TopItems.Estimator estimator)
		throws TasteException
	{
		DataModel model = getDataModel();
		return TopItems.getTopUsers(howMany, model.getUserIDs(), null, estimator);
	}

	protected float doEstimatePreference(long theUserID, long theNeighborhood[], long itemID)
		throws TasteException
	{
		if (theNeighborhood.length == 0)
			return (0.0F / 0.0F);
		DataModel dataModel = getDataModel();
		double preference = 0.0D;
		double totalSimilarity = 0.0D;
		long arr$[] = theNeighborhood;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			long userID = arr$[i$];
			if (userID == theUserID)
				continue;
			Float pref = dataModel.getPreferenceValue(userID, itemID);
			if (pref != null)
			{
				double theSimilarity = similarity.userSimilarity(theUserID, userID) + 1.0D;
				preference += theSimilarity * (double)pref.floatValue();
				totalSimilarity += theSimilarity;
			}
		}

		return totalSimilarity != 0.0D ? (float)(preference / totalSimilarity) : (0.0F / 0.0F);
	}

	protected FastIDSet getAllOtherItems(long theNeighborhood[], long theUserID)
		throws TasteException
	{
		DataModel dataModel = getDataModel();
		FastIDSet allItemIDs = new FastIDSet();
		long arr$[] = theNeighborhood;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			long userID = arr$[i$];
			PreferenceArray prefs = dataModel.getPreferencesFromUser(userID);
			int size = prefs.length();
			for (int i = 0; i < size; i++)
			{
				long itemID = prefs.getItemID(i);
				if (dataModel.getPreferenceValue(theUserID, itemID) == null)
					allItemIDs.add(itemID);
			}

		}

		return allItemIDs;
	}

	public void refresh(Collection alreadyRefreshed)
	{
		refreshHelper.refresh(alreadyRefreshed);
	}

	public String toString()
	{
		return (new StringBuilder()).append("GenericUserBasedRecommender[neighborhood:").append(neighborhood).append(']').toString();
	}

}
