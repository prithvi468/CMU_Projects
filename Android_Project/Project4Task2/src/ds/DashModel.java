package ds; /* prithvipoddar Prithvip */

import java.util.Map;

public class DashModel {

//Max count of word most searched
    public String maxCount(Map<String, Integer> words) {
        int max = 0;
        String freqWord= "";
        for (String key: words.keySet()) {
            int count = words.get(key);
            if(count > max){
                max=count;
               freqWord = key;
            }
        }

        return freqWord + " and count is " + max;
    }
}
