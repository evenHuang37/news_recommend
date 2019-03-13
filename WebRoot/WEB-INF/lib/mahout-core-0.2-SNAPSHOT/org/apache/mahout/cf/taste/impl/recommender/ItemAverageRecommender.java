// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ItemAverageRecommender.java

package org.apache.mahout.cf.taste.impl.recommender;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.*;
import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.*;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.Rescorer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package org.apache.mahout.cf.taste.impl.recommender:
//			AbstractRecommender, TopItems

public final class ItemAverageRecommender extends AbstractRecommender
{
	private final class Estimator
		implements TopItems.Estimator
	{

		final ItemAverageRecommender this$0;

		public double estimate(Long itemID)
		{
			return (double)doEstimatePreference(itemID.longValue());
		}

		public volatile double estimate(Object x0)
			throws TasteException
		{
			return estimate((Long)x0);
		}

		private Estimator()
		{
			this$0 = ItemAverageRecommender.this;
			super();
		}

	}


	private static final Logger log = LoggerFactory.getLogger(org/apache/mahout/cf/taste/impl/recommender/ItemAverageRecommender);
	private final FastByIDMap itemAverages = new FastByIDMap();
	private boolean averagesBuilt;
	private final ReadWriteLock buildAveragesLock = new ReentrantReadWriteLock();
	private final RefreshHelper refreshHelper = new RefreshHelper(new Callable() {

		final ItemAverageRecommender this$0;

		public Object call()
			throws TasteException
		{
			buildAverageDiffs();
			return null;
		}

			
			{
				this$0 = ItemAverageRecommender.this;
				super();
			}
	});

	public ItemAverageRecommender(DataModel dataModel)
	{
		super(dataModel);
		refreshHelper.addDependency(dataModel);
	}

	public List recommend(long userID, int howMany, Rescorer rescorer)
		throws TasteException
	{
		if (howMany < 1)
		{
			throw new IllegalArgumentException("howMany must be at least 1");
		} else
		{
			log.debug("Recommending items for user ID '{}'", Long.valueOf(userID));
			checkAverageDiffsBuilt();
			FastIDSet allItemIDs = getAllOtherItems(userID);
			TopItems.Estimator estimator = new Estimator();
			List topItems = TopItems.getTopItems(howMany, allItemIDs.iterator(), rescorer, estimator);
			log.debug("Recommendations are: {}", topItems);
			return topItems;
		}
	}

	public float estimatePreference(long userID, long itemID)
		throws TasteException
	{
		DataModel dataModel = getDataModel();
		Float actualPref = dataModel.getPreferenceValue(userID, itemID);
		if (actualPref != null)
		{
			return actualPref.floatValue();
		} else
		{
			checkAverageDiffsBuilt();
			return doEstimatePreference(itemID);
		}
	}

	private float doEstimatePreference(long itemID)
	{
		buildAveragesLock.readLock().lock();
		float f;
		RunningAverage average = (RunningAverage)itemAverages.get(itemID);
		f = average != null ? (float)average.getAverage() : (0.0F / 0.0F);
		buildAveragesLock.readLock().unlock();
		return f;
		Exception exception;
		exception;
		buildAveragesLock.readLock().unlock();
		throw exception;
	}

	private void checkAverageDiffsBuilt()
		throws TasteException
	{
		if (!averagesBuilt)
			buildAverageDiffs();
	}

	private void buildAverageDiffs()
		throws TasteException
	{
		buildAveragesLock.writeLock().lock();
		DataModel dataModel = getDataModel();
		for (LongPrimitiveIterator it = dataModel.getUserIDs(); it.hasNext();)
		{
			PreferenceArray prefs = dataModel.getPreferencesFromUser(it.nextLong());
			int size = prefs.length();
			int i = 0;
			while (i < size) 
			{
				long itemID = prefs.getItemID(i);
				RunningAverage average = (RunningAverage)itemAverages.get(itemID);
				if (average == null)
				{
					average = new FullRunningAverage();
					itemAverages.put(itemID, average);
				}
				average.addDatum(prefs.getValue(i));
				i++;
			}
		}

		averagesBuilt = true;
		buildAveragesLock.writeLock().unlock();
		break MISSING_BLOCK_LABEL_182;
		Exception exception;
		exception;
		buildAveragesLock.writeLock().unlock();
		throw exception;
	}

	public void setPreference(long userID, long itemID, float value)
		throws TasteException
	{
		double prefDelta;
		DataModel dataModel = getDataModel();
		try
		{
			Float oldPref = dataModel.getPreferenceValue(userID, itemID);
			prefDelta = oldPref != null ? value - oldPref.floatValue() : value;
		}
		catch (NoSuchUserException nsee)
		{
			prefDelta = value;
		}
		super.setPreference(userID, itemID, value);
		buildAveragesLock.writeLock().lock();
		RunningAverage average = (RunningAverage)itemAverages.get(itemID);
		if (average == null)
		{
			RunningAverage newAverage = new FullRunningAverage();
			newAverage.addDatum(prefDelta);
			itemAverages.put(itemID, newAverage);
		} else
		{
			average.changeDatum(prefDelta);
		}
		buildAveragesLock.writeLock().unlock();
		break MISSING_BLOCK_LABEL_166;
		Exception exception;
		exception;
		buildAveragesLock.writeLock().unlock();
		throw exception;
	}

	public void removePreference(long userID, long itemID)
		throws TasteException
	{
		Float oldPref;
		DataModel dataModel = getDataModel();
		oldPref = dataModel.getPreferenceValue(userID, itemID);
		super.removePreference(userID, itemID);
		if (oldPref == null)
			break MISSING_BLOCK_LABEL_136;
		buildAveragesLock.writeLock().lock();
		RunningAverage average = (RunningAverage)itemAverages.get(itemID);
		if (average == null)
			throw new IllegalStateException((new StringBuilder()).append("No preferences exist for item ID: ").append(itemID).toString());
		average.removeDatum(oldPref.floatValue());
		buildAveragesLock.writeLock().unlock();
		break MISSING_BLOCK_LABEL_136;
		Exception exception;
		exception;
		buildAveragesLock.writeLock().unlock();
		throw exception;
	}

	public void refresh(Collection alreadyRefreshed)
	{
		refreshHelper.refresh(alreadyRefreshed);
	}

	public String toString()
	{
		return "ItemAverageRecommender";
	}



}
