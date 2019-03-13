// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GenericItemSimilarity.java

package org.apache.mahout.cf.taste.impl.similarity;

import java.util.*;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.*;
import org.apache.mahout.cf.taste.impl.recommender.TopItems;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public final class GenericItemSimilarity
	implements ItemSimilarity
{
	private static final class DataModelSimilaritiesIterator
		implements Iterator
	{

		private final ItemSimilarity otherSimilarity;
		private final long itemIDs[];
		private final int size;
		private int i;
		private long itemID1;
		private int j;

		public boolean hasNext()
		{
			return i < size - 1;
		}

		public ItemItemSimilarity next()
		{
			if (!hasNext())
				throw new NoSuchElementException();
			long itemID2 = itemIDs[j];
			double similarity;
			try
			{
				similarity = otherSimilarity.itemSimilarity(itemID1, itemID2);
			}
			catch (TasteException te)
			{
				throw new RuntimeException(te);
			}
			ItemItemSimilarity result = new ItemItemSimilarity(itemID1, itemID2, similarity);
			j++;
			if (j == size)
			{
				i++;
				itemID1 = itemIDs[i];
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

		private DataModelSimilaritiesIterator(ItemSimilarity otherSimilarity, long itemIDs[])
		{
			this.otherSimilarity = otherSimilarity;
			this.itemIDs = itemIDs;
			size = itemIDs.length;
			i = 0;
			itemID1 = itemIDs[0];
			j = 1;
		}

	}

	public static final class ItemItemSimilarity
		implements Comparable
	{

		private final long itemID1;
		private final long itemID2;
		private final double value;

		public long getItemID1()
		{
			return itemID1;
		}

		public long getItemID2()
		{
			return itemID2;
		}

		public double getValue()
		{
			return value;
		}

		public String toString()
		{
			return (new StringBuilder()).append("ItemItemSimilarity[").append(itemID1).append(',').append(itemID2).append(':').append(value).append(']').toString();
		}

		public int compareTo(ItemItemSimilarity other)
		{
			double otherValue = other.value;
			return value <= otherValue ? ((int) (value >= otherValue ? 0 : 1)) : -1;
		}

		public boolean equals(Object other)
		{
			if (!(other instanceof ItemItemSimilarity))
			{
				return false;
			} else
			{
				ItemItemSimilarity otherSimilarity = (ItemItemSimilarity)other;
				return otherSimilarity.itemID1 == itemID1 && otherSimilarity.itemID2 == itemID2 && otherSimilarity.value == value;
			}
		}

		public int hashCode()
		{
			return (int)itemID1 ^ (int)itemID2 ^ RandomUtils.hashDouble(value);
		}

		public volatile int compareTo(Object x0)
		{
			return compareTo((ItemItemSimilarity)x0);
		}

		public ItemItemSimilarity(long itemID1, long itemID2, double value)
		{
			if (Double.isNaN(value) || value < -1D || value > 1.0D)
			{
				throw new IllegalArgumentException((new StringBuilder()).append("Illegal value: ").append(value).toString());
			} else
			{
				this.itemID1 = itemID1;
				this.itemID2 = itemID2;
				this.value = value;
				return;
			}
		}
	}


	private final FastByIDMap similarityMaps;

	public GenericItemSimilarity(Iterable similarities)
	{
		similarityMaps = new FastByIDMap();
		initSimilarityMaps(similarities);
	}

	public GenericItemSimilarity(Iterable similarities, int maxToKeep)
	{
		similarityMaps = new FastByIDMap();
		Iterable keptSimilarities = TopItems.getTopItemItemSimilarities(maxToKeep, similarities);
		initSimilarityMaps(keptSimilarities);
	}

	public GenericItemSimilarity(ItemSimilarity otherSimilarity, DataModel dataModel)
		throws TasteException
	{
		similarityMaps = new FastByIDMap();
		long itemIDs[] = IteratorUtils.longIteratorToList(dataModel.getItemIDs());
		Iterator it = new DataModelSimilaritiesIterator(otherSimilarity, itemIDs);
		initSimilarityMaps(new IteratorIterable(it));
	}

	public GenericItemSimilarity(ItemSimilarity otherSimilarity, DataModel dataModel, int maxToKeep)
		throws TasteException
	{
		similarityMaps = new FastByIDMap();
		long itemIDs[] = IteratorUtils.longIteratorToList(dataModel.getItemIDs());
		Iterator it = new DataModelSimilaritiesIterator(otherSimilarity, itemIDs);
		Iterable keptSimilarities = TopItems.getTopItemItemSimilarities(maxToKeep, new IteratorIterable(it));
		initSimilarityMaps(keptSimilarities);
	}

	private void initSimilarityMaps(Iterable similarities)
	{
		Iterator i$ = similarities.iterator();
		do
		{
			if (!i$.hasNext())
				break;
			ItemItemSimilarity iic = (ItemItemSimilarity)i$.next();
			long similarityItemID1 = iic.getItemID1();
			long similarityItemID2 = iic.getItemID2();
			if (similarityItemID1 != similarityItemID2)
			{
				long itemID1;
				long itemID2;
				if (similarityItemID1 < similarityItemID2)
				{
					itemID1 = similarityItemID1;
					itemID2 = similarityItemID2;
				} else
				{
					itemID1 = similarityItemID2;
					itemID2 = similarityItemID1;
				}
				FastByIDMap map = (FastByIDMap)similarityMaps.get(itemID1);
				if (map == null)
				{
					map = new FastByIDMap();
					similarityMaps.put(itemID1, map);
				}
				map.put(itemID2, Double.valueOf(iic.getValue()));
			}
		} while (true);
	}

	public double itemSimilarity(long itemID1, long itemID2)
	{
		if (itemID1 == itemID2)
			return 1.0D;
		long firstID;
		long secondID;
		if (itemID1 < itemID2)
		{
			firstID = itemID1;
			secondID = itemID2;
		} else
		{
			firstID = itemID2;
			secondID = itemID1;
		}
		FastByIDMap nextMap = (FastByIDMap)similarityMaps.get(firstID);
		if (nextMap == null)
		{
			return (0.0D / 0.0D);
		} else
		{
			Double similarity = (Double)nextMap.get(secondID);
			return similarity != null ? similarity.doubleValue() : (0.0D / 0.0D);
		}
	}

	public void refresh(Collection collection)
	{
	}
}
