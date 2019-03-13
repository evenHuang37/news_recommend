// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CachingRecommender.java

package org.apache.mahout.cf.taste.impl.recommender;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.*;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.Rescorer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CachingRecommender
	implements Recommender
{
	private static final class Recommendations
	{

		private final List items;
		private boolean noMoreRecommendableItems;

		List getItems()
		{
			return items;
		}

		boolean isNoMoreRecommendableItems()
		{
			return noMoreRecommendableItems;
		}

		void setNoMoreRecommendableItems(boolean noMoreRecommendableItems)
		{
			this.noMoreRecommendableItems = noMoreRecommendableItems;
		}

		private Recommendations(List items)
		{
			this.items = items;
		}

	}

	private static final class EstimatedPrefRetriever
		implements Retriever
	{

		private final Recommender recommender;

		public Float get(LongPair key)
			throws TasteException
		{
			long userID = key.getFirst();
			long itemID = key.getSecond();
			CachingRecommender.log.debug("Retrieving estimated preference for user ID '{}' and item ID '{}'", Long.valueOf(userID), Long.valueOf(itemID));
			return Float.valueOf(recommender.estimatePreference(userID, itemID));
		}

		public volatile Object get(Object x0)
			throws TasteException
		{
			return get((LongPair)x0);
		}

		private EstimatedPrefRetriever(Recommender recommender)
		{
			this.recommender = recommender;
		}

	}

	private final class RecommendationRetriever
		implements Retriever
	{

		private final Recommender recommender;
		final CachingRecommender this$0;

		public Recommendations get(Long key)
			throws TasteException
		{
			CachingRecommender.log.debug("Retrieving new recommendations for user ID '{}'", key);
			int howMany = maxHowMany.get();
			Rescorer rescorer = getCurrentRescorer();
			List recommendations = rescorer != null ? recommender.recommend(key.longValue(), howMany, rescorer) : recommender.recommend(key.longValue(), howMany);
			return new Recommendations(Collections.unmodifiableList(recommendations));
		}

		public volatile Object get(Object x0)
			throws TasteException
		{
			return get((Long)x0);
		}

		private RecommendationRetriever(Recommender recommender)
		{
			this$0 = CachingRecommender.this;
			super();
			this.recommender = recommender;
		}

	}


	private static final Logger log = LoggerFactory.getLogger(org/apache/mahout/cf/taste/impl/recommender/CachingRecommender);
	private final Recommender recommender;
	private final AtomicInteger maxHowMany;
	private final Cache recommendationCache;
	private final Cache estimatedPrefCache;
	private final RefreshHelper refreshHelper;
	private Rescorer currentRescorer;

	public CachingRecommender(Recommender recommender)
		throws TasteException
	{
		if (recommender == null)
		{
			throw new IllegalArgumentException("recommender is null");
		} else
		{
			this.recommender = recommender;
			maxHowMany = new AtomicInteger(1);
			int numUsers = recommender.getDataModel().getNumUsers();
			recommendationCache = new Cache(new RecommendationRetriever(this.recommender), numUsers);
			estimatedPrefCache = new Cache(new EstimatedPrefRetriever(this.recommender), numUsers);
			refreshHelper = new RefreshHelper(new Callable() {

				final CachingRecommender this$0;

				public Object call()
				{
					clear();
					return null;
				}

			
			{
				this$0 = CachingRecommender.this;
				super();
			}
			});
			refreshHelper.addDependency(recommender);
			return;
		}
	}

	private synchronized Rescorer getCurrentRescorer()
	{
		return currentRescorer;
	}

	private synchronized void setCurrentRescorer(Rescorer rescorer)
	{
		if (rescorer == null)
		{
			if (currentRescorer != null)
			{
				currentRescorer = null;
				clear();
			}
		} else
		if (!rescorer.equals(currentRescorer))
		{
			currentRescorer = rescorer;
			clear();
		}
	}

	public List recommend(long userID, int howMany)
		throws TasteException
	{
		return recommend(userID, howMany, null);
	}

	public List recommend(long userID, int howMany, Rescorer rescorer)
		throws TasteException
	{
		if (howMany < 1)
			throw new IllegalArgumentException("howMany must be at least 1");
		synchronized (maxHowMany)
		{
			if (howMany > maxHowMany.get())
				maxHowMany.set(howMany);
		}
		setCurrentRescorer(rescorer);
		Recommendations recommendations = (Recommendations)recommendationCache.get(Long.valueOf(userID));
		if (recommendations.getItems().size() < howMany && !recommendations.isNoMoreRecommendableItems())
		{
			clear(userID);
			recommendations = (Recommendations)recommendationCache.get(Long.valueOf(userID));
			if (recommendations.getItems().size() < howMany)
				recommendations.setNoMoreRecommendableItems(true);
		}
		List recommendedItems = recommendations.getItems();
		return recommendedItems.size() <= howMany ? recommendedItems : recommendedItems.subList(0, howMany);
	}

	public float estimatePreference(long userID, long itemID)
		throws TasteException
	{
		return ((Float)estimatedPrefCache.get(new LongPair(userID, itemID))).floatValue();
	}

	public void setPreference(long userID, long itemID, float value)
		throws TasteException
	{
		recommender.setPreference(userID, itemID, value);
		clear(userID);
	}

	public void removePreference(long userID, long itemID)
		throws TasteException
	{
		recommender.removePreference(userID, itemID);
		clear(userID);
	}

	public DataModel getDataModel()
	{
		return recommender.getDataModel();
	}

	public void refresh(Collection alreadyRefreshed)
	{
		refreshHelper.refresh(alreadyRefreshed);
	}

	public void clear(long userID)
	{
		log.debug("Clearing recommendations for user ID '{}'", Long.valueOf(userID));
		recommendationCache.remove(Long.valueOf(userID));
	}

	public void clear()
	{
		log.debug("Clearing all recommendations...");
		recommendationCache.clear();
	}

	public String toString()
	{
		return (new StringBuilder()).append("CachingRecommender[recommender:").append(recommender).append(']').toString();
	}




}
