package org.ns.vk.cachegrabber.api.vk;

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
    int getDuration();

    String getDownloadUrl();
    
    /**
     * id текста песни
     * @return 
     */
    String getLirycsId();

    /**
     * Id жанра
     * @return 
     */
    String getGenreId();
}
