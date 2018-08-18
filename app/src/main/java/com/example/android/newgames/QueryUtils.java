package com.example.android.newgames;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class QueryUtils {
    private String yearOfData;
    private static String mexpectedYear;
    private QueryUtils() {

    }

    public static List<Game> fetchGameData(String requestURl, String expectedYear) throws ParseException {
        URL url = creatURL(requestURl);
        String jsonResponse = "";
        mexpectedYear = expectedYear;

        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            //print log maybe?
        }
        //Extracts data from URL and puts everything into earthquake array
        List<Game> gamesList = extractGames(jsonResponse);
        return gamesList;
    }

    private static URL creatURL (String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        }catch (MalformedURLException e) {
            e.printStackTrace();

        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws  IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(100000);
            urlConnection.setConnectTimeout(150000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }




    public static List<Game> extractGames(String earthQuakeJSON) throws ParseException {

        if (TextUtils.isEmpty(earthQuakeJSON)) {
            return null;
        }


        // Create an empty ArrayList that we can start adding games to

        List<Game> gamesList = new ArrayList<>();
        int [] monthCount = new int [13];





        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON

        // is formatted, a JSONException exception object will be thrown.

        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            //gets the root of the JSON file
            JSONObject root = new JSONObject(earthQuakeJSON);
            JSONArray resultsArray = root.getJSONArray("results");

            for(int i = 0; i<resultsArray.length(); i++) {
                JSONObject currentGame = resultsArray.getJSONObject(i);
                String name = currentGame.getString("name");
                String date = currentGame.getString("original_release_date");
                //get Expected date
                String day = currentGame.getString("expected_release_day");
                String month = currentGame.getString("expected_release_month");
                String year = currentGame.getString("expected_release_year");



                if(!month.equals("null")){
                    monthCount[Integer.parseInt(month)]++;
                }
                else if(!date.equals("null")){


                    String monthOfRelease = date.substring(5, date.length() - 12);
                    monthOfRelease = monthOfRelease.replaceFirst("0","");
                    //Log.e("MonthofRelease", monthOfRelease);

                    monthCount[Integer.parseInt(monthOfRelease)]++;
                }
                else{
                    //Log.e("2018", "2018");

                    monthCount[0]++;
                }


                gamesList.add(new Game(name, date,day,month,year,mexpectedYear));

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);

        }
        // Return the list of games
        for(int x = 0; x< monthCount.length;x++){
            if(monthCount[x]>0){
                String monthString="";
                String dateString="";
                switch (x) {
                    case 1:  monthString = "January"; dateString= mexpectedYear +"-01-01"; gamesList.add(new Game(monthString,dateString));       break;
                    case 2:  monthString = "February"; dateString =mexpectedYear +"-01-31"; gamesList.add(new Game(monthString,dateString));     break;
                    case 3:  monthString = "March"; dateString =mexpectedYear +"-02-27";gamesList.add(new Game(monthString,dateString));         break;
                    case 4:  monthString = "April"; dateString =mexpectedYear +"-03-31";gamesList.add(new Game(monthString,dateString));         break;
                    case 5:  monthString = "May";  dateString =mexpectedYear +"-04-30";gamesList.add(new Game(monthString,dateString));          break;
                    case 6:  monthString = "June";  dateString =mexpectedYear +"-05-31";gamesList.add(new Game(monthString,dateString));         break;
                    case 7:  monthString = "July";  dateString =mexpectedYear +"-06-30";gamesList.add(new Game(monthString,dateString));         break;
                    case 8:  monthString = "August"; dateString =mexpectedYear +"-07-31";gamesList.add(new Game(monthString,dateString));      break;
                    case 9:  monthString = "September"; dateString =mexpectedYear +"-08-31";gamesList.add(new Game(monthString,dateString));     break;
                    case 10: monthString = "October"; dateString =mexpectedYear +"-09-30";gamesList.add(new Game(monthString,dateString));       break;
                    case 11: monthString = "November"; dateString =mexpectedYear +"-10-31";gamesList.add(new Game(monthString,dateString));      break;
                    case 12: monthString = "December"; dateString =mexpectedYear +"-11-30";gamesList.add(new Game(monthString,dateString));      break;
                    default: monthString = "Rest of 2018"; dateString =mexpectedYear +"-13-01"; gamesList.add(new Game(monthString,dateString)); break;

                }

            }

        }




        return gamesList;

    }
}

