package com.project.bolt;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.twitter.CustomStatus;

public class SentimentBoltTest {

    @Test
    public void sentimentBolt() throws IOException {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = this.getClass().getResourceAsStream("/twitter/status.txt");
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String statusString = reader.readLine();

            ObjectMapper mapper = new ObjectMapper();
            CustomStatus status = mapper.readValue(statusString, CustomStatus.class);
            status.setSentiment(11);

            List<Object> values = new LinkedList<>();
            values.add(status);

            BasicOutputCollector collector = mock(BasicOutputCollector.class);
            Tuple input = BoltTest.getTuple(values);

            SentimentBolt bolt = new SentimentBolt();
            bolt.execute(input, collector);

            ArgumentCaptor<Values> emitValues = ArgumentCaptor.forClass(Values.class);
            verify(collector, atLeastOnce()).emit(emitValues.capture());
            Values capturedValue = emitValues.getValue();
            String resultString = (String)capturedValue.get(0);
            CustomStatus result = mapper.readValue(resultString, CustomStatus.class);
            assertTrue((result.getSentiment() >= -2) && (result.getSentiment() <= 2));
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }
}
