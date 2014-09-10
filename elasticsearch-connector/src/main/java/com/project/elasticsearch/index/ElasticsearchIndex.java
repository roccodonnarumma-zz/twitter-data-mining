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

/**
 * Service class that provides common methods on the Elasticsearch index.
 * 
 * @author rdonnarumma
 * 
 */
@Service
public class ElasticsearchIndex {

    public static final String INDEX = "twitter";

    private Client client;

    /**
     * Constructs an ElasticsearchIndex by using a TransportClient that connects to an existing cluster. The hostname and port are specified in the properties
     * file elasticsearch/elasticsearch.properties.
     */
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

    /**
     * Constructs an ElasticsearchIndex using the given client.
     * 
     * @param client
     */
    public ElasticsearchIndex(Client client) {
        this.client = client;
    }

    /**
     * Indexes the map as a document with the given id in the given type.
     * 
     * @param type
     * @param id
     * @param map
     */
    public void index(Type type, String id, Map<String, Object> map) {
        client.prepareIndex(INDEX, type.getName(), id).setSource(map).execute();
    }

    /**
     * Indexes the JSON String as a document with the given id in the given type.
     * 
     * @param type
     * @param id
     * @param json
     */
    public void index(Type type, String id, String json) {
        client.prepareIndex(INDEX, type.getName(), id).setSource(json).execute();
    }

    /**
     * Removes all documents that matches the given ids from the given type.
     * 
     * @param type
     * @param ids
     */
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

    /**
     * Returns the document with the given id and type. If not document exists, it returns an empty object.
     * 
     * @param type
     * @param id
     * @return the document with the given id and type. If not document exists, it returns an empty object.
     */
    public JSONObject get(Type type, String id) {
        GetResponse response = client.prepareGet(INDEX, type.getName(), id).execute().actionGet();
        String source = response.getSourceAsString();
        if (source == null) {
            return new JSONObject();
        }
        return new JSONObject(source);
    }

    /**
     * Performs a search on the given type. Returns only limit elements if specified. The filter is applied if is not null. The sortField is applied with the
     * given sortOrder if is not null.
     * 
     * @param type
     * @param limit
     * @param filter
     * @param sortField
     * @param sortOrder
     * @return an array of matching documents.
     */
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

    /**
     * Returns the documents count on the given type for the given query.
     * 
     * @param type
     * @param query
     * @return the documents count on the given type for the given query.
     */
    public long count(Type type, QueryBuilder query) {
        CountRequestBuilder requestBuilder = client.prepareCount(INDEX).setTypes(type.getName());

        if (query != null) {
            requestBuilder.setQuery(query);
        }
        CountResponse response = requestBuilder.execute().actionGet();
        return response.getCount();
    }

    public void closeClient() {
        if (client != null) {
            client.close();
        }
    }
}
