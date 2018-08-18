package com.example.android.newgames;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Game>> {
    private  ListView gamesListView;
    private  RecyclerView.LayoutManager mLayoutManager;
    private  GameListAdaptor mAdapter;
    Date todayDate = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
    String todayString = formatter.format(todayDate);
    private String expectedYear = todayString;
    private String platform;
    private String USGS_REQUEST_URL = "https://www.giantbomb.com/api/games/?api_key=fe1cf134e1ece916e3c6c12734a13b0c38cba20e&&filter=expected_release_year:"+expectedYear+",platforms:145&format=json";


    /**
     * Constant value for the Game loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWGAMES_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gamesListView = (ListView) findViewById(R.id.my_recycler_view);


        // specify an adapter
        mAdapter = new GameListAdaptor(this, new ArrayList<Game>());
        gamesListView.setAdapter(mAdapter);

        //Network
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        gamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Game currentGame = mAdapter.getItem(position);
                Toast.makeText(getApplicationContext(), currentGame.getDate(), Toast.LENGTH_SHORT).show();
                String givenDateString = currentGame.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if(currentGame.getNoDate()==0) {
                        Date mDate = sdf.parse(givenDateString);
                        long timeInMilliseconds = mDate.getTime();
                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra("title", currentGame.getTitle());
                        intent.putExtra("description", "Some description");
                        intent.putExtra("beginTime", timeInMilliseconds);
                        intent.putExtra(CalendarContract.Events.ALL_DAY, true);
                        startActivity(intent);
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });



        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWGAMES_LOADER_ID, null, this);
        }
        /*else{

            View progress = (View) findViewById(R.id.loadingData);
            progress.setVisibility(View.GONE);
            mEmptyStateTextView.setText("NO INTERNET CONNECTION");
        }*/

    }

    @Override
    public Loader<List<Game>> onCreateLoader(int id, Bundle args) {




        return new GameLoader(this, USGS_REQUEST_URL,expectedYear);
    }



    @Override
    public void onLoadFinished(Loader<List<Game>> loader, List<Game> data) {
        mAdapter.clear();
        Collections.sort(data);
        if (data != null && !data.isEmpty()) {

            mAdapter.addAll(data);
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                mAdapter.clear();
                expectedYear = "2018";
                USGS_REQUEST_URL = "https://www.giantbomb.com/api/games/?api_key=fe1cf134e1ece916e3c6c12734a13b0c38cba20e&&filter=expected_release_year:"+expectedYear+",platforms:145&format=json";
                getLoaderManager().restartLoader(NEWGAMES_LOADER_ID, null, this);
                return true;
            case R.id.item2:
                mAdapter.clear();
                int nextYear = Integer.parseInt(todayString)+1;
                expectedYear = Integer.toString(nextYear);
                USGS_REQUEST_URL = "https://www.giantbomb.com/api/games/?api_key=fe1cf134e1ece916e3c6c12734a13b0c38cba20e&&filter=expected_release_year:"+expectedYear+",platforms:145&format=json";
                getLoaderManager().restartLoader(NEWGAMES_LOADER_ID, null, this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

        @Override
    public void onLoaderReset(Loader<List<Game>> loader) {
        mAdapter.clear();

    }
}

//create a 12 cell array for each month. Check date for each item, add to approiate cell. Then make game item passing
//in requesting you want a header, and what month. Set time for all game items to be above 12am. Set headers to be @12am.
//set title for current year//find PS4 ID//Get sexy header and design done.//Tidy up app


