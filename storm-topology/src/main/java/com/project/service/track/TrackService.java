package com.project.service.track;

import org.json.JSONObject;

import com.project.elasticsearch.index.ElasticsearchIndex;
import com.project.elasticsearch.track.TrackFields;
import com.project.elasticsearch.type.Type;

public class TrackService {

    public static final TrackService INSTANCE = new TrackService();

    private String[] tracks;

    private TrackService() {
        ElasticsearchIndex index = ElasticsearchIndex.INSTANCE;
        JSONObject object = index.get(Type.TRACK, "1");
        if ((object == null) || (object.getString(TrackFields.TRACKS.getName()) == null)) {
            throw new RuntimeException("Could not find tracks");
        }

        tracks = object.getString(TrackFields.TRACKS.getName()).split(",");
    }

    public String[] getTracks() {
        return tracks;
    }
}
