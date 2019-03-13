// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TreeClusteringRecommender.java

package org.apache.mahout.cf.taste.impl.recommender;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.*;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package org.apache.mahout.cf.taste.impl.recommender:
//			AbstractRecommender, ByRescoreComparator, ClusterSimilarity, TopItems

public final class TreeClusteringRecommender extends AbstractRecommender
	implements ClusteringRecommender
{
	private class Estimator
		implements TopItems.Estimator
	{

		private final FastIDSet cluster;
		final TreeClusteringRecommender this$0;

		public double estimate(Long itemID)
			throws TasteException
		{
			DataModel dataModel = getDataModel();
			RunningAverage average = new FullRunningAverage();
			LongPrimitiveIterator it = cluster.iterator();
			do
			{
				if (!it.hasNext())
					break;
				Float pref = dataModel.getPreferenceValue(((Long)it.next()).longValue(), itemID.longValue());
				if (pref != null)
					average.addDatum(pref.floatValue());
			} while (true);
			return average.getAverage();
		}

		public volatile double estimate(Object x0)
			throws TasteException
		{
			return estimate((Long)x0);
		}

		private Estimator(FastIDSet cluster)
		{
			this$0 = TreeClusteringRecommender.this;
			super();
			this.cluster = cluster;
		}

	}


	private static final Logger log = LoggerFactory.getLogger(org/apache/mahout/cf/taste/impl/recommender/TreeClusteringRecommender);
	private static final FastIDSet NO_CLUSTERS[] = new FastIDSet[0];
	private static final Random r = RandomUtils.getRandom();
	private final ClusterSimilarity clusterSimilarity;
	private final int numClusters;
	private final double clusteringThreshold;
	private final boolean clusteringByThreshold;
	private final double samplingRate;
	private FastByIDMap topRecsByUserID;
	private FastIDSet allClusters[];
	private FastByIDMap clustersByUserID;
	private boolean clustersBuilt;
	private final ReentrantLock buildClustersLock;
	private final RefreshHelper refreshHelper;

	public TreeClusteringRecommender(DataModel dataModel, ClusterSimilarity clusterSimilarity, int numClusters)
	{
		this(dataModel, clusterSimilarity, numClusters, 1.0D);
	}

	public TreeClusteringRecommender(DataModel dataModel, ClusterSimilarity clusterSimilarity, int numClusters, double samplingRate)
	{
		super(dataModel);
		if (clusterSimilarity == null)
			throw new IllegalArgumentException("clusterSimilarity is null");
		if (numClusters < 2)
			throw new IllegalArgumentException("numClusters must be at least 2");
		if (Double.isNaN(samplingRate) || samplingRate <= 0.0D || samplingRate > 1.0D)
		{
			throw new IllegalArgumentException((new StringBuilder()).append("samplingRate is invalid: ").append(samplingRate).toString());
		} else
		{
			this.clusterSimilarity = clusterSimilarity;
			this.numClusters = numClusters;
			clusteringThreshold = (0.0D / 0.0D);
			clusteringByThreshold = false;
			this.samplingRate = samplingRate;
			buildClustersLock = new ReentrantLock();
			refreshHelper = new RefreshHelper(new Callable() {

				final TreeClusteringRecommender this$0;

				public Object call()
					throws TasteException
				{
					buildClusters();
					return null;
				}

			
			{
				this$0 = TreeClusteringRecommender.this;
				super();
			}
			});
			refreshHelper.addDependency(dataModel);
			refreshHelper.addDependency(clusterSimilarity);
			return;
		}
	}

	public TreeClusteringRecommender(DataModel dataModel, ClusterSimilarity clusterSimilarity, double clusteringThreshold)
	{
		this(dataModel, clusterSimilarity, clusteringThreshold, 1.0D);
	}

	public TreeClusteringRecommender(DataModel dataModel, ClusterSimilarity clusterSimilarity, double clusteringThreshold, double samplingRate)
	{
		super(dataModel);
		if (clusterSimilarity == null)
			throw new IllegalArgumentException("clusterSimilarity is null");
		if (Double.isNaN(clusteringThreshold))
			throw new IllegalArgumentException("clusteringThreshold must not be NaN");
		if (Double.isNaN(samplingRate) || samplingRate <= 0.0D || samplingRate > 1.0D)
		{
			throw new IllegalArgumentException((new StringBuilder()).append("samplingRate is invalid: ").append(samplingRate).toString());
		} else
		{
			this.clusterSimilarity = clusterSimilarity;
			numClusters = 0x80000000;
			this.clusteringThreshold = clusteringThreshold;
			clusteringByThreshold = true;
			this.samplingRate = samplingRate;
			buildClustersLock = new ReentrantLock();
			refreshHelper = new RefreshHelper(new Callable() {

				final TreeClusteringRecommender this$0;

				public Object call()
					throws TasteException
				{
					buildClusters();
					return null;
				}

			
			{
				this$0 = TreeClusteringRecommender.this;
				super();
			}
			});
			refreshHelper.addDependency(dataModel);
			refreshHelper.addDependency(clusterSimilarity);
			return;
		}
	}

	public List recommend(long userID, int howMany, Rescorer rescorer)
		throws TasteException
	{
		if (howMany < 1)
			throw new IllegalArgumentException("howMany must be at least 1");
		checkClustersBuilt();
		log.debug("Recommending items for user ID '{}'", Long.valueOf(userID));
		List recommended = (List)topRecsByUserID.get(userID);
		if (recommended == null)
			return Collections.emptyList();
		DataModel dataModel = getDataModel();
		List rescored = new ArrayList(recommended.size());
		Iterator i$ = recommended.iterator();
		do
		{
			if (!i$.hasNext())
				break;
			RecommendedItem recommendedItem = (RecommendedItem)i$.next();
			long itemID = recommendedItem.getItemID();
			if ((rescorer == null || !rescorer.isFiltered(Long.valueOf(itemID))) && (dataModel.getPreferenceValue(userID, itemID) == null && (rescorer == null || !Double.isNaN(rescorer.rescore(Long.valueOf(itemID), recommendedItem.getValue())))))
				rescored.add(recommendedItem);
		} while (true);
		Collections.sort(rescored, new ByRescoreComparator(rescorer));
		return rescored;
	}

	public float estimatePreference(long userID, long itemID)
		throws TasteException
	{
label0:
		{
			DataModel model = getDataModel();
			Float actualPref = model.getPreferenceValue(userID, itemID);
			if (actualPref != null)
				return actualPref.floatValue();
			checkClustersBuilt();
			List topRecsForUser = (List)topRecsByUserID.get(userID);
			if (topRecsForUser == null)
				break label0;
			Iterator i$ = topRecsForUser.iterator();
			RecommendedItem item;
			do
			{
				if (!i$.hasNext())
					break label0;
				item = (RecommendedItem)i$.next();
			} while (itemID != item.getItemID());
			return item.getValue();
		}
		return (0.0F / 0.0F);
	}

	public FastIDSet getCluster(long userID)
		throws TasteException
	{
		checkClustersBuilt();
		FastIDSet cluster = (FastIDSet)clustersByUserID.get(userID);
		return cluster != null ? cluster : new FastIDSet();
	}

	public FastIDSet[] getClusters()
		throws TasteException
	{
		checkClustersBuilt();
		return allClusters;
	}

	private void checkClustersBuilt()
		throws TasteException
	{
		if (!clustersBuilt)
			buildClusters();
	}

	private void buildClusters()
		throws TasteException
	{
		buildClustersLock.lock();
		DataModel model = getDataModel();
		int numUsers = model.getNumUsers();
		if (numUsers > 0)
		{
			List newClusters = new ArrayList(numUsers);
			FastIDSet newCluster;
			for (LongPrimitiveIterator it = model.getUserIDs(); it.hasNext(); newClusters.add(newCluster))
			{
				newCluster = new FastIDSet();
				newCluster.add(it.nextLong());
			}

			if (numUsers > 1)
				findClusters(newClusters);
			topRecsByUserID = computeTopRecsPerUserID(newClusters);
			clustersByUserID = computeClustersPerUserID(newClusters);
			allClusters = (FastIDSet[])newClusters.toArray(new FastIDSet[newClusters.size()]);
		} else
		{
			topRecsByUserID = new FastByIDMap();
			clustersByUserID = new FastByIDMap();
			allClusters = NO_CLUSTERS;
		}
		clustersBuilt = true;
		buildClustersLock.unlock();
		break MISSING_BLOCK_LABEL_192;
		Exception exception;
		exception;
		buildClustersLock.unlock();
		throw exception;
	}

	private void findClusters(List newClusters)
		throws TasteException
	{
		if (clusteringByThreshold)
		{
			Pair nearestPair = findNearestClusters(newClusters);
			if (nearestPair != null)
			{
				FastIDSet cluster1 = (FastIDSet)nearestPair.getFirst();
				FastIDSet cluster2 = (FastIDSet)nearestPair.getSecond();
				do
				{
					if (clusterSimilarity.getSimilarity(cluster1, cluster2) < clusteringThreshold)
						break;
					newClusters.remove(cluster1);
					newClusters.remove(cluster2);
					FastIDSet merged = new FastIDSet(cluster1.size() + cluster2.size());
					merged.addAll(cluster1);
					merged.addAll(cluster2);
					newClusters.add(merged);
					nearestPair = findNearestClusters(newClusters);
					if (nearestPair == null)
						break;
					cluster1 = (FastIDSet)nearestPair.getFirst();
					cluster2 = (FastIDSet)nearestPair.getSecond();
				} while (true);
			}
		} else
		{
			do
			{
				if (newClusters.size() <= numClusters)
					break;
				Pair nearestPair = findNearestClusters(newClusters);
				if (nearestPair == null)
					break;
				FastIDSet cluster1 = (FastIDSet)nearestPair.getFirst();
				FastIDSet cluster2 = (FastIDSet)nearestPair.getSecond();
				newClusters.remove(cluster1);
				newClusters.remove(cluster2);
				FastIDSet merged = new FastIDSet(cluster1.size() + cluster2.size());
				merged.addAll(cluster1);
				merged.addAll(cluster2);
				newClusters.add(merged);
			} while (true);
		}
	}

	private Pair findNearestClusters(List clusters)
		throws TasteException
	{
		int size = clusters.size();
		Pair nearestPair = null;
		double bestSimilarity = (-1.0D / 0.0D);
		for (int i = 0; i < size; i++)
		{
			FastIDSet cluster1 = (FastIDSet)clusters.get(i);
			for (int j = i + 1; j < size; j++)
			{
				if (samplingRate < 1.0D && r.nextDouble() >= samplingRate)
					continue;
				FastIDSet cluster2 = (FastIDSet)clusters.get(j);
				double similarity = clusterSimilarity.getSimilarity(cluster1, cluster2);
				if (!Double.isNaN(similarity) && similarity > bestSimilarity)
				{
					bestSimilarity = similarity;
					nearestPair = new Pair(cluster1, cluster2);
				}
			}

		}

		return nearestPair;
	}

	private FastByIDMap computeTopRecsPerUserID(List clusters)
		throws TasteException
	{
		FastByIDMap recsPerUser = new FastByIDMap();
		for (Iterator i$ = clusters.iterator(); i$.hasNext();)
		{
			FastIDSet cluster = (FastIDSet)i$.next();
			List recs = computeTopRecsForCluster(cluster);
			LongPrimitiveIterator it = cluster.iterator();
			while (it.hasNext()) 
				recsPerUser.put(((Long)it.next()).longValue(), recs);
		}

		return recsPerUser;
	}

	private List computeTopRecsForCluster(FastIDSet cluster)
		throws TasteException
	{
		DataModel dataModel = getDataModel();
		FastIDSet allItemIDs = new FastIDSet();
		for (LongPrimitiveIterator it = cluster.iterator(); it.hasNext();)
		{
			PreferenceArray prefs = dataModel.getPreferencesFromUser(((Long)it.next()).longValue());
			int size = prefs.length();
			int i = 0;
			while (i < size) 
			{
				allItemIDs.add(prefs.getItemID(i));
				i++;
			}
		}

		TopItems.Estimator estimator = new Estimator(cluster);
		List topItems = TopItems.getTopItems(100, allItemIDs.iterator(), null, estimator);
		log.debug("Recommendations are: {}", topItems);
		return Collections.unmodifiableList(topItems);
	}

	private static FastByIDMap computeClustersPerUserID(List clusters)
	{
		FastByIDMap clustersPerUser = new FastByIDMap(clusters.size());
		for (Iterator i$ = clusters.iterator(); i$.hasNext();)
		{
			FastIDSet cluster = (FastIDSet)i$.next();
			LongPrimitiveIterator it = cluster.iterator();
			while (it.hasNext()) 
				clustersPerUser.put(((Long)it.next()).longValue(), cluster);
		}

		return clustersPerUser;
	}

	public void refresh(Collection alreadyRefreshed)
	{
		refreshHelper.refresh(alreadyRefreshed);
	}

	public String toString()
	{
		return (new StringBuilder()).append("TreeClusteringRecommender[clusterSimilarity:").append(clusterSimilarity).append(']').toString();
	}


}
