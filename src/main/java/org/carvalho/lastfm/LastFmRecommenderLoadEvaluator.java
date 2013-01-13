package org.carvalho.lastfm;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.eval.LoadEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class LastFmRecommenderLoadEvaluator {

	public static void main(String[] args) throws IOException, TasteException {
		DataModel model = new FileDataModel(
				new File("/Users/rodrigo/Documents/workspace/tutorials/mahout-book/data/lastfm/lastfm-dataset-360K/userid-artmbid-plays.tsv"));
		UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
		UserNeighborhood neighbourhood = new NearestNUserNeighborhood(2, similarity, model);
		Recommender recommender = new GenericUserBasedRecommender(model, neighbourhood, similarity);
		LoadEvaluator.runLoad(recommender);
	}
}
