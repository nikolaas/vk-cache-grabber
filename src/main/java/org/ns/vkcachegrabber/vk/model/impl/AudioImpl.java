package org.ns.vkcachegrabber.vk.model.impl;

import java.util.Objects;
import org.ns.vkcachegrabber.vk.model.Audio;

/**
 *
 * @author stupak
 */
public class AudioImpl implements Audio {

    private String id;
    private String ownerId;
    private String artist;
    private String title;
    private int duration;
    private String downloadUrl;
    private String lyricsId;
    private String genreId;
    
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String getLirycsId() {
        return lyricsId;
    }

    public void setLyricsId(String lyricsId) {
        this.lyricsId = lyricsId;
    }

    @Override
    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AudioImpl other = (AudioImpl) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AudioImpl{" + 
                  "id=" + id + 
                ", ownerId=" + ownerId + 
                ", artist=" + artist + 
                ", title=" + title + 
                ", duration=" + duration + 
                ", downloadUrl=" + downloadUrl + 
                ", lyricsId=" + lyricsId + 
                ", genreId=" + genreId + 
        '}';
    }
    
}
