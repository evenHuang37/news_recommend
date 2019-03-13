// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GenericBooleanPrefUserBasedRecommender.java

package org.apache.mahout.cf.taste.impl.recommender;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

// Referenced classes of package org.apache.mahout.cf.taste.impl.recommender:
//			GenericUserBasedRecommender

public final class GenericBooleanPrefUserBasedRecommender extends GenericUserBasedRecommender
{

	public GenericBooleanPrefUserBasedRecommender(DataModel dataModel, UserNeighborhood neighborhood, UserSimilarity similarity)
	{
		super(dataModel, neighborhood, similarity);
	}

	protected float doEstimatePreference(long theUserID, long theNeighborhood[], long itemID)
		throws TasteException
	{
		if (theNeighborhood.length == 0)
			return (0.0F / 0.0F);
		DataModel dataModel = getDataModel();
		UserSimilarity similarity = getSimilarity();
		float totalSimilarity = 0.0F;
		boolean foundAPref = false;
		long arr$[] = theNeighborhood;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			long userID = arr$[i$];
			if (userID != theUserID && dataModel.getPreferenceValue(userID, itemID) != null)
			{
				foundAPref = true;
				totalSimilarity = (float)((double)totalSimilarity + similarity.userSimilarity(theUserID, userID));
			}
		}

		return foundAPref ? totalSimilarity : (0.0F / 0.0F);
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
			allItemIDs.addAll(dataModel.getItemIDsFromUser(userID));
		}

		allItemIDs.removeAll(dataModel.getItemIDsFromUser(theUserID));
		return allItemIDs;
	}

	public String toString()
	{
		return "GenericBooleanPrefUserBasedRecommender";
	}
}
