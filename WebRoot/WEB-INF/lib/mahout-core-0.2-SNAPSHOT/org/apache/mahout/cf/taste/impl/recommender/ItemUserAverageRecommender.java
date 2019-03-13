// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ItemUserAverageRecommender.java

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

public final class ItemUserAverageRecommender extends AbstractRecommender
{
	private final class Estimator
		implements TopItems.Estimator
	{

		private final long userID;
		final ItemUserAverageRecommender this$0;

		public double estimate(Long itemID)
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
			this$0 = ItemUserAverageRecommender.this;
			super();
			this.userID = userID;
		}

		Estimator(long x1, 1 x2)
		{
			this(x1);
		}
	}


	private static final Logger log = LoggerFactory.getLogger(org/apache/mahout/cf/taste/impl/recommender/ItemUserAverageRecommender);
	private final FastByIDMap itemAverages = new FastByIDMap();
	private final FastByIDMap userAverages = new FastByIDMap();
	private final RunningAverage overallAveragePrefValue = new FullRunningAverage();
	private boolean averagesBuilt;
	private final ReadWriteLock buildAveragesLock = new ReentrantReadWriteLock();
	private final RefreshHelper refreshHelper = new RefreshHelper(new Callable() {

		final ItemUserAverageRecommender this$0;

		public Object call()
			throws TasteException
		{
			buildAverageDiffs();
			return null;
		}

			
			{
				this$0 = ItemUserAverageRecommender.this;
				super();
			}
	});

	public ItemUserAverageRecommender(DataModel dataModel)
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
			TopItems.Estimator estimator = new Estimator(userID);
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
			return doEstimatePreference(userID, itemID);
		}
	}

	private float doEstimatePreference(long userID, long itemID)
	{
		buildAveragesLock.readLock().lock();
		RunningAverage itemAverage;
		float f;
		itemAverage = (RunningAverage)itemAverages.get(itemID);
		if (itemAverage != null)
			break MISSING_BLOCK_LABEL_53;
		f = (0.0F / 0.0F);
		buildAveragesLock.readLock().unlock();
		return f;
		RunningAverage userAverage;
		float f1;
		userAverage = (RunningAverage)userAverages.get(userID);
		if (userAverage != null)
			break MISSING_BLOCK_LABEL_92;
		f1 = (0.0F / 0.0F);
		buildAveragesLock.readLock().unlock();
		return f1;
		float f2;
		double userDiff = userAverage.getAverage() - overallAveragePrefValue.getAverage();
		f2 = (float)(itemAverage.getAverage() + userDiff);
		buildAveragesLock.readLock().unlock();
		return f2;
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
			long userID = it.nextLong();
			PreferenceArray prefs = dataModel.getPreferencesFromUser(userID);
			int size = prefs.length();
			int i = 0;
			while (i < size) 
			{
				long itemID = prefs.getItemID(i);
				float value = prefs.getValue(i);
				addDatumAndCreateIfNeeded(itemID, value, itemAverages);
				addDatumAndCreateIfNeeded(userID, value, userAverages);
				overallAveragePrefValue.addDatum(value);
				i++;
			}
		}

		averagesBuilt = true;
		buildAveragesLock.writeLock().unlock();
		break MISSING_BLOCK_LABEL_175;
		Exception exception;
		exception;
		buildAveragesLock.writeLock().unlock();
		throw exception;
	}

	private static void addDatumAndCreateIfNeeded(long itemID, float value, FastByIDMap averages)
	{
		RunningAverage itemAverage = (RunningAverage)averages.get(itemID);
		if (itemAverage == null)
		{
			itemAverage = new FullRunningAverage();
			averages.put(itemID, itemAverage);
		}
		itemAverage.addDatum(value);
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
		RunningAverage itemAverage = (RunningAverage)itemAverages.get(itemID);
		if (itemAverage == null)
		{
			RunningAverage newItemAverage = new FullRunningAverage();
			newItemAverage.addDatum(prefDelta);
			itemAverages.put(itemID, newItemAverage);
		} else
		{
			itemAverage.changeDatum(prefDelta);
		}
		RunningAverage userAverage = (RunningAverage)userAverages.get(userID);
		if (userAverage == null)
		{
			RunningAverage newUserAveragae = new FullRunningAverage();
			newUserAveragae.addDatum(prefDelta);
			userAverages.put(userID, newUserAveragae);
		} else
		{
			userAverage.changeDatum(prefDelta);
		}
		overallAveragePrefValue.changeDatum(prefDelta);
		buildAveragesLock.writeLock().unlock();
		break MISSING_BLOCK_LABEL_236;
		Exception exception;
		exception;
		buildAveragesLock.writeLock().unlock();
		throw exception;
	}

	public void removePreference(long userID, long itemID)
		throws TasteException
	{
		double value;
		DataModel dataModel = getDataModel();
		Float oldPref = dataModel.getPreferenceValue(userID, itemID);
		super.removePreference(userID, itemID);
		if (oldPref == null)
			break MISSING_BLOCK_LABEL_205;
		value = oldPref.floatValue();
		buildAveragesLock.writeLock().lock();
		RunningAverage itemAverage = (RunningAverage)itemAverages.get(itemID);
		if (itemAverage == null)
			throw new IllegalStateException((new StringBuilder()).append("No preferences exist for item ID: ").append(itemID).toString());
		itemAverage.removeDatum(value);
		RunningAverage userAverage = (RunningAverage)userAverages.get(userID);
		if (userAverage == null)
			throw new IllegalStateException((new StringBuilder()).append("No preferences exist for user ID: ").append(userID).toString());
		userAverage.removeDatum(value);
		overallAveragePrefValue.removeDatum(value);
		buildAveragesLock.writeLock().unlock();
		break MISSING_BLOCK_LABEL_205;
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
		return "ItemUserAverageRecommender";
	}



}
