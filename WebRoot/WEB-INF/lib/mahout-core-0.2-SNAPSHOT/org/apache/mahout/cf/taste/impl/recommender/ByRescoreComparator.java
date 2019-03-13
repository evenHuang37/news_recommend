// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ByRescoreComparator.java

package org.apache.mahout.cf.taste.impl.recommender;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Rescorer;

final class ByRescoreComparator
	implements Comparator, Serializable
{

	private final Rescorer rescorer;

	ByRescoreComparator(Rescorer rescorer)
	{
		this.rescorer = rescorer;
	}

	public int compare(RecommendedItem o1, RecommendedItem o2)
	{
		double rescored1;
		double rescored2;
		if (rescorer == null)
		{
			rescored1 = o1.getValue();
			rescored2 = o2.getValue();
		} else
		{
			rescored1 = rescorer.rescore(Long.valueOf(o1.getItemID()), o1.getValue());
			rescored2 = rescorer.rescore(Long.valueOf(o2.getItemID()), o2.getValue());
		}
		if (rescored1 < rescored2)
			return 1;
		return rescored1 <= rescored2 ? 0 : -1;
	}

	public String toString()
	{
		return (new StringBuilder()).append("ByRescoreComparator[rescorer:").append(rescorer).append(']').toString();
	}

	public volatile int compare(Object x0, Object x1)
	{
		return compare((RecommendedItem)x0, (RecommendedItem)x1);
	}
}
