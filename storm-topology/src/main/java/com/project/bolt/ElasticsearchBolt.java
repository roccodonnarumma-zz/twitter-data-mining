package com.project.bolt;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.project.elasticsearch.field.TweetField;
import com.project.elasticsearch.index.ElasticsearchIndex;
import com.project.elasticsearch.type.Type;
import com.project.model.sentiment.Sentiment;
import com.project.model.twitter.CustomStatus;
import com.project.service.track.TrackService;

public class ElasticsearchBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 3361080905463165886L;

    private ElasticsearchIndex index;
    private String[] tracks;

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        if (index == null) {
            index = ElasticsearchIndex.INSTANCE;
        }
        for (Object obj : input.getValues()) {
            if (obj instanceof Sentiment) {
                Sentiment sentiment = (Sentiment)obj;
                CustomStatus status = sentiment.getStatus();
                Map<String, Object> map = new HashMap<>();
                map.put(TweetField.MOVIE_ID.getName(), status.getMovieId());
                map.put(TweetField.MOVIE_NAME.getName(), getMovieName(status.getText()));
                map.put(TweetField.SENTIMENT.getName(), sentiment.getSentiment());
                map.put(TweetField.TEXT.getName(), status.getText());
                map.put(TweetField.CREATED_AT.getName(), status.getCreatedAt());
                map.put(TweetField.GEO_LOCATION.getName(), status.getGeoLocation());
                index.index(Type.TWEET, map);
            }
        }
    }

    private String getMovieName(String text) {
        if (tracks == null) {
            tracks = TrackService.INSTANCE.getTracks();
        }

        String textLower = text.toLowerCase();
        for (String track : tracks) {
            if (textLower.contains(track.toLowerCase())) {
                return track;
            }
        }

        return null;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub
    }

}
