package news;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.AveragingPreferenceInferrer;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;

import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasteUserAlgorithm {

	public static void main(String[] args) throws IOException, TasteException,
			SAXException, ParserConfigurationException {
		
		
		String recsFile =StaticConstant.StaticRating;
		System.out.print(recsFile);
		String mov = StaticConstant.BooksXML;
		Integer neighborhoodSize = Integer.parseInt(StaticConstant.NeighborhoodSize);
		
		
		Long userId = Long.parseLong("700");
		boolean printCommonalities = true;
		InputSource is = new InputSource(new FileInputStream(mov));
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		SAXParser sp = factory.newSAXParser();
		ContentHandler handler = new ContentHandler();
		sp.parse(is, handler);
		// create the data model
		FileDataModel dataModel = new FileDataModel(new File(recsFile));
		System.out.println("用户人数: " + dataModel.getNumUsers()
				+ " 文章篇数: " + dataModel.getNumItems());

		UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(
				dataModel);
		// Optional:
		userSimilarity.setPreferenceInferrer(new AveragingPreferenceInferrer(
				dataModel));
		// Get a neighborhood of users
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(
				neighborhoodSize, userSimilarity, dataModel);
		// Create the recommender
		Recommender recommender = new GenericUserBasedRecommender(dataModel,
				neighborhood, userSimilarity);
		System.out.println("-----");
		System.out.println("用户ID: " + userId);
		// Print out the users own preferences first
		TasteUtils.printPreferences(dataModel, userId, handler.map);
		if (printCommonalities) {
			long[] users = neighborhood.getUserNeighborhood(userId);
			for (int i = 0; i < users.length; i++) {
				long neighbor = users[i];
				System.out.println("Neighbor: " + neighbor);
				TasteUtils.printCommonalities(dataModel, userId, neighbor,
						handler.map);
			}

			System.out.println("");
		}
		// Get the top 10 recommendations
		List<RecommendedItem> recommendations = recommender.recommend(userId,
				10);
		TasteUtils.printRecs(recommendations, handler.map);
	}

	public static Map<String, String> getRecommendNews(String userid)
			throws IOException, TasteException, 
			ParserConfigurationException {
		String recsFile =StaticConstant.StaticRating;
		System.out.print(recsFile);
		String mov = StaticConstant.BooksXML;
		Integer neighborhoodSize = Integer.parseInt(StaticConstant.NeighborhoodSize);
		Long userId = Long.parseLong(userid);
		boolean printCommonalities = Boolean.parseBoolean("true");
		InputSource is = new InputSource(new FileInputStream(mov));
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		SAXParser sp = null;
		try {
			sp = factory.newSAXParser();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ContentHandler handler = new ContentHandler();
		try {
			sp.parse(is, handler);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// create the data model
		FileDataModel dataModel = new FileDataModel(new File(recsFile));
		System.out.println("Data Model: Users: " + dataModel.getNumUsers()
				+ " Items: " + dataModel.getNumItems());

		UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(
				dataModel);
		// Optional:
		userSimilarity.setPreferenceInferrer(new AveragingPreferenceInferrer(
				dataModel));
		// Get a neighborhood of users
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(
				neighborhoodSize, userSimilarity, dataModel);
		// Create the recommender
		Recommender recommender = new GenericUserBasedRecommender(dataModel,
				neighborhood, userSimilarity);
		System.out.println("-----");
		System.out.println("User: " + userId);
		// Print out the users own preferences first
		TasteUtils.printPreferences(dataModel, userId, handler.map);
		if (printCommonalities) {
			long[] users = neighborhood.getUserNeighborhood(userId);
			for (int i = 0; i < users.length; i++) {
				long neighbor = users[i];
				System.out.println("Neighbor: " + neighbor);
				TasteUtils.printCommonalities(dataModel, userId, neighbor,
						handler.map);
			}

			System.out.println("");
		}
		// Get the top 10 recommendations
		List<RecommendedItem> recommendations = recommender.recommend(userId,
				Integer.parseInt(StaticConstant.UserRNUM));
		TasteUtils.printRecs(recommendations, handler.map);

		Map<String, String> remap = new HashMap<String, String>();
		for (RecommendedItem item : recommendations) {
			Comparable<?> theItem = item.getItemID();
			String title = handler.map.get(theItem.toString());
			remap.put(theItem.toString(), title);
		}
		System.out.println("第二ddddddddddddddddddddddddddddddddddd");
		return remap;
	}
}
