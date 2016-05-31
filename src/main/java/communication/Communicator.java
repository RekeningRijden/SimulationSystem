/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import domain.CarTracker;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * This class handles the communication to and from the Movementsystem Api
 *
 * @author Eric
 */
public class Communicator {

    /**
     * The production url of the Movementsystem api.
     */
    private static final String BASE_URL_PRODUCTION = "http://movement.s63a.marijn.ws/api/trackers";
    //private static final String BASE_URL_PRODUCTION = "http://localhost:8080/MovementSystem/api/trackers";

    private static final String CHARACTER_SET = "UTF-8";

    private Communicator() {
        // empty constructor
    }

    /**
     * Adds a new trackingPosition to an existing cartracker
     *
     * @param tracker The cartracker with a new trackingPosition
     * @return The serialnumber of the new trackingPosition
     * @throws IOException
     */
    public static Long postTrackingPositionsForTracker(CarTracker tracker) throws IOException {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(BASE_URL_PRODUCTION + "/" + tracker.getId() + "/movements");
        String jsonBody = gson.toJson(tracker.getCurrentTrackingPeriod());
        StringEntity postingString = new StringEntity(jsonBody, CHARACTER_SET);
        post.setEntity(postingString);
        post.setHeader(HTTP.CONTENT_TYPE, "application/json");
        HttpResponse response = httpClient.execute(post);

        String responseString = EntityUtils.toString(response.getEntity(), CHARACTER_SET);
        JSONObject json = new JSONObject(responseString);
        return json.getLong("serialNumber");
    }

    /**
     * Gets all the cartrackers from the Movementsystem api
     *
     * @return A list with all the cartrackers from the Movementsystem api
     * @throws IOException Could be thrown when executing the http request, or
     * when converting the result to a String
     */
    public static List<CarTracker> getAllCartrackers() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(BASE_URL_PRODUCTION);
        HttpResponse response = httpClient.execute(get);

        String responseString = EntityUtils.toString(response.getEntity(), CHARACTER_SET);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gson.fromJson(responseString, new TypeToken<List<CarTracker>>() {
        }.getType());
    }
}
