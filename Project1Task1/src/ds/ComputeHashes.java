package ds;
//PrithviPoddar Prithvip

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ComputeHashes", urlPatterns = {"/ComputeHashes"})
public class ComputeHashes extends HttpServlet {
    ComputeHashesModel computeHashes;

    // setting the object of the model class
    public void init() {
        computeHashes = new ComputeHashesModel();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Get input Text
        String getText = request.getParameter("data");
        //Get hash method from radio
        String hashMethod = request.getParameter("hashChoice");

        //calling the fucntion to encrypt using hash methods
        computeHashes.calculateAlgo(getText, hashMethod);
        //Fetching the values of the computed hash
        String binary64 = computeHashes.binary64;
        String hexaBinary = computeHashes.hexaBinary;
        //setting Attributes to be displayed on the result html
        request.setAttribute("textString", getText);
        request.setAttribute("hashMethod", hashMethod);
        request.setAttribute("bin64", binary64);
        request.setAttribute("hexaBinary", hexaBinary);
        String nextView = "result.jsp";
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        view.forward(request, response);

    }
}
