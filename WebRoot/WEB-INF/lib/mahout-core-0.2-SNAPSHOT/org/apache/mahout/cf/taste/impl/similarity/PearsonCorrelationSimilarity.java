// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PearsonCorrelationSimilarity.java

package org.apache.mahout.cf.taste.impl.similarity;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.common.Weighting;
import org.apache.mahout.cf.taste.model.DataModel;

// Referenced classes of package org.apache.mahout.cf.taste.impl.similarity:
//			AbstractSimilarity

public final class PearsonCorrelationSimilarity extends AbstractSimilarity
{

	public PearsonCorrelationSimilarity(DataModel dataModel)
		throws TasteException
	{
		super(dataModel);
	}

	public PearsonCorrelationSimilarity(DataModel dataModel, Weighting weighting)
		throws TasteException
	{
		super(dataModel, weighting);
	}

	double computeResult(int n, double sumXY, double sumX2, double sumY2, 
			double sumXYdiff2)
	{
		if (n == 0)
			return (0.0D / 0.0D);
		double xTerm = Math.sqrt(sumX2);
		double yTerm = Math.sqrt(sumY2);
		double denominator = xTerm * yTerm;
		if (denominator == 0.0D)
			return (0.0D / 0.0D);
		else
			return sumXY / denominator;
	}

	public volatile double userSimilarity(long x0, long x1)
		throws TasteException
	{
		return super.userSimilarity(x0, x1);
	}
}
