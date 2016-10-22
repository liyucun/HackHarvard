package com.hackharvard.smartmusicshuffle;

import android.os.StrictMode;
import android.util.Log;

import com.spotify.sdk.android.player.SpotifyPlayer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by danielchan on 22/10/16.
 */

public class SpottyFi extends MainActivity{


    // URIs for the various playlists. Although it shouldn't be hardcoded.
    private static String relaxing_peacefulPiano = "spotify:user:spotify:playlist:63dDpdoVHvx5RkK87g4LKk";
    private static String embed_peacefulPianoElements = "https://open.spotify.com/embed?uri=spotify%3Auser%3Aspotify%3Aplaylist%3A63dDpdoVHvx5RkK87g4LKk";
    private static String html_peacefulPiano;

    private static String depressed_lifeSucks = "spotify:user:spotify:playlist:5eSMIpsnkXJhXEPyRQCTSc";
    private static String embed_lifeSucksElements = "https://open.spotify.com/embed?uri=spotify%3Auser%3Aspotify%3Aplaylist%3A5eSMIpsnkXJhXEPyRQCTSc";
    private static String html_lifeSucks;


    //Happy playlists
    private static String happy_happyHits = "spotify:user:spotify:playlist:65V6djkcVRyOStLd8nza8E";
    private static String embed_happyHitsElements = "https://open.spotify.com/embed?uri=spotify%3Auser%3Aspotify%3Aplaylist%3A65V6djkcVRyOStLd8nza8E";
    private static String html_happyHits;


    private static String happy_haveAGreatDay = "spotify:user:spotify:playlist:2PXdUld4Ueio2pHcB6sM8j";
    private static String embed_haveAGreatDayElements = "https://open.spotify.com/embed?uri=spotify%3Auser%3Aspotify%3Aplaylist%3A2PXdUld4Ueio2pHcB6sM8j";
    private static String html_haveAGreatDay;


    private static String happy_moodBooster = "spotify:user:spotify:playlist:6uTuhSs7qiEPfCI3QDHXsL";
    private static String embed_moodBoosterElements = "https://embed.spotify.com/?uri=spotify%3Auser%3Aspotify%3Aplaylist%3A6uTuhSs7qiEPfCI3QDHXsL";
    private static String html_moodBooster;

    private static final String extractURI = "data-uri=\"(.*?)\"";

    private static ArrayList<String> happySongURI = new ArrayList<>();
    private static ArrayList<String> depressedURI = new ArrayList<>();
    private static ArrayList<String> relaxingURI = new ArrayList<>();
    private static ArrayList<String> disgustURI = new ArrayList<>();
    private static ArrayList<String> surpriseURI = new ArrayList<>();
    private static ArrayList<String> fearURI = new ArrayList<>();
    private static ArrayList<String> contemptURI = new ArrayList<>();
    private static ArrayList<String> angerURI = new ArrayList<>();

    private static HashMap<Integer, ArrayList> uriMapper = new HashMap<>();


    // I think for neutral we can pick out one random song from each of the other.
    ArrayList<String> neutralURI = new ArrayList<>();




    /*
    0 is relaxing
    1 is happy
    2 is depressed/sad
    3 is disgust
    4 is fear
    5 is contempt
    6 is anger
    7 is surprise
     */
    public static ArrayList<String> selectRandom(int amount, int genre) {

        Random r = new Random();

        ArrayList<String> tempArray = new ArrayList<>();

        absorbURI(genre);

        System.out.println(uriMapper.get(genre).size());

        for(int i = 0; i < amount; i++) {
            int randomInt = r.nextInt(uriMapper.get(genre).size());
            tempArray.add((String) uriMapper.get(genre).get(randomInt));

            //Making sure no duplicates end up in the ArrayList.
            uriMapper.get(genre).remove(randomInt);

        }

        return tempArray;
    }


    public static void absorbURI(int genre) {
        retrieveAllHtml();

        uriMapper.put(0, relaxingURI);
        uriMapper.put(1, happySongURI);
        uriMapper.put(2, depressedURI);
        uriMapper.put(3, disgustURI);
        uriMapper.put(4, fearURI);
        uriMapper.put(5, contemptURI);
        uriMapper.put(6, angerURI);
        uriMapper.put(7, surpriseURI);

        Pattern patternObject = Pattern.compile(extractURI);

        if (genre == 0) {
            Matcher matcher = patternObject.matcher(html_peacefulPiano);
            while (matcher.find()) {
                relaxingURI.add(matcher.group(1));
            }

        } else if (genre == 1) { // Happy
            Matcher matcher = patternObject.matcher(html_happyHits);
            while (matcher.find()) {
                happySongURI.add(matcher.group(1));
            }

            Matcher matcher2 = patternObject.matcher(html_haveAGreatDay);
            while (matcher2.find()) {
                happySongURI.add(matcher2.group(1));
            }

            Matcher matcher3 = patternObject.matcher(html_moodBooster);
            while (matcher3.find()) {
                happySongURI.add(matcher3.group(1));
            }

        } else if (genre == 2) { // Depressed
            Matcher matcher = patternObject.matcher(html_lifeSucks);
            while (matcher.find()) {
                happySongURI.add(matcher.group(1));
            }
        }
    }


    public static void retrieveAllHtml() {
        html_peacefulPiano = getHTML(embed_peacefulPianoElements);
        html_happyHits = getHTML(embed_happyHitsElements);
        html_haveAGreatDay = getHTML(embed_haveAGreatDayElements);
        html_lifeSucks = getHTML(embed_lifeSucksElements);
        html_moodBooster = getHTML(embed_moodBoosterElements);
    }


    /**
     * This method retrieves the raw content of the HTML file, as if it were
     * to be viewed in a plain text editor.
     *
     * @param urlString The name of the HTML file, located in the root
     *                  directory.
     * @return Returns the raw content of the HTML file.
     */
    public static String getHTML(String urlString) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        String html = "";
        StringBuilder str = new StringBuilder();


        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlString);
            HttpResponse response = client.execute(request);

            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            in.close();
        } catch (IOException e) {
            Log.e("Error:", e.getMessage());
        }
        html = str.toString();

        return html;
    }


}
