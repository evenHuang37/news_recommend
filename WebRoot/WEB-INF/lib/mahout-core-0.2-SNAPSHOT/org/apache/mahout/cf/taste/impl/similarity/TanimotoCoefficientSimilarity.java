// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TanimotoCoefficientSimilarity.java

package org.apache.mahout.cf.taste.impl.similarity;

import java.util.Collection;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.RefreshHelper;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.*;

public final class TanimotoCoefficientSimilarity
	implements UserSimilarity, ItemSimilarity
{

	private final DataModel dataModel;

	public TanimotoCoefficientSimilarity(DataModel dataModel)
	{
		this.dataModel = dataModel;
	}

	public void setPreferenceInferrer(PreferenceInferrer inferrer)
	{
		throw new UnsupportedOperationException();
	}

	public double userSimilarity(long userID1, long userID2)
		throws TasteException
	{
		FastIDSet xPrefs = dataModel.getItemIDsFromUser(userID1);
		FastIDSet yPrefs = dataModel.getItemIDsFromUser(userID2);
		if (xPrefs.isEmpty() && yPrefs.isEmpty())
			return (0.0D / 0.0D);
		if (xPrefs.isEmpty() || yPrefs.isEmpty())
			return 0.0D;
		int intersectionSize = xPrefs.intersectionSize(yPrefs);
		if (intersectionSize == 0)
		{
			return (0.0D / 0.0D);
		} else
		{
			int unionSize = (xPrefs.size() + yPrefs.size()) - intersectionSize;
			return (double)intersectionSize / (double)unionSize;
		}
	}

	public double itemSimilarity(long itemID1, long itemID2)
		throws TasteException
	{
		int preferring1and2 = dataModel.getNumUsersWithPreferenceFor(new long[] {
			itemID1, itemID2
		});
		int preferring1 = dataModel.getNumUsersWithPreferenceFor(new long[] {
			itemID1
		});
		int preferring2 = dataModel.getNumUsersWithPreferenceFor(new long[] {
			itemID2
		});
		return (double)preferring1and2 / (double)((preferring1 + preferring2) - preferring1and2);
	}

	public void refresh(Collection alreadyRefreshed)
	{
		alreadyRefreshed = RefreshHelper.buildRefreshed(alreadyRefreshed);
		RefreshHelper.maybeRefresh(alreadyRefreshed, dataModel);
	}

	public String toString()
	{
		return (new StringBuilder()).append("TanimotoCoefficientSimilarity[dataModel:").append(dataModel).append(']').toString();
	}
}
