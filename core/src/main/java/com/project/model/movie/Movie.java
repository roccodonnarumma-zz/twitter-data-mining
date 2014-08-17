package com.project.model.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    private String id;
    private String title;

    @JsonProperty("mpaa_rating")
    private String mpaaRating;

    private String synopsis;

    @JsonProperty("release_dates")
    private ReleaseDate releaseDate;

    @JsonProperty("posters")
    private Poster poster;

    @JsonProperty("alternate_ids")
    private AlternateId alternateId;

    private String tracks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public ReleaseDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(ReleaseDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Poster getPoster() {
        return poster;
    }

    public void setPoster(Poster poster) {
        this.poster = poster;
    }

    public AlternateId getAlternateId() {
        return alternateId;
    }

    public void setAlternateId(AlternateId alternateId) {
        this.alternateId = alternateId;
    }

    public String getTracks() {
        return tracks;
    }

    public void setTracks(String tracks) {
        this.tracks = tracks;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReleaseDate {

        private String theater;

        public String getTheater() {
            return theater;
        }

        public void setTheater(String theater) {
            this.theater = theater;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Poster {

        private String original;

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            if (original != null) {
                this.original = original.replace("_tmb", "_ori");
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AlternateId {

        private String imdb;

        public String getImdb() {
            return imdb;
        }

        public void setImdb(String imdb) {
            this.imdb = imdb;
        }
    }
}
