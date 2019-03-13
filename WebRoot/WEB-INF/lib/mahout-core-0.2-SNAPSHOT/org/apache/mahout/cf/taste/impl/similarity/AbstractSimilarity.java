// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractSimilarity.java

package org.apache.mahout.cf.taste.impl.similarity;

import java.util.Collection;
import java.util.concurrent.Callable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.common.Weighting;
import org.apache.mahout.cf.taste.impl.common.RefreshHelper;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.similarity.*;
import org.apache.mahout.cf.taste.transforms.PreferenceTransform;
import org.apache.mahout.cf.taste.transforms.SimilarityTransform;

abstract class AbstractSimilarity
	implements UserSimilarity, ItemSimilarity
{

	private final DataModel dataModel;
	private PreferenceInferrer inferrer;
	private PreferenceTransform prefTransform;
	private SimilarityTransform similarityTransform;
	private boolean weighted;
	private int cachedNumItems;
	private int cachedNumUsers;
	private final RefreshHelper refreshHelper;

	AbstractSimilarity(DataModel dataModel)
		throws TasteException
	{
		this(dataModel, Weighting.UNWEIGHTED);
	}

	AbstractSimilarity(final DataModel dataModel, Weighting weighting)
		throws TasteException
	{
		if (dataModel == null)
		{
			throw new IllegalArgumentException("dataModel is null");
		} else
		{
			this.dataModel = dataModel;
			weighted = weighting == Weighting.WEIGHTED;
			cachedNumItems = dataModel.getNumItems();
			cachedNumUsers = dataModel.getNumUsers();
			refreshHelper = new RefreshHelper(new Callable() {

				final DataModel val$dataModel;
				final AbstractSimilarity this$0;

				public Object call()
					throws TasteException
				{
					cachedNumItems = dataModel.getNumItems();
					cachedNumUsers = dataModel.getNumUsers();
					return null;
				}

			
			{
				this$0 = AbstractSimilarity.this;
				dataModel = datamodel;
				super();
			}
			});
			refreshHelper.addDependency(this.dataModel);
			return;
		}
	}

	final DataModel getDataModel()
	{
		return dataModel;
	}

	final PreferenceInferrer getPreferenceInferrer()
	{
		return inferrer;
	}

	public final void setPreferenceInferrer(PreferenceInferrer inferrer)
	{
		if (inferrer == null)
		{
			throw new IllegalArgumentException("inferrer is null");
		} else
		{
			refreshHelper.addDependency(inferrer);
			refreshHelper.removeDependency(this.inferrer);
			this.inferrer = inferrer;
			return;
		}
	}

	public final PreferenceTransform getPrefTransform()
	{
		return prefTransform;
	}

	public final void setPrefTransform(PreferenceTransform prefTransform)
	{
		refreshHelper.addDependency(prefTransform);
		refreshHelper.removeDependency(this.prefTransform);
		this.prefTransform = prefTransform;
	}

	public final SimilarityTransform getSimilarityTransform()
	{
		return similarityTransform;
	}

	public final void setSimilarityTransform(SimilarityTransform similarityTransform)
	{
		refreshHelper.addDependency(similarityTransform);
		refreshHelper.removeDependency(this.similarityTransform);
		this.similarityTransform = similarityTransform;
	}

	final boolean isWeighted()
	{
		return weighted;
	}

	abstract double computeResult(int i, double d, double d1, double d2, 
			double d3);

	public double userSimilarity(long userID1, long userID2)
		throws TasteException
	{
		PreferenceArray xPrefs = dataModel.getPreferencesFromUser(userID1);
		PreferenceArray yPrefs = dataModel.getPreferencesFromUser(userID2);
		int xLength = xPrefs.length();
		int yLength = yPrefs.length();
		if (xLength == 0 || yLength == 0)
			return (0.0D / 0.0D);
		Preference xPref = xPrefs.get(0);
		Preference yPref = yPrefs.get(0);
		long xIndex = xPref.getItemID();
		long yIndex = yPref.getItemID();
		int xPrefIndex = 0;
		int yPrefIndex = 0;
		double sumX = 0.0D;
		double sumX2 = 0.0D;
		double sumY = 0.0D;
		double sumY2 = 0.0D;
		double sumXY = 0.0D;
		double sumXYdiff2 = 0.0D;
		int count = 0;
		boolean hasInferrer = inferrer != null;
		boolean hasPrefTransform = prefTransform != null;
		do
		{
			int compare = xIndex >= yIndex ? ((int) (xIndex <= yIndex ? 0 : 1)) : -1;
			if (hasInferrer || compare == 0)
			{
				double x;
				double y;
				if (xIndex == yIndex)
				{
					if (hasPrefTransform)
					{
						x = prefTransform.getTransformedValue(xPref);
						y = prefTransform.getTransformedValue(yPref);
					} else
					{
						x = xPref.getValue();
						y = yPref.getValue();
					}
				} else
				if (compare < 0)
				{
					x = hasPrefTransform ? prefTransform.getTransformedValue(xPref) : xPref.getValue();
					y = inferrer.inferPreference(userID2, xIndex);
				} else
				{
					x = inferrer.inferPreference(userID1, yIndex);
					y = hasPrefTransform ? prefTransform.getTransformedValue(yPref) : yPref.getValue();
				}
				sumXY += x * y;
				sumX += x;
				sumX2 += x * x;
				sumY += y;
				sumY2 += y * y;
				double diff = x - y;
				sumXYdiff2 += diff * diff;
				count++;
			}
			if (compare <= 0)
			{
				if (++xPrefIndex >= xLength)
					break;
				xPref = xPrefs.get(xPrefIndex);
				xIndex = xPref.getItemID();
			}
			if (compare < 0)
				continue;
			if (++yPrefIndex >= yLength)
				break;
			yPref = yPrefs.get(yPrefIndex);
			yIndex = yPref.getItemID();
		} while (true);
		double n = count;
		double meanX = sumX / n;
		double meanY = sumY / n;
		double centeredSumXY = sumXY - meanY * sumX;
		double centeredSumX2 = sumX2 - meanX * sumX;
		double centeredSumY2 = sumY2 - meanY * sumY;
		double result = computeResult(count, centeredSumXY, centeredSumX2, centeredSumY2, sumXYdiff2);
		if (similarityTransform != null)
			result = similarityTransform.transformSimilarity(userID1, userID2, result);
		if (!Double.isNaN(result))
			result = normalizeWeightResult(result, count, cachedNumItems);
		return result;
	}

	public final double itemSimilarity(long itemID1, long itemID2)
		throws TasteException
	{
		PreferenceArray xPrefs = dataModel.getPreferencesForItem(itemID1);
		PreferenceArray yPrefs = dataModel.getPreferencesForItem(itemID2);
		int xLength = xPrefs.length();
		int yLength = yPrefs.length();
		if (xLength == 0 || yLength == 0)
			return (0.0D / 0.0D);
		Preference xPref = xPrefs.get(0);
		Preference yPref = yPrefs.get(0);
		long xIndex = xPref.getUserID();
		long yIndex = yPref.getUserID();
		int xPrefIndex = 1;
		int yPrefIndex = 1;
		double sumX = 0.0D;
		double sumX2 = 0.0D;
		double sumY = 0.0D;
		double sumY2 = 0.0D;
		double sumXY = 0.0D;
		double sumXYdiff2 = 0.0D;
		int count = 0;
		do
		{
			int compare = xIndex >= yIndex ? ((int) (xIndex <= yIndex ? 0 : 1)) : -1;
			if (compare == 0)
			{
				double x = xPref.getValue();
				double y = yPref.getValue();
				sumXY += x * y;
				sumX += x;
				sumX2 += x * x;
				sumY += y;
				sumY2 += y * y;
				double diff = x - y;
				sumXYdiff2 += diff * diff;
				count++;
			}
			if (compare <= 0)
			{
				if (xPrefIndex == xLength)
					break;
				xPref = xPrefs.get(xPrefIndex++);
				xIndex = xPref.getUserID();
			}
			if (compare < 0)
				continue;
			if (yPrefIndex == yLength)
				break;
			yPref = yPrefs.get(yPrefIndex++);
			yIndex = yPref.getUserID();
		} while (true);
		double n = count;
		double meanX = sumX / n;
		double meanY = sumY / n;
		double centeredSumXY = sumXY - meanY * sumX;
		double centeredSumX2 = sumX2 - meanX * sumX;
		double centeredSumY2 = sumY2 - meanY * sumY;
		double result = computeResult(count, centeredSumXY, centeredSumX2, centeredSumY2, sumXYdiff2);
		if (similarityTransform != null)
			result = similarityTransform.transformSimilarity(itemID1, itemID2, result);
		if (!Double.isNaN(result))
			result = normalizeWeightResult(result, count, cachedNumUsers);
		return result;
	}

	final double normalizeWeightResult(double result, int count, int num)
	{
		if (weighted)
		{
			double scaleFactor = 1.0D - (double)count / (double)(num + 1);
			if (result < 0.0D)
				result = -1D + scaleFactor * (1.0D + result);
			else
				result = 1.0D - scaleFactor * (1.0D - result);
		}
		if (result < -1D)
			result = -1D;
		else
		if (result > 1.0D)
			result = 1.0D;
		return result;
	}

	public final void refresh(Collection alreadyRefreshed)
	{
		refreshHelper.refresh(alreadyRefreshed);
	}

	public final String toString()
	{
		return (new StringBuilder()).append(getClass().getSimpleName()).append("[dataModel:").append(dataModel).append(",inferrer:").append(inferrer).append(']').toString();
	}


}
