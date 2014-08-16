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

    public static ElasticsearchIndex INSTANCE = new ElasticsearchIndex();

    private Client client;

    private ElasticsearchIndex() {
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

    public void index(Type type, Map<String, Object> map) {
        client.prepareIndex("twitter", type.getName()).setSource(map).execute();
    }

    public JSONObject get(Type type, String id) {
        GetResponse response = client.prepareGet("twitter", type.getName(), id).execute().actionGet();
        return new JSONObject(response.getSourceAsString());
    }

    public JSONArray getAll(Type type) {
        JSONArray array = new JSONArray();
        SearchResponse response = client.prepareSearch("twitter").setTypes(type.getName()).execute().actionGet();
        Iterator<SearchHit> iterator = response.getHits().iterator();
        while (iterator.hasNext()) {
            array.put(new JSONObject(iterator.next().getSourceAsString()));
        }
        return array;
    }

    //    public static void main(String[] args) {
    //        Client client;
    //        try {
    //            Properties properties = new Properties();
    //            properties.load(ElasticsearchIndex.class.getClassLoader().getResourceAsStream("elasticsearch/elasticsearch.properties"));
    //            client = new TransportClient().addTransportAddress(
    //                    new InetSocketTransportAddress(properties.getProperty("elasticsearch.hostname"),
    //                            Integer.parseInt(properties.getProperty("elasticsearch.port"))));
    //        } catch (IOException e) {
    //            throw new RuntimeException(e);
    //        }
    //
    //        SearchResponse response = client.prepareSearch("twitter").setTypes("movie").execute().actionGet();
    //        Iterator<SearchHit> iterator = response.getHits().iterator();
    //        while (iterator.hasNext()) {
    //            System.out.println(new JSONObject(iterator.next().getSourceAsString()));
    //        }
    //    }

    @Override
    public void finalize() {
        client.close();
    }
}
