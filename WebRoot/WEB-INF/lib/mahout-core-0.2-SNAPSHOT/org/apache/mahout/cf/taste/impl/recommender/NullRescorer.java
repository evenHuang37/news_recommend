// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NullRescorer.java

package org.apache.mahout.cf.taste.impl.recommender;

import org.apache.mahout.cf.taste.recommender.Rescorer;

public final class NullRescorer
	implements Rescorer
{

	private static final Rescorer userOrItemInstance = new NullRescorer();
	private static final Rescorer itemItemPairInstance = new NullRescorer();
	private static final Rescorer userUserPairInstance = new NullRescorer();

	public static Rescorer getItemInstance()
	{
		return userOrItemInstance;
	}

	public static Rescorer getUserInstance()
	{
		return userOrItemInstance;
	}

	public static Rescorer getItemItemPairInstance()
	{
		return itemItemPairInstance;
	}

	public static Rescorer getUserUserPairInstance()
	{
		return userUserPairInstance;
	}

	private NullRescorer()
	{
	}

	public double rescore(Object thing, double originalScore)
	{
		return originalScore;
	}

	public boolean isFiltered(Object thing)
	{
		return false;
	}

	public String toString()
	{
		return "NullRescorer";
	}

}
