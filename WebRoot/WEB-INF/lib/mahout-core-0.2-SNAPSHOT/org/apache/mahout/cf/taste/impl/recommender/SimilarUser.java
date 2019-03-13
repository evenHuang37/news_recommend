// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SimilarUser.java

package org.apache.mahout.cf.taste.impl.recommender;

import org.apache.mahout.cf.taste.impl.common.RandomUtils;

public final class SimilarUser
	implements Comparable
{

	private final long userID;
	private final double similarity;

	public SimilarUser(long userID, double similarity)
	{
		this.userID = userID;
		this.similarity = similarity;
	}

	long getUserID()
	{
		return userID;
	}

	double getSimilarity()
	{
		return similarity;
	}

	public int hashCode()
	{
		return (int)userID ^ RandomUtils.hashDouble(similarity);
	}

	public boolean equals(Object o)
	{
		if (!(o instanceof SimilarUser))
		{
			return false;
		} else
		{
			SimilarUser other = (SimilarUser)o;
			return userID == other.userID && similarity == other.similarity;
		}
	}

	public String toString()
	{
		return (new StringBuilder()).append("SimilarUser[user:").append(userID).append(", similarity:").append(similarity).append(']').toString();
	}

	public int compareTo(SimilarUser other)
	{
		double otherSimilarity = other.similarity;
		return similarity <= otherSimilarity ? ((int) (similarity >= otherSimilarity ? 0 : 1)) : -1;
	}

	public volatile int compareTo(Object x0)
	{
		return compareTo((SimilarUser)x0);
	}
}
