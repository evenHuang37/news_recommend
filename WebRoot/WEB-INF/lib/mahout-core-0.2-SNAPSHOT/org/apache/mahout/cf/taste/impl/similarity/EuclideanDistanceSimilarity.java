// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   EuclideanDistanceSimilarity.java

package org.apache.mahout.cf.taste.impl.similarity;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.common.Weighting;
import org.apache.mahout.cf.taste.model.DataModel;

// Referenced classes of package org.apache.mahout.cf.taste.impl.similarity:
//			AbstractSimilarity

public final class EuclideanDistanceSimilarity extends AbstractSimilarity
{

	public EuclideanDistanceSimilarity(DataModel dataModel)
		throws TasteException
	{
		super(dataModel);
	}

	public EuclideanDistanceSimilarity(DataModel dataModel, Weighting weighting)
		throws TasteException
	{
		super(dataModel, weighting);
	}

	double computeResult(int n, double sumXY, double sumX2, double sumY2, 
			double sumXYdiff2)
	{
		if (n == 0)
			return (0.0D / 0.0D);
		double denominator = Math.sqrt(sumX2) + Math.sqrt(sumY2);
		if (denominator == 0.0D)
		{
			return (0.0D / 0.0D);
		} else
		{
			sumXYdiff2 /= denominator;
			return 1.0D / (1.0D + Math.sqrt(sumXYdiff2) / (double)n);
		}
	}

	public volatile double userSimilarity(long x0, long x1)
		throws TasteException
	{
		return super.userSimilarity(x0, x1);
	}
}
