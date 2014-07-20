package com.project.bolt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class HdfsBolt extends BaseBasicBolt {
    private static final Logger LOG = LoggerFactory.getLogger(HdfsBolt.class);

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        LOG.debug(input.toString());
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

}
