package edu.cmu.prithvip; /* prithvipoddar Prithvip */


import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * call restful service
 * fill json
 */

public class MatchUrl {

    MainActivity harryPotter = null;
    volatile JSONObject json = null; // JSON object to capture web service response in.

    /**
     * Find character and run async
     *
     * @param search
     * @param harryPotter
     */
    public void charFind(String search, MainActivity harryPotter) {
        this.harryPotter = harryPotter;
        new AsyncCharacterSearch().execute(search.toLowerCase());
    }


    /**
     * async method
     * some char names for app testing
     *Sirius back , Katie bell, Molly Weasley, Ronald weasley or
     * refer to this link to get names if u dont know about harry potter
     * https://www.potterapi.com/v1/characters?house=Gryffindor&key=$2a$10$KBnA3vLlEg7IUkLmo/l.YuPBCKQ8QGvJqnDTMk.ePhL5i76Poi2/a
     */

    private class AsyncCharacterSearch extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            return search(strings[0]);
        }

        protected void onPostExecute(JSONObject jsonObject) {
            try {
                harryPotter.mapDetails(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /*
        Search for harry potetr character
         */
        private JSONObject search(String search) {
            int code = 0;
            try {
                URL url = null; // Depending on event generate url.
                if (search.equalsIgnoreCase("random")) {
                    url = new URL("https://damp-dusk-68463.herokuapp.com/HarryServlet?name=random");
                    code = 1;
                } else {
                    url = new URL("https://damp-dusk-68463.herokuapp.com/HarryServlet?name=" + search);
                    code = 2;
                }
                /*
                Open connection and connect.
                Instantiate buffered reader to read service's output stream.
                Fill up string builder and create a json object from the response.
                 */
                HttpURLConnection searchCon = (HttpURLConnection) url.openConnection();
                searchCon.connect();

                System.out.println(searchCon.getResponseCode());

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (searchCon.getInputStream())));

                String readResponse;
                StringBuilder stringBuilder = new StringBuilder();
                while ((readResponse = br.readLine()) != null) {
                    stringBuilder.append(readResponse);
                }
                br.close();
//populate json
                if (!stringBuilder.toString().isEmpty() && code != 1) {
                    json = new JSONObject(stringBuilder.toString());
                } else {
                    JSONObject newObj = new JSONObject();
                    newObj.put("house", stringBuilder.toString());
                    json = newObj;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return json;
        }
    }


}

