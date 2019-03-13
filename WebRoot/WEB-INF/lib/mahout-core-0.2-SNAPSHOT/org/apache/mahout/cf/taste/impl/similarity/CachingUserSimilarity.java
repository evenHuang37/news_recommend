// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CachingUserSimilarity.java

package org.apache.mahout.cf.taste.impl.similarity;

import java.util.Collection;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.*;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.PreferenceInferrer;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public final class CachingUserSimilarity
	implements UserSimilarity
{
	private static final class SimilarityRetriever
		implements Retriever
	{

		private final UserSimilarity similarity;

		public Double get(LongPair key)
			throws TasteException
		{
			return Double.valueOf(similarity.userSimilarity(key.getFirst(), key.getSecond()));
		}

		public volatile Object get(Object x0)
			throws TasteException
		{
			return get((LongPair)x0);
		}

		private SimilarityRetriever(UserSimilarity similarity)
		{
			this.similarity = similarity;
		}

	}


	private final UserSimilarity similarity;
	private final Cache similarityCache;

	public CachingUserSimilarity(UserSimilarity similarity, DataModel dataModel)
		throws TasteException
	{
		if (similarity == null)
		{
			throw new IllegalArgumentException("similarity is null");
		} else
		{
			this.similarity = similarity;
			int maxCacheSize = dataModel.getNumUsers();
			similarityCache = new Cache(new SimilarityRetriever(similarity), maxCacheSize);
			return;
		}
	}

	public double userSimilarity(long userID1, long userID2)
		throws TasteException
	{
		LongPair key = userID1 >= userID2 ? new LongPair(userID2, userID1) : new LongPair(userID1, userID2);
		return ((Double)similarityCache.get(key)).doubleValue();
	}

	public void setPreferenceInferrer(PreferenceInferrer inferrer)
	{
		similarityCache.clear();
		similarity.setPreferenceInferrer(inferrer);
	}

	public void refresh(Collection alreadyRefreshed)
	{
		similarityCache.clear();
		alreadyRefreshed = RefreshHelper.buildRefreshed(alreadyRefreshed);
		RefreshHelper.maybeRefresh(alreadyRefreshed, similarity);
	}
}
