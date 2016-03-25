/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.CarTracker;
import java.io.IOException;
import java.text.DateFormat;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Eric
 */
public class Communicator {

    private static final String BASE_URL = "http://localhost:8080/MovementSystem/webresources/trackers";

    public static Long subscribeTracker(CarTracker tracker) throws IOException, JSONException {
        //Request
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(BASE_URL);
        String jsonBody = gson.toJson(tracker);
        StringEntity postingString = new StringEntity(jsonBody);
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(post);

        //Response
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        JSONObject json = new JSONObject(responseString);
        System.out.println("JSON Response: " + json);
        return json.getLong("id");
    }

    public static Long postTrackingPositionsForTracker(CarTracker tracker) throws IOException, JSONException {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").excludeFieldsWithoutExposeAnnotation().create();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(BASE_URL + "/" + tracker.getId() + "/movements");
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

    //TODO - Mehtod for the duplicated code e.g. doRequest
}
