package ds;
//Prithvi Poddar prithvip

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NameSearch {

    //converting input into desirable output string
    public String nameConversion(String inputName) {
        String finalOutput = "";
        StringBuilder sb = new StringBuilder();
        String strArray[] = inputName.split(" ");
        for (int i = 0; i < strArray.length; i++) {
            strArray[i] = strArray[i].substring(0, 1).toUpperCase() + strArray[i].substring(1).toLowerCase();
            sb.append(strArray[i] + " ");
        }
        finalOutput = sb.toString();
        finalOutput = finalOutput.trim();
        finalOutput = finalOutput.replace(" ", "%20");
        return finalOutput;
    }

    public String fetch(String harryUrl) {
        String readResponse = "";
        try {
            URL url = new URL(harryUrl);//Create an HttpURLConnection.  This is useful for setting headers
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("user-key", "6cc2a3a6db7563e46fc9f21859338716");
            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            // Read each line of "in" until done, adding each to "response"
            while ((str = in.readLine()) != null) {
                // str is one line of text readLine() strips newline characters
                readResponse += str;
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Eeek, an exception");
        }
        return readResponse;
    }
}
