package com.project.batch;

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

import org.apache.hadoop.io.Text;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.project.batch.map.MovieTweetMapper;
import com.project.batch.model.Tweet;

public class MovieTweetMapperTest {

    @Test
    public void map() throws InterruptedException, IOException {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = this.getClass().getResourceAsStream("/tweets/tweets.txt");
            reader = new BufferedReader(new InputStreamReader(inputStream));
            MovieTweetMapper mapper = new MovieTweetMapper();
            MovieTweetMapper.Context context = mock(MovieTweetMapper.Context.class);
            mapper.setup(context);

            List<String> movies = new LinkedList<>();

            String line = reader.readLine();
            while (line != null) {
                Text value = new Text(line);
                ArgumentCaptor<Text> movieId = ArgumentCaptor.forClass(Text.class);
                ArgumentCaptor<Tweet> tweet = ArgumentCaptor.forClass(Tweet.class);
                mapper.map(null, value, context);
                verify(context, atLeastOnce()).write(movieId.capture(), tweet.capture());
                movies.add(movieId.getValue().toString());
                line = reader.readLine();
            }

            int movie1Count = 0;
            int movie2Count = 0;

            for (String movie : movies) {
                if ("movie1".equals(movie)) {
                    movie1Count++;
                } else if ("movie2".equals(movie)) {
                    movie2Count++;
                }
            }

            assertTrue(movie1Count == 4);
            assertTrue(movie2Count == 2);
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
