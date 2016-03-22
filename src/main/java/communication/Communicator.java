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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import simulation.Simulator;

/**
 *
 * @author Eric
 */
public class Communicator {

    private static final String TEST_URL = "http://localhost:8080/MovementSystem/webresources/trackers";

    public static Long subscribeTracker(CarTracker tracker) throws IOException, JSONException {
        //Request
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(TEST_URL);
        StringEntity postingString = new StringEntity(gson.toJson(tracker));
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(post);

        //Response
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        JSONObject json = new JSONObject(responseString);
        return json.getLong("id");
    }
}
