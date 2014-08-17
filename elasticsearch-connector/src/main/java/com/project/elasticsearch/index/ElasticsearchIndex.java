package com.project.elasticsearch.index;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.json.JSONArray;
import org.json.JSONObject;

import com.project.elasticsearch.type.Type;

public class ElasticsearchIndex {

    private Client client;

    public ElasticsearchIndex() {
        try {
            Properties properties = new Properties();
            properties.load(ElasticsearchIndex.class.getClassLoader().getResourceAsStream("elasticsearch/elasticsearch.properties"));
            client = new TransportClient().addTransportAddress(
                    new InetSocketTransportAddress(properties.getProperty("elasticsearch.hostname"),
                            Integer.parseInt(properties.getProperty("elasticsearch.port"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void index(Type type, String id, Map<String, Object> map) {
        client.prepareIndex("twitter", type.getName(), id).setSource(map).execute();
    }

    public void index(Type type, String id, String json) {
        client.prepareIndex("twitter", type.getName(), id).setSource(json).execute();
    }

    public JSONObject get(Type type, String id) {
        GetResponse response = client.prepareGet("twitter", type.getName(), id).execute().actionGet();
        String source = response.getSourceAsString();
        if (source == null) {
            return new JSONObject();
        }
        return new JSONObject(source);
    }

    public JSONArray getAll(Type type) {
        JSONArray array = new JSONArray();
        SearchResponse response = client.prepareSearch("twitter").setTypes(type.getName()).setSize(1000).execute().actionGet();
        Iterator<SearchHit> iterator = response.getHits().iterator();
        while (iterator.hasNext()) {
            String source = iterator.next().getSourceAsString();
            if (source != null) {
                array.put(new JSONObject(source));
            }
        }
        return array;
    }

    @Override
    public void finalize() {
        client.close();
    }
}
