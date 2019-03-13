// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FarthestNeighborClusterSimilarity.java

package org.apache.mahout.cf.taste.impl.recommender;

import java.util.Collection;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.*;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

// Referenced classes of package org.apache.mahout.cf.taste.impl.recommender:
//			ClusterSimilarity

public final class FarthestNeighborClusterSimilarity
	implements ClusterSimilarity
{

	private final UserSimilarity similarity;
	private final double samplingRate;

	public FarthestNeighborClusterSimilarity(UserSimilarity similarity)
	{
		this(similarity, 1.0D);
	}

	public FarthestNeighborClusterSimilarity(UserSimilarity similarity, double samplingRate)
	{
		if (similarity == null)
			throw new IllegalArgumentException("similarity is null");
		if (Double.isNaN(samplingRate) || samplingRate <= 0.0D || samplingRate > 1.0D)
		{
			throw new IllegalArgumentException((new StringBuilder()).append("samplingRate is invalid: ").append(samplingRate).toString());
		} else
		{
			this.similarity = similarity;
			this.samplingRate = samplingRate;
			return;
		}
	}

	public double getSimilarity(FastIDSet cluster1, FastIDSet cluster2)
		throws TasteException
	{
		if (cluster1.isEmpty() || cluster2.isEmpty())
			return (0.0D / 0.0D);
		double leastSimilarity = (1.0D / 0.0D);
		for (LongPrimitiveIterator someUsers = SamplingLongPrimitiveIterator.maybeWrapIterator(cluster1.iterator(), samplingRate); someUsers.hasNext();)
		{
			long userID1 = ((Long)someUsers.next()).longValue();
			LongPrimitiveIterator it2 = cluster2.iterator();
			while (it2.hasNext()) 
			{
				double theSimilarity = similarity.userSimilarity(userID1, ((Long)it2.next()).longValue());
				if (theSimilarity < leastSimilarity)
					leastSimilarity = theSimilarity;
			}
		}

		if (leastSimilarity == (1.0D / 0.0D))
			return similarity.userSimilarity(((Long)cluster1.iterator().next()).longValue(), ((Long)cluster2.iterator().next()).longValue());
		else
			return leastSimilarity;
	}

	public void refresh(Collection alreadyRefreshed)
	{
		alreadyRefreshed = RefreshHelper.buildRefreshed(alreadyRefreshed);
		RefreshHelper.maybeRefresh(alreadyRefreshed, similarity);
	}

	public String toString()
	{
		return (new StringBuilder()).append("FarthestNeighborClusterSimilarity[similarity:").append(similarity).append(']').toString();
	}
}
