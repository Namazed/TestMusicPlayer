package com.namazed.testmusicplayer.api;

import com.namazed.testmusicplayer.api.models.ListSongs;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface SearchClient {

    /**
     * Search music from itunes api search
     * @param query - your request
     * @param mediaType - music, video and etc
     * @return list of songs
     */
    @GET("search")
    Single<ListSongs> getListSongs(
            @Query("term") String query,
            @Query("media") String mediaType); // only music
}
