// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AveragingPreferenceInferrer.java

package org.apache.mahout.cf.taste.impl.similarity;

import java.util.Collection;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.*;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.similarity.PreferenceInferrer;

public final class AveragingPreferenceInferrer
	implements PreferenceInferrer
{
	private final class PrefRetriever
		implements Retriever
	{

		final AveragingPreferenceInferrer this$0;

		public Float get(Long key)
			throws TasteException
		{
			RunningAverage average = new FullRunningAverage();
			PreferenceArray prefs = dataModel.getPreferencesFromUser(key.longValue());
			int size = prefs.length();
			if (size == 0)
				return AveragingPreferenceInferrer.ZERO;
			for (int i = 0; i < size; i++)
				average.addDatum(prefs.getValue(i));

			return Float.valueOf((float)average.getAverage());
		}

		public volatile Object get(Object x0)
			throws TasteException
		{
			return get((Long)x0);
		}

		private PrefRetriever()
		{
			this$0 = AveragingPreferenceInferrer.this;
			super();
		}

	}


	private static final Float ZERO = Float.valueOf(0.0F);
	private final DataModel dataModel;
	private final Cache averagePreferenceValue;

	public AveragingPreferenceInferrer(DataModel dataModel)
		throws TasteException
	{
		this.dataModel = dataModel;
		Retriever retriever = new PrefRetriever();
		averagePreferenceValue = new Cache(retriever, dataModel.getNumUsers());
		refresh(null);
	}

	public float inferPreference(long userID, long itemID)
		throws TasteException
	{
		return ((Float)averagePreferenceValue.get(Long.valueOf(userID))).floatValue();
	}

	public void refresh(Collection alreadyRefreshed)
	{
		averagePreferenceValue.clear();
	}

	public String toString()
	{
		return "AveragingPreferenceInferrer";
	}



}
