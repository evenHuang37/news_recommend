// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CachingItemSimilarity.java

package org.apache.mahout.cf.taste.impl.similarity;

import java.util.Collection;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.*;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public final class CachingItemSimilarity
	implements ItemSimilarity
{
	private static final class SimilarityRetriever
		implements Retriever
	{

		private final ItemSimilarity similarity;

		public Double get(LongPair key)
			throws TasteException
		{
			return Double.valueOf(similarity.itemSimilarity(key.getFirst(), key.getSecond()));
		}

		public volatile Object get(Object x0)
			throws TasteException
		{
			return get((LongPair)x0);
		}

		private SimilarityRetriever(ItemSimilarity similarity)
		{
			this.similarity = similarity;
		}

	}


	private final ItemSimilarity similarity;
	private final Cache similarityCache;

	public CachingItemSimilarity(ItemSimilarity similarity, DataModel dataModel)
		throws TasteException
	{
		if (similarity == null)
		{
			throw new IllegalArgumentException("similarity is null");
		} else
		{
			this.similarity = similarity;
			int maxCacheSize = dataModel.getNumItems();
			similarityCache = new Cache(new SimilarityRetriever(similarity), maxCacheSize);
			return;
		}
	}

	public double itemSimilarity(long itemID1, long itemID2)
		throws TasteException
	{
		LongPair key = itemID1 >= itemID2 ? new LongPair(itemID2, itemID1) : new LongPair(itemID1, itemID2);
		return ((Double)similarityCache.get(key)).doubleValue();
	}

	public void refresh(Collection alreadyRefreshed)
	{
		similarityCache.clear();
		alreadyRefreshed = RefreshHelper.buildRefreshed(alreadyRefreshed);
		RefreshHelper.maybeRefresh(alreadyRefreshed, similarity);
	}
}
