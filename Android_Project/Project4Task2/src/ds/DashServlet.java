package ds;
//Prithvi Poddar Prithvip

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "DashServlet", urlPatterns = {"/DashServlet"})
public class DashServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
         *Mongo Client connection established
         *  */
        MongoClientURI uri = new MongoClientURI("mongodb://prithvip:46837achd@dsproject-shard-00-00-d7pee.mongodb.net:27017,dsproject-shard-00-01-d7pee.mongodb.net:27017,dsproject-shard-00-02-d7pee.mongodb.net:27017/test?ssl=true&replicaSet=dsproject-shard-0&authSource=admin&retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("HarryData");
        MongoCollection<Document> docCollection = database.getCollection("WebServiceMetaData");

        //iterator Object
        MongoCursor<Document> iterObj = docCollection.find().iterator();
        Map<String, Integer> wordStore = new HashMap<>();
        Map<Integer, List> tableData = new HashMap<>();
        int count = 0;
        int todayCount = 0;
        long averageLatency = 0;
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String todayDate = simpleDateFormat.format(new Date());
        while (iterObj.hasNext()) {

            Document currentEntry = iterObj.next();
            String search = (String) currentEntry.get("search");
            long startTime = (long) currentEntry.get("startTime");
            long endtime = (long) currentEntry.get("endtime");
            averageLatency = averageLatency + (endtime - startTime);
            String url = (String) currentEntry.get("url");
            long timetaken = (long) currentEntry.get("timetaken");
            String responseStr = (String) currentEntry.get("response");
            Integer responseLength = (Integer) currentEntry.get("responseLength");
            String date = simpleDateFormat.format(new Date(startTime));
            if (date.equalsIgnoreCase(todayDate)) {
                todayCount++;
            }
            if (wordStore.containsKey(search)) {
                int currCount = wordStore.get(search);
                wordStore.put(search, ++currCount);
            } else {
                wordStore.put(search, 1);
            }
//Tabular data list formation based on each entry fetched
            List<String> inputData = Arrays.asList(search, new Timestamp(startTime) + "", new Timestamp(endtime) + "", timetaken + " ms", url, responseStr, Integer.toString(responseLength));
            for (int i = 0; i < inputData.size(); i++) {
                System.out.println(inputData.get(i));
            }
            tableData.put(count, inputData);
            count++;

        }
        averageLatency = averageLatency / count;
        DashModel dashModel = new DashModel();
        String mostFreq = dashModel.maxCount(wordStore);
        //Set attributes
        request.setAttribute("tableData", tableData);
        request.setAttribute("mostFreq", mostFreq);
        request.setAttribute("averageLatency", averageLatency);
        request.setAttribute("todayCount", todayCount);
        request.setAttribute("totalHits", count);
        // Display dashboard
        RequestDispatcher view = request.getRequestDispatcher("Dashboard.jsp");
        view.forward(request, response);
    }
}
