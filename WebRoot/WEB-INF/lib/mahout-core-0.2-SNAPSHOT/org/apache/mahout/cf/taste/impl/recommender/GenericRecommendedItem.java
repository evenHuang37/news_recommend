// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GenericRecommendedItem.java

package org.apache.mahout.cf.taste.impl.recommender;

import java.io.Serializable;
import org.apache.mahout.cf.taste.impl.common.RandomUtils;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

public final class GenericRecommendedItem
	implements RecommendedItem, Serializable
{

	private final long itemID;
	private final float value;

	public GenericRecommendedItem(long itemID, float value)
	{
		if (Float.isNaN(value))
		{
			throw new IllegalArgumentException("value is NaN");
		} else
		{
			this.itemID = itemID;
			this.value = value;
			return;
		}
	}

	public long getItemID()
	{
		return itemID;
	}

	public float getValue()
	{
		return value;
	}

	public String toString()
	{
		return (new StringBuilder()).append("RecommendedItem[item:").append(itemID).append(", value:").append(value).append(']').toString();
	}

	public int hashCode()
	{
		return (int)itemID ^ RandomUtils.hashFloat(value);
	}

	public boolean equals(Object o)
	{
		if (!(o instanceof GenericRecommendedItem))
		{
			return false;
		} else
		{
			GenericRecommendedItem other = (GenericRecommendedItem)o;
			return itemID == other.itemID && value == other.value;
		}
	}

	public int compareTo(RecommendedItem other)
	{
		float otherValue = other.getValue();
		return value <= otherValue ? ((int) (value >= otherValue ? 0 : 1)) : -1;
	}

	public volatile int compareTo(Object x0)
	{
		return compareTo((RecommendedItem)x0);
	}
}
