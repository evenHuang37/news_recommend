// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TopItems.java

package org.apache.mahout.cf.taste.impl.recommender;

import java.util.*;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.GenericUserSimilarity;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Rescorer;

// Referenced classes of package org.apache.mahout.cf.taste.impl.recommender:
//			GenericRecommendedItem, SimilarUser

public final class TopItems
{
	public static interface Estimator
	{

		public abstract double estimate(Object obj)
			throws TasteException;
	}


	private static final long NO_IDS[] = new long[0];

	private TopItems()
	{
	}

	public static List getTopItems(int howMany, LongPrimitiveIterator allItemIDs, Rescorer rescorer, Estimator estimator)
		throws TasteException
	{
		if (allItemIDs == null || estimator == null)
			throw new IllegalArgumentException("argument is null");
		Queue topItems = new PriorityQueue(howMany + 1, Collections.reverseOrder());
		boolean full = false;
		double lowestTopValue = (-1.0D / 0.0D);
		do
		{
			if (!allItemIDs.hasNext())
				break;
			long itemID = ((Long)allItemIDs.next()).longValue();
			if (rescorer == null || !rescorer.isFiltered(Long.valueOf(itemID)))
			{
				double preference = estimator.estimate(Long.valueOf(itemID));
				double rescoredPref = rescorer != null ? rescorer.rescore(Long.valueOf(itemID), preference) : preference;
				if (!Double.isNaN(rescoredPref) && (!full || rescoredPref > lowestTopValue))
				{
					topItems.add(new GenericRecommendedItem(itemID, (float)rescoredPref));
					if (full)
						topItems.poll();
					else
					if (topItems.size() > howMany)
					{
						full = true;
						topItems.poll();
					}
					lowestTopValue = ((RecommendedItem)topItems.peek()).getValue();
				}
			}
		} while (true);
		List result = new ArrayList(topItems.size());
		result.addAll(topItems);
		Collections.sort(result);
		return result;
	}

	public static long[] getTopUsers(int howMany, LongPrimitiveIterator allUserIDs, Rescorer rescorer, Estimator estimator)
		throws TasteException
	{
		Queue topUsers = new PriorityQueue(howMany + 1, Collections.reverseOrder());
		boolean full = false;
		double lowestTopValue = (-1.0D / 0.0D);
		do
		{
			if (!allUserIDs.hasNext())
				break;
			long userID = ((Long)allUserIDs.next()).longValue();
			if (rescorer == null || !rescorer.isFiltered(Long.valueOf(userID)))
			{
				double similarity = estimator.estimate(Long.valueOf(userID));
				double rescoredSimilarity = rescorer != null ? rescorer.rescore(Long.valueOf(userID), similarity) : similarity;
				if (!Double.isNaN(rescoredSimilarity) && (!full || rescoredSimilarity > lowestTopValue))
				{
					topUsers.add(new SimilarUser(userID, similarity));
					if (full)
						topUsers.poll();
					else
					if (topUsers.size() > howMany)
					{
						full = true;
						topUsers.poll();
					}
					lowestTopValue = ((SimilarUser)topUsers.peek()).getSimilarity();
				}
			}
		} while (true);
		if (topUsers.isEmpty())
			return NO_IDS;
		List sorted = new ArrayList(topUsers.size());
		sorted.addAll(topUsers);
		Collections.sort(sorted);
		long result[] = new long[sorted.size()];
		int i = 0;
		for (Iterator i$ = sorted.iterator(); i$.hasNext();)
		{
			SimilarUser similarUser = (SimilarUser)i$.next();
			result[i++] = similarUser.getUserID();
		}

		return result;
	}

	public static List getTopItemItemSimilarities(int howMany, Iterable allSimilarities)
	{
		Queue topSimilarities = new PriorityQueue(howMany + 1, Collections.reverseOrder());
		boolean full = false;
		double lowestTopValue = (-1.0D / 0.0D);
		Iterator i$ = allSimilarities.iterator();
		do
		{
			if (!i$.hasNext())
				break;
			org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity.ItemItemSimilarity similarity = (org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity.ItemItemSimilarity)i$.next();
			double value = similarity.getValue();
			if (!Double.isNaN(value) && (!full || value > lowestTopValue))
			{
				topSimilarities.add(similarity);
				if (full)
					topSimilarities.poll();
				else
				if (topSimilarities.size() > howMany)
				{
					full = true;
					topSimilarities.poll();
				}
				lowestTopValue = ((org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity.ItemItemSimilarity)topSimilarities.peek()).getValue();
			}
		} while (true);
		List result = new ArrayList(topSimilarities.size());
		result.addAll(topSimilarities);
		Collections.sort(result);
		return result;
	}

	public static List getTopUserUserSimilarities(int howMany, Iterable allSimilarities)
	{
		Queue topSimilarities = new PriorityQueue(howMany + 1, Collections.reverseOrder());
		boolean full = false;
		double lowestTopValue = (-1.0D / 0.0D);
		Iterator i$ = allSimilarities.iterator();
		do
		{
			if (!i$.hasNext())
				break;
			org.apache.mahout.cf.taste.impl.similarity.GenericUserSimilarity.UserUserSimilarity similarity = (org.apache.mahout.cf.taste.impl.similarity.GenericUserSimilarity.UserUserSimilarity)i$.next();
			double value = similarity.getValue();
			if (!Double.isNaN(value) && (!full || value > lowestTopValue))
			{
				topSimilarities.add(similarity);
				if (full)
					topSimilarities.poll();
				else
				if (topSimilarities.size() > howMany)
				{
					full = true;
					topSimilarities.poll();
				}
				lowestTopValue = ((org.apache.mahout.cf.taste.impl.similarity.GenericUserSimilarity.UserUserSimilarity)topSimilarities.peek()).getValue();
			}
		} while (true);
		List result = new ArrayList(topSimilarities.size());
		result.addAll(topSimilarities);
		Collections.sort(result);
		return result;
	}

}
