// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractRecommender.java

package org.apache.mahout.cf.taste.impl.recommender;

import java.util.List;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRecommender
	implements Recommender
{

	private static final Logger log = LoggerFactory.getLogger(org/apache/mahout/cf/taste/impl/recommender/AbstractRecommender);
	private final DataModel dataModel;

	protected AbstractRecommender(DataModel dataModel)
	{
		if (dataModel == null)
		{
			throw new IllegalArgumentException("dataModel is null");
		} else
		{
			this.dataModel = dataModel;
			return;
		}
	}

	public List recommend(long userID, int howMany)
		throws TasteException
	{
		return recommend(userID, howMany, null);
	}

	public void setPreference(long userID, long itemID, float value)
		throws TasteException
	{
		if (Double.isNaN(value))
		{
			throw new IllegalArgumentException((new StringBuilder()).append("Invalid value: ").append(value).toString());
		} else
		{
			log.debug("Setting preference for user {}, item {}", Long.valueOf(userID), Long.valueOf(itemID));
			dataModel.setPreference(userID, itemID, value);
			return;
		}
	}

	public void removePreference(long userID, long itemID)
		throws TasteException
	{
		log.debug("Remove preference for user '{}', item '{}'", Long.valueOf(userID), Long.valueOf(itemID));
		dataModel.removePreference(userID, itemID);
	}

	public DataModel getDataModel()
	{
		return dataModel;
	}

	protected FastIDSet getAllOtherItems(long theUserID)
		throws TasteException
	{
		FastIDSet allItemIDs = new FastIDSet(dataModel.getNumItems());
		for (LongPrimitiveIterator it = dataModel.getItemIDs(); it.hasNext(); allItemIDs.add(it.nextLong()));
		PreferenceArray prefs = dataModel.getPreferencesFromUser(theUserID);
		int size = prefs.length();
		for (int i = 0; i < size; i++)
			allItemIDs.remove(prefs.getItemID(i));

		return allItemIDs;
	}

}
