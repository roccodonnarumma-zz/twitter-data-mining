package com.project.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.project.model.sentiment.Sentiment;
import com.project.model.twitter.CustomStatus;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class SentimentBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 3201910429837431413L;

    private static StanfordCoreNLP pipeline = new StanfordCoreNLP("nlp/nlp.properties");

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        for (Object obj : input.getValues()) {
            if (obj instanceof CustomStatus) {
                CustomStatus status = (CustomStatus)obj;
                int mainSentiment = 0;
                if ((status.getText() != null) && (status.getText().length() > 0)) {
                    int longest = 0;
                    Annotation annotation = pipeline.process(cleanText(status));
                    for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                        Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
                        int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                        String partText = sentence.toString();
                        if (partText.length() > longest) {
                            mainSentiment = sentiment;
                            longest = partText.length();
                        }
                    }
                }
                collector.emit(new Values(new Sentiment(status, mainSentiment)));
            }
        }
    }

    private String cleanText(CustomStatus status) {
        String text = status.getText();

        if (text.startsWith("RT")) {
            text = text.substring(text.indexOf(":"), text.length());
        }

        return text;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("sentiment"));
    }

}
