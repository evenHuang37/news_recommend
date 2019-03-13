// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GenericUserSimilarity.java

package org.apache.mahout.cf.taste.impl.similarity;

import java.util.*;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.*;
import org.apache.mahout.cf.taste.impl.recommender.TopItems;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.PreferenceInferrer;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public final class GenericUserSimilarity
	implements UserSimilarity
{
	private static final class DataModelSimilaritiesIterator
		implements Iterator
	{

		private final UserSimilarity otherSimilarity;
		private final long userIDs[];
		private final int size;
		private int i;
		private long userID1;
		private int j;

		public boolean hasNext()
		{
			return i < size - 1;
		}

		public UserUserSimilarity next()
		{
			if (!hasNext())
				throw new NoSuchElementException();
			long userID2 = userIDs[j];
			double similarity;
			try
			{
				similarity = otherSimilarity.userSimilarity(userID1, userID2);
			}
			catch (TasteException te)
			{
				throw new RuntimeException(te);
			}
			UserUserSimilarity result = new UserUserSimilarity(userID1, userID2, similarity);
			j++;
			if (j == size)
			{
				i++;
				userID1 = userIDs[i];
				j = i + 1;
			}
			return result;
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}

		public volatile Object next()
		{
			return next();
		}

		private DataModelSimilaritiesIterator(UserSimilarity otherSimilarity, long userIDs[])
		{
			this.otherSimilarity = otherSimilarity;
			this.userIDs = userIDs;
			size = userIDs.length;
			i = 0;
			userID1 = userIDs[0];
			j = 1;
		}

	}

	public static final class UserUserSimilarity
		implements Comparable
	{

		private final long userID1;
		private final long userID2;
		private final double value;

		public long getUserID1()
		{
			return userID1;
		}

		public long getUserID2()
		{
			return userID2;
		}

		public double getValue()
		{
			return value;
		}

		public String toString()
		{
			return (new StringBuilder()).append("UserUserSimilarity[").append(userID1).append(',').append(userID2).append(':').append(value).append(']').toString();
		}

		public int compareTo(UserUserSimilarity other)
		{
			double otherValue = other.value;
			return value <= otherValue ? ((int) (value >= otherValue ? 0 : 1)) : -1;
		}

		public boolean equals(Object other)
		{
			if (!(other instanceof UserUserSimilarity))
			{
				return false;
			} else
			{
				UserUserSimilarity otherSimilarity = (UserUserSimilarity)other;
				return otherSimilarity.userID1 == userID1 && otherSimilarity.userID2 == userID2 && otherSimilarity.value == value;
			}
		}

		public int hashCode()
		{
			return (int)userID1 ^ (int)userID2 ^ RandomUtils.hashDouble(value);
		}

		public volatile int compareTo(Object x0)
		{
			return compareTo((UserUserSimilarity)x0);
		}

		public UserUserSimilarity(long userID1, long userID2, double value)
		{
			if (Double.isNaN(value) || value < -1D || value > 1.0D)
			{
				throw new IllegalArgumentException((new StringBuilder()).append("Illegal value: ").append(value).toString());
			} else
			{
				this.userID1 = userID1;
				this.userID2 = userID2;
				this.value = value;
				return;
			}
		}
	}


	private final FastByIDMap similarityMaps;

	public GenericUserSimilarity(Iterable similarities)
	{
		similarityMaps = new FastByIDMap();
		initSimilarityMaps(similarities);
	}

	public GenericUserSimilarity(Iterable similarities, int maxToKeep)
	{
		similarityMaps = new FastByIDMap();
		Iterable keptSimilarities = TopItems.getTopUserUserSimilarities(maxToKeep, similarities);
		initSimilarityMaps(keptSimilarities);
	}

	public GenericUserSimilarity(UserSimilarity otherSimilarity, DataModel dataModel)
		throws TasteException
	{
		similarityMaps = new FastByIDMap();
		long userIDs[] = IteratorUtils.longIteratorToList(dataModel.getUserIDs());
		Iterator it = new DataModelSimilaritiesIterator(otherSimilarity, userIDs);
		initSimilarityMaps(new IteratorIterable(it));
	}

	public GenericUserSimilarity(UserSimilarity otherSimilarity, DataModel dataModel, int maxToKeep)
		throws TasteException
	{
		similarityMaps = new FastByIDMap();
		long userIDs[] = IteratorUtils.longIteratorToList(dataModel.getUserIDs());
		Iterator it = new DataModelSimilaritiesIterator(otherSimilarity, userIDs);
		Iterable keptSimilarities = TopItems.getTopUserUserSimilarities(maxToKeep, new IteratorIterable(it));
		initSimilarityMaps(keptSimilarities);
	}

	private void initSimilarityMaps(Iterable similarities)
	{
		Iterator i$ = similarities.iterator();
		do
		{
			if (!i$.hasNext())
				break;
			UserUserSimilarity uuc = (UserUserSimilarity)i$.next();
			long similarityUser1 = uuc.getUserID1();
			long similarityUser2 = uuc.getUserID2();
			if (similarityUser1 != similarityUser2)
			{
				long user1;
				long user2;
				if (similarityUser1 < similarityUser2)
				{
					user1 = similarityUser1;
					user2 = similarityUser2;
				} else
				{
					user1 = similarityUser2;
					user2 = similarityUser1;
				}
				FastByIDMap map = (FastByIDMap)similarityMaps.get(user1);
				if (map == null)
				{
					map = new FastByIDMap();
					similarityMaps.put(user1, map);
				}
				map.put(user2, Double.valueOf(uuc.getValue()));
			}
		} while (true);
	}

	public double userSimilarity(long userID1, long userID2)
	{
		if (userID1 == userID2)
			return 1.0D;
		long first;
		long second;
		if (userID1 < userID2)
		{
			first = userID1;
			second = userID2;
		} else
		{
			first = userID2;
			second = userID1;
		}
		FastByIDMap nextMap = (FastByIDMap)similarityMaps.get(first);
		if (nextMap == null)
		{
			return (0.0D / 0.0D);
		} else
		{
			Double similarity = (Double)nextMap.get(second);
			return similarity != null ? similarity.doubleValue() : (0.0D / 0.0D);
		}
	}

	public void setPreferenceInferrer(PreferenceInferrer inferrer)
	{
		throw new UnsupportedOperationException();
	}

	public void refresh(Collection collection)
	{
	}
}
