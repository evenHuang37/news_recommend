// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SpearmanCorrelationSimilarity.java

package org.apache.mahout.cf.taste.impl.similarity;

import java.util.Collection;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.RefreshHelper;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.similarity.PreferenceInferrer;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public final class SpearmanCorrelationSimilarity
	implements UserSimilarity
{

	private final DataModel dataModel;

	public SpearmanCorrelationSimilarity(DataModel dataModel)
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

	public double userSimilarity(long userID1, long userID2)
		throws TasteException
	{
		PreferenceArray xPrefs = dataModel.getPreferencesFromUser(userID1);
		PreferenceArray yPrefs = dataModel.getPreferencesFromUser(userID2);
		int xLength = xPrefs.length();
		int yLength = yPrefs.length();
		if (xLength <= 1 || yLength <= 1)
			return (0.0D / 0.0D);
		xPrefs = xPrefs.clone();
		yPrefs = yPrefs.clone();
		xPrefs.sortByValue();
		yPrefs.sortByValue();
		for (int i = 0; i < xLength; i++)
			xPrefs.setValue(i, i);

		for (int i = 0; i < yLength; i++)
			yPrefs.setValue(i, i);

		xPrefs.sortByItem();
		yPrefs.sortByItem();
		long xIndex = xPrefs.getItemID(0);
		long yIndex = yPrefs.getItemID(0);
		int xPrefIndex = 0;
		int yPrefIndex = 0;
		double sumXYRankDiff2 = 0.0D;
		int count = 0;
		do
		{
			int compare = xIndex >= yIndex ? ((int) (xIndex <= yIndex ? 0 : 1)) : -1;
			if (compare == 0)
			{
				double diff = xPrefs.getValue(xPrefIndex) - yPrefs.getValue(yPrefIndex);
				sumXYRankDiff2 += diff * diff;
				count++;
			}
			if (compare <= 0)
			{
				if (++xPrefIndex >= xLength)
					break;
				xIndex = xPrefs.getItemID(xPrefIndex);
			}
			if (compare < 0)
				continue;
			if (++yPrefIndex >= yLength)
				break;
			yIndex = yPrefs.getItemID(yPrefIndex);
		} while (true);
		return 1.0D - (6D * sumXYRankDiff2) / (double)count / (double)(count * count - 1);
	}

	public void setPreferenceInferrer(PreferenceInferrer inferrer)
	{
		throw new UnsupportedOperationException();
	}

	public void refresh(Collection alreadyRefreshed)
	{
		alreadyRefreshed = RefreshHelper.buildRefreshed(alreadyRefreshed);
		RefreshHelper.maybeRefresh(alreadyRefreshed, dataModel);
	}
}
