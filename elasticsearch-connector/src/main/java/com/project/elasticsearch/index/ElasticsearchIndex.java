package com.project.elasticsearch.index;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.project.elasticsearch.type.Type;

@Service
public class ElasticsearchIndex {

    public static final String INDEX = "twitter";

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
        client.prepareIndex(INDEX, type.getName(), id).setSource(map).execute();
    }

    public void index(Type type, String id, String json) {
        client.prepareIndex(INDEX, type.getName(), id).setSource(json).execute();
    }

    public void bulkRemove(Type type, List<String> ids) {
        if (ids != null) {
            BulkRequestBuilder requestBuilder = client.prepareBulk();
            for (String id : ids) {
                DeleteRequest request = client.prepareDelete(INDEX, type.getName(), id).request();
                requestBuilder.add(request);
            }
            requestBuilder.execute();
        }
    }

    public JSONObject get(Type type, String id) {
        GetResponse response = client.prepareGet(INDEX, type.getName(), id).execute().actionGet();
        String source = response.getSourceAsString();
        if (source == null) {
            return new JSONObject();
        }
        return new JSONObject(source);
    }

    public JSONArray search(Type type, Integer limit, FilterBuilder filter, String sortField, SortOrder sortOrder) {
        JSONArray array = new JSONArray();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEX)
                .setTypes(type.getName());

        if (limit != null) {
            searchRequestBuilder.setSize(limit);
        }

        if ((sortField != null) && (sortOrder != null)) {
            searchRequestBuilder.addSort(sortField, sortOrder);
        }

        if (filter != null) {
            searchRequestBuilder.setPostFilter(filter);
        }

        SearchResponse response = searchRequestBuilder.execute().actionGet();
        Iterator<SearchHit> iterator = response.getHits().iterator();
        while (iterator.hasNext()) {
            String source = iterator.next().getSourceAsString();
            if (source != null) {
                array.put(new JSONObject(source));
            }
        }
        return array;
    }

    public long count(Type type, QueryBuilder query) {
        CountRequestBuilder requestBuilder = client.prepareCount(INDEX).setTypes(type.getName());

        if (query != null) {
            requestBuilder.setQuery(query);
        }
        CountResponse response = requestBuilder.execute().actionGet();
        return response.getCount();
    }
}
