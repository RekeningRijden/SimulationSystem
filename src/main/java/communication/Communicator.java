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
import java.text.DateFormat;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Eric
 */
public class Communicator {

    /**
     * The test url of the Movementsystem api.
     */
    private static final String BASE_URL_TEST = "http://localhost:8080/MovementSystem/api/trackers";

    /**
     * The production url of the Movementsystem api.
     */
    private static final String BASE_URL_PRODUCTION = "http://movement.s63a.marijn.ws/api/trackers";

//    /**
//     * Adds a new cartracker to the movement api
//     * @param tracker The cartracker new cartracker
//     * @return The newly added cartracker
//     * @throws IOException Can happen when something is wrong with (StringEntity(jsonBody) en httpClient.execute(post)
//     */
//    public static Long subscribeTracker(CarTracker tracker) throws IOException {
//        //Request
//        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//        HttpPost post = new HttpPost(BASE_URL);
//        String jsonBody = gson.toJson(tracker);
//        StringEntity postingString = new StringEntity(jsonBody);
//        post.setEntity(postingString);
//        post.setHeader("Content-type", "application/json");
//        HttpResponse response = httpClient.execute(post);
//
//        //Response
//        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
//        JSONObject json = new JSONObject(responseString);
//        System.out.println("JSON Response: " + json);
//        return json.getLong("id");
//    }

    /**
     * Adds a new trackingPosition to an existing cartracker
     * @param tracker The cartracker with a new trackingPosition
     * @return The serialnumber of the new trackingPosition
     * @throws IOException
     */
    public static Long postTrackingPositionsForTracker(CarTracker tracker) throws IOException {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").excludeFieldsWithoutExposeAnnotation().create();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(BASE_URL_TEST + "/" + tracker.getId() + "/movements");
        String jsonBody = gson.toJson(tracker.getCurrentTrackingPeriod());
        StringEntity postingString = new StringEntity(jsonBody, "UTF-8");
        System.out.println("POSTString: " + jsonBody);
        post.setEntity(postingString);
        post.setHeader(HTTP.CONTENT_TYPE, "application/json");
        HttpResponse response = httpClient.execute(post);
        
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println("ResponseString: " + responseString);
        JSONObject json = new JSONObject(responseString);
        return json.getLong("serialNumber");
    }

    public static List<CarTracker> getAllCartrackers() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(BASE_URL_TEST);
        HttpResponse response = httpClient.execute(get);

        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        Gson gson = new Gson();
        return gson.fromJson(responseString, new TypeToken<List<CarTracker>>(){}.getType());
    }
}
