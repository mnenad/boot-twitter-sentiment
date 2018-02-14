package io.pivotal.twitter.feeder.service;

import org.springframework.beans.factory.annotation.Value;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
 
public class NLP {
    static StanfordCoreNLP pipeline;
	static String language;
  
	@Value("${twitter_language:de}")
    public void setLanguage(String str) {
        language = str;
    }

    public static void init() {
//        pipeline = new StanfordCoreNLP("StanfordCoreNLP-german.properties");
    		if(language.startsWith("en") == true)
            pipeline = new StanfordCoreNLP("english.properties");
    		else
            pipeline = new StanfordCoreNLP("german.properties");
    			
    }
  
    public static int findSentiment(String tweet) {
 
        int mainSentiment = 0;
        if (tweet != null && tweet.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(tweet);
            for (CoreMap sentence : annotation
                    .get(CoreAnnotations.SentencesAnnotation.class)) {
 //               Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
 
            }
        }
        return mainSentiment;
    }
}