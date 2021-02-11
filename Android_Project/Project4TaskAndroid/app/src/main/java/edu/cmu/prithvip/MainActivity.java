package edu.cmu.prithvip; /* prithvipoddar Prithvip */
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    //Oncreate method Below
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MainActivity hr = this;
        Button charSearch = (Button) findViewById(R.id.charSearch);

        // Add a listener on clicking of search
        charSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                //search tem from edit field
                String searchTerm = ((EditText) findViewById(R.id.editText)).getText().toString();
                if(searchTerm.isEmpty()){
                    searchTerm="adummy";
                }
                MatchUrl matchUrl = new MatchUrl();
                matchUrl.charFind(searchTerm, hr); //Call and initiate async method to find character
            }
        });
    }

    public void mapDetails(JSONObject jsonObject) throws JSONException {

        TextView displayDetails = findViewById(R.id.search1);

        EditText editText = findViewById(R.id.editText);

        if (jsonObject.length()==0) {
            editText.setText("");
            String displayError = "Please enter a valid entry";
            displayDetails.setText(displayError);

        } else {
            try {
                //Map json to string and other data types
                String name = (String) jsonObject.get("name");
                String role = (String) jsonObject.get("role");
                String bloodStatus = (String) jsonObject.get("bloodStatus");
                boolean deathEater = (boolean) jsonObject.get("deathEater");
                String house = (String) jsonObject.get("house");
                boolean dumbledoresArmy = (boolean) jsonObject.get("dumbledoresArmy");
//For final display string
                String finalDisplay = ("Name: " + name + "\n" + "House: " + house + "\n" + "Role: " + role + "\n" +
                        "BloodStatus: " + bloodStatus + "\n" + "IsDeathEater: " + deathEater + "\n" + "Part Of DumbleDore Army: " + dumbledoresArmy);
                displayDetails.setText(finalDisplay);
                displayDetails.setVisibility(View.VISIBLE);
                editText.setText("");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
