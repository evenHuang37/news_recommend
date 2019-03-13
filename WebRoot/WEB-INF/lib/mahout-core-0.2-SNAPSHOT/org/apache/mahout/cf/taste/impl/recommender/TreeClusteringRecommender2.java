// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TreeClusteringRecommender2.java

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

public final class TreeClusteringRecommender2 extends AbstractRecommender
	implements ClusteringRecommender
{
	private class Estimator
		implements TopItems.Estimator
	{

		private final FastIDSet cluster;
		final TreeClusteringRecommender2 this$0;

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
			this$0 = TreeClusteringRecommender2.this;
			super();
			this.cluster = cluster;
		}

	}

	private static final class ClusterClusterPair
		implements Comparable
	{

		private final FastIDSet cluster1;
		private final FastIDSet cluster2;
		private final double similarity;

		private FastIDSet getCluster1()
		{
			return cluster1;
		}

		private FastIDSet getCluster2()
		{
			return cluster2;
		}

		private double getSimilarity()
		{
			return similarity;
		}

		public int hashCode()
		{
			return cluster1.hashCode() ^ cluster2.hashCode() ^ RandomUtils.hashDouble(similarity);
		}

		public boolean equals(Object o)
		{
			if (!(o instanceof ClusterClusterPair))
			{
				return false;
			} else
			{
				ClusterClusterPair other = (ClusterClusterPair)o;
				return cluster1.equals(other.cluster1) && cluster2.equals(other.cluster2) && similarity == other.similarity;
			}
		}

		public int compareTo(ClusterClusterPair other)
		{
			double otherSimilarity = other.similarity;
			if (similarity > otherSimilarity)
				return -1;
			return similarity >= otherSimilarity ? 0 : 1;
		}

		public volatile int compareTo(Object x0)
		{
			return compareTo((ClusterClusterPair)x0);
		}




		private ClusterClusterPair(FastIDSet cluster1, FastIDSet cluster2, double similarity)
		{
			this.cluster1 = cluster1;
			this.cluster2 = cluster2;
			this.similarity = similarity;
		}

		ClusterClusterPair(FastIDSet x0, FastIDSet x1, double x2, 1 x3)
		{
			this(x0, x1, x2);
		}
	}


	private static final Logger log = LoggerFactory.getLogger(org/apache/mahout/cf/taste/impl/recommender/TreeClusteringRecommender2);
	private final ClusterSimilarity clusterSimilarity;
	private final int numClusters;
	private final double clusteringThreshold;
	private final boolean clusteringByThreshold;
	private FastByIDMap topRecsByUserID;
	private FastIDSet allClusters[];
	private FastByIDMap clustersByUserID;
	private boolean clustersBuilt;
	private final ReentrantLock buildClustersLock;
	private final RefreshHelper refreshHelper;

	public TreeClusteringRecommender2(DataModel dataModel, ClusterSimilarity clusterSimilarity, int numClusters)
	{
		super(dataModel);
		if (clusterSimilarity == null)
			throw new IllegalArgumentException("clusterSimilarity is null");
		if (numClusters < 2)
		{
			throw new IllegalArgumentException("numClusters must be at least 2");
		} else
		{
			this.clusterSimilarity = clusterSimilarity;
			this.numClusters = numClusters;
			clusteringThreshold = (0.0D / 0.0D);
			clusteringByThreshold = false;
			buildClustersLock = new ReentrantLock();
			refreshHelper = new RefreshHelper(new Callable() {

				final TreeClusteringRecommender2 this$0;

				public Object call()
					throws TasteException
				{
					buildClusters();
					return null;
				}

			
			{
				this$0 = TreeClusteringRecommender2.this;
				super();
			}
			});
			refreshHelper.addDependency(dataModel);
			refreshHelper.addDependency(clusterSimilarity);
			return;
		}
	}

	public TreeClusteringRecommender2(DataModel dataModel, ClusterSimilarity clusterSimilarity, double clusteringThreshold)
	{
		super(dataModel);
		if (clusterSimilarity == null)
			throw new IllegalArgumentException("clusterSimilarity is null");
		if (Double.isNaN(clusteringThreshold))
		{
			throw new IllegalArgumentException("clusteringThreshold must not be NaN");
		} else
		{
			this.clusterSimilarity = clusterSimilarity;
			numClusters = 0x80000000;
			this.clusteringThreshold = clusteringThreshold;
			clusteringByThreshold = true;
			buildClustersLock = new ReentrantLock();
			refreshHelper = new RefreshHelper(new Callable() {

				final TreeClusteringRecommender2 this$0;

				public Object call()
					throws TasteException
				{
					buildClusters();
					return null;
				}

			
			{
				this$0 = TreeClusteringRecommender2.this;
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
			Float actualPref = getDataModel().getPreferenceValue(userID, itemID);
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
		if (numUsers == 0)
		{
			topRecsByUserID = new FastByIDMap();
			clustersByUserID = new FastByIDMap();
		} else
		{
			List clusters = new LinkedList();
			FastIDSet newCluster;
			for (LongPrimitiveIterator it = model.getUserIDs(); it.hasNext(); clusters.add(newCluster))
			{
				newCluster = new FastIDSet();
				newCluster.add(it.nextLong());
			}

			for (boolean done = false; !done; done = mergeClosestClusters(numUsers, clusters, done));
			topRecsByUserID = computeTopRecsPerUserID(clusters);
			clustersByUserID = computeClustersPerUserID(clusters);
			allClusters = (FastIDSet[])clusters.toArray(new FastIDSet[clusters.size()]);
		}
		clustersBuilt = true;
		buildClustersLock.unlock();
		break MISSING_BLOCK_LABEL_195;
		Exception exception;
		exception;
		buildClustersLock.unlock();
		throw exception;
	}

	private boolean mergeClosestClusters(int numUsers, List clusters, boolean done)
		throws TasteException
	{
		FastIDSet merged;
label0:
		for (LinkedList queue = findClosestClusters(numUsers, clusters); !queue.isEmpty(); clusters.add(merged))
		{
			if (!clusteringByThreshold && clusters.size() <= numClusters)
			{
				done = true;
				break;
			}
			ClusterClusterPair top = (ClusterClusterPair)queue.removeFirst();
			if (clusteringByThreshold && top.getSimilarity() < clusteringThreshold)
			{
				done = true;
				break;
			}
			FastIDSet cluster1 = top.getCluster1();
			FastIDSet cluster2 = top.getCluster2();
			Iterator clusterIterator = clusters.iterator();
			boolean removed1 = false;
			boolean removed2 = false;
			do
			{
				if (!clusterIterator.hasNext() || removed1 && removed2)
					break;
				FastIDSet current = (FastIDSet)clusterIterator.next();
				if (!removed1 && cluster1 == current)
				{
					clusterIterator.remove();
					removed1 = true;
				} else
				if (!removed2 && cluster2 == current)
				{
					clusterIterator.remove();
					removed2 = true;
				}
			} while (true);
			Iterator queueIterator = queue.iterator();
			do
			{
				if (!queueIterator.hasNext())
					break;
				ClusterClusterPair pair = (ClusterClusterPair)queueIterator.next();
				FastIDSet pair1 = pair.getCluster1();
				FastIDSet pair2 = pair.getCluster2();
				if (pair1 == cluster1 || pair1 == cluster2 || pair2 == cluster1 || pair2 == cluster2)
					queueIterator.remove();
			} while (true);
			merged = new FastIDSet(cluster1.size() + cluster2.size());
			merged.addAll(cluster1);
			merged.addAll(cluster2);
			Iterator i$ = clusters.iterator();
			do
			{
				if (!i$.hasNext())
					continue label0;
				FastIDSet cluster = (FastIDSet)i$.next();
				double similarity = clusterSimilarity.getSimilarity(merged, cluster);
				if (similarity <= ((ClusterClusterPair)queue.getLast()).getSimilarity())
					continue;
				ListIterator queueIterator = queue.listIterator();
				do
				{
					if (!queueIterator.hasNext())
						break;
					if (similarity <= ((ClusterClusterPair)queueIterator.next()).getSimilarity())
						continue;
					queueIterator.previous();
					break;
				} while (true);
				queueIterator.add(new ClusterClusterPair(merged, cluster, similarity));
			} while (true);
		}

		return done;
	}

	private LinkedList findClosestClusters(int numUsers, List clusters)
		throws TasteException
	{
		boolean full = false;
		LinkedList queue = new LinkedList();
		int i = 0;
		for (Iterator i$ = clusters.iterator(); i$.hasNext();)
		{
			FastIDSet cluster1 = (FastIDSet)i$.next();
			i++;
			ListIterator it2 = clusters.listIterator(i);
			while (it2.hasNext()) 
			{
				FastIDSet cluster2 = (FastIDSet)it2.next();
				double similarity = clusterSimilarity.getSimilarity(cluster1, cluster2);
				if (!Double.isNaN(similarity) && (!full || similarity > ((ClusterClusterPair)queue.getLast()).getSimilarity()))
				{
					ListIterator queueIterator = queue.listIterator(queue.size());
					do
					{
						if (!queueIterator.hasPrevious())
							break;
						if (similarity > ((ClusterClusterPair)queueIterator.previous()).getSimilarity())
							continue;
						queueIterator.next();
						break;
					} while (true);
					queueIterator.add(new ClusterClusterPair(cluster1, cluster2, similarity));
					if (full)
						queue.removeLast();
					else
					if (queue.size() > numUsers)
					{
						full = true;
						queue.removeLast();
					}
				}
			}
		}

		return queue;
	}

	private FastByIDMap computeTopRecsPerUserID(Iterable clusters)
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
		List topItems = TopItems.getTopItems(0x7fffffff, allItemIDs.iterator(), null, estimator);
		log.debug("Recommendations are: {}", topItems);
		return Collections.unmodifiableList(topItems);
	}

	private static FastByIDMap computeClustersPerUserID(Collection clusters)
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
		return (new StringBuilder()).append("TreeClusteringRecommender2[clusterSimilarity:").append(clusterSimilarity).append(']').toString();
	}


}
