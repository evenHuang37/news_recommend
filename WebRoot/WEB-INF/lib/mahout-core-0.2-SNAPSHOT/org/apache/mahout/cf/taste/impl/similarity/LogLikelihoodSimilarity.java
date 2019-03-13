// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LogLikelihoodSimilarity.java

package org.apache.mahout.cf.taste.impl.similarity;

import java.util.Collection;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.RefreshHelper;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.*;

public final class LogLikelihoodSimilarity
	implements UserSimilarity, ItemSimilarity
{

	private final DataModel dataModel;

	public LogLikelihoodSimilarity(DataModel dataModel)
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
		FastIDSet prefs1 = dataModel.getItemIDsFromUser(userID1);
		FastIDSet prefs2 = dataModel.getItemIDsFromUser(userID2);
		int prefs1Size = prefs1.size();
		int prefs2Size = prefs2.size();
		int intersectionSize = prefs1Size >= prefs2Size ? prefs1.intersectionSize(prefs2) : prefs2.intersectionSize(prefs1);
		if (intersectionSize == 0)
		{
			return (0.0D / 0.0D);
		} else
		{
			int numItems = dataModel.getNumItems();
			double logLikelihood = twoLogLambda(intersectionSize, prefs1Size - intersectionSize, prefs2Size, numItems - prefs2Size);
			return 1.0D - 1.0D / (1.0D + logLikelihood);
		}
	}

	public double itemSimilarity(long itemID1, long itemID2)
		throws TasteException
	{
		int preferring1and2 = dataModel.getNumUsersWithPreferenceFor(new long[] {
			itemID1, itemID2
		});
		if (preferring1and2 == 0)
		{
			return 0.0D;
		} else
		{
			int preferring1 = dataModel.getNumUsersWithPreferenceFor(new long[] {
				itemID1
			});
			int preferring2 = dataModel.getNumUsersWithPreferenceFor(new long[] {
				itemID2
			});
			int numUsers = dataModel.getNumUsers();
			double logLikelihood = twoLogLambda(preferring1and2, preferring1 - preferring1and2, preferring2, numUsers - preferring2);
			return 1.0D - 1.0D / (1.0D + logLikelihood);
		}
	}

	static double twoLogLambda(double k1, double k2, double n1, double n2)
	{
		double p = (k1 + k2) / (n1 + n2);
		return 2D * ((logL(k1 / n1, k1, n1) + logL(k2 / n2, k2, n2)) - logL(p, k1, n1) - logL(p, k2, n2));
	}

	private static double logL(double p, double k, double n)
	{
		return k * safeLog(p) + (n - k) * safeLog(1.0D - p);
	}

	private static double safeLog(double d)
	{
		return d > 0.0D ? Math.log(d) : 0.0D;
	}

	public void refresh(Collection alreadyRefreshed)
	{
		alreadyRefreshed = RefreshHelper.buildRefreshed(alreadyRefreshed);
		RefreshHelper.maybeRefresh(alreadyRefreshed, dataModel);
	}

	public String toString()
	{
		return (new StringBuilder()).append("LogLikelihoodSimilarity[dataModel:").append(dataModel).append(']').toString();
	}
}
