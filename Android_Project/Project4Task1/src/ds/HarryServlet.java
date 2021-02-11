package ds;
//Prithvi Poddar ,prithvip
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

@WebServlet(name = "HarryServlet", urlPatterns = {"/HarryServlet"})
public class HarryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("name");
            NameSearch nameSearch = new NameSearch();
            String apiInputName = nameSearch.nameConversion(search);
            //url formation
        //heroku url = https://safe-spire-92054.herokuapp.com/HarryServlet?name=
            String harryUrl = "https://www.potterapi.com/v1/characters?name=" + apiInputName + "&key=$2a$10$KBnA3vLlEg7IUkLmo/l.YuPBCKQ8QGvJqnDTMk.ePhL5i76Poi2/a";
           //fetch response
            String readResponse = nameSearch.fetch(harryUrl);
            JSONArray jsonArray = null;
            JSONObject cur = new JSONObject();
            try {
                jsonArray = new JSONArray(readResponse);
                cur = (JSONObject) jsonArray.get(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(cur.toString());
            PrintWriter out = response.getWriter();
            out.println(cur.toString());
            out.flush();
    }
}
