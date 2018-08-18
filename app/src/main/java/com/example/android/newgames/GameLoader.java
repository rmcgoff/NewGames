package com.example.android.newgames;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.text.ParseException;
import java.util.List;

/**
 * Created by rymcg on 21/04/2018.
 */

public class GameLoader extends AsyncTaskLoader<List<Game>> {

    /** Tag for log messages */
    private static final String LOG_TAG = GameLoader.class.getName();

    /** Query URL */
    private String mUrl;
    String mexpectedYear;

    public GameLoader(Context context, String url,String expectedYear) {
        super(context);
        mUrl = url;
        mexpectedYear = expectedYear;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Game> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of games.
        List<Game> gameList = null;
        try {
            gameList = QueryUtils.fetchGameData(mUrl,mexpectedYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gameList;
    }
}