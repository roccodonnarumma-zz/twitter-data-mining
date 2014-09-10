package com.project.bolt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backtype.storm.generated.StormTopology;
import backtype.storm.task.GeneralTopologyContext;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.TupleImpl;

public class BoltTest {

    private static int taskId = 1;
    private static String componentId = "component";
    private static String streamId = "stream";

    private static StormTopology stormTopology;
    private static GeneralTopologyContext context;

    static {
        TopologyBuilder builder = new TopologyBuilder();
        stormTopology = builder.createTopology();
        Map<Integer, String> taskToComponent = new HashMap<>();
        taskToComponent.put(taskId, componentId);

        Map<String, Fields> streamToFields = new HashMap<>();
        streamToFields.put(streamId, new Fields("field"));

        Map<String, Map<String, Fields>> componentToStreamToFields = new HashMap<>();
        componentToStreamToFields.put(componentId, streamToFields);
        context = new GeneralTopologyContext(stormTopology, null, taskToComponent, null, componentToStreamToFields, null);
    }

    public static Tuple getTuple(List<Object> values) {
        return new TupleImpl(context, values, taskId, streamId);
    }
}
