package org.ns.vkcachegrabber.vk.model;

/**
 *
 * @author stupak
 */
public interface Audio extends VKObject {
    
    String getId();
    
    String getOwnerId();
    
    String getArtist();
    
    String getTitle();
    
    /**
     * продолжительность
     * @return 
     */
    long getDuration();

    String getDownloadUrl();
    
    /**
     * id текста песни
     * @return 
     */
    String getLyricsId();

    /**
     * Id жанра
     * @return 
     */
    String getGenreId();
}
