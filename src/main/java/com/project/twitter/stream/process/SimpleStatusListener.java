package com.project.twitter.stream.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

@Component
public class SimpleStatusListener implements StatusListener {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleStatusListener.class);

    @Override
    public void onStatus(Status status) {
        System.out.println(status.getText());
    }

    @Override
    public void onException(Exception ex) {
        LOG.error(ex.getMessage(), ex);
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        LOG.info("Status Deletion Notice {} for user {}", statusDeletionNotice.getStatusId(), statusDeletionNotice.getUserId());
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        LOG.warn("Track Limitation Notice, number of limited statuses = {}", numberOfLimitedStatuses);
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        LOG.info("Scrub Geo, userId = {}, statusId = {}", userId, upToStatusId);
    }

    @Override
    public void onStallWarning(StallWarning warning) {
        LOG.warn("Stall Warning code = {}, message = {}", warning.getCode(), warning.getMessage());
    }

}
