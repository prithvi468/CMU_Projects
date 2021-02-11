package ds;
//Prithvi Poddar Prithvip

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

@WebServlet(name = "HarryServlet", urlPatterns = {"/HarryServlet"})
public class HarryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("name");
        String urlCalled = "";
//url formation
        NameSearch nameSearch = new NameSearch();
        String apiInputName = nameSearch.nameConversion(search);
        Timestamp inputTime = new Timestamp(System.currentTimeMillis());
//    heroku URLs:
//        https://damp-dusk-68463.herokuapp.com/HarryServlet?name=
//        https://damp-dusk-68463.herokuapp.com/DashServlet
        String harryUrl = "https://www.potterapi.com/v1/characters?name=" + apiInputName + "&key=$2a$10$KBnA3vLlEg7IUkLmo/l.YuPBCKQ8QGvJqnDTMk.ePhL5i76Poi2/a";
        urlCalled = harryUrl;
        String readResponse = nameSearch.fetch(harryUrl);
        Timestamp outputTime = new Timestamp(System.currentTimeMillis());
//json formation
        JSONArray jsonArray = null;
        JSONObject cur = new JSONObject();
        try {
            jsonArray = new JSONArray(readResponse);
            cur = (JSONObject) jsonArray.get(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.println(cur.toString());
        out.flush();
        try {
            /*
             * Mongo connection established
             */

            MongoClientURI uri = new MongoClientURI("mongodb://prithvip:46837achd@dsproject-shard-00-00-d7pee.mongodb.net:27017,dsproject-shard-00-01-d7pee.mongodb.net:27017,dsproject-shard-00-02-d7pee.mongodb.net:27017/test?ssl=true&replicaSet=dsproject-shard-0&authSource=admin&retryWrites=true&w=majority");
            MongoClient mongoClient = new MongoClient(uri);
            MongoDatabase database = mongoClient.getDatabase("HarryData");
            MongoCollection<Document> docCollection = database.getCollection("WebServiceMetaData");

            Document document = new Document("search", search)
                    .append("startTime", inputTime.getTime())
                    .append("endtime", outputTime.getTime())
                    .append("url", urlCalled)
                    .append("timetaken", outputTime.getTime() - inputTime.getTime())
                    .append("response", cur.toString())
                    .append("responseLength", cur.toString().length());
//data entry
            docCollection.insertOne(document);
            mongoClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
