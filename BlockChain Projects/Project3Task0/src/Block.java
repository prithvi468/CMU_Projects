//Prithvip Prithvi Poddar

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;


public class Block {

    //Intial variables
    int index;
    Timestamp timestamp;
    String data, previousHash;
    int difficulty;
    BigInteger nonce;

    //initializing block constructor
    Block(int index, Timestamp timestamp, String data, int difficulty) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.difficulty = difficulty;
        nonce = new BigInteger("0");
    }

    //Calculating hash of the required params specified
    public String calculateHash() {
        MessageDigest messageDigest;
        //as defined by the description
        String hashInput = getIndex() + getTimestamp().toString() + getData() +
                getPreviousHash() + getNonce() + getDifficulty();
        byte[] bytes = new byte[0];
        //calculating hash
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            bytes = messageDigest.digest(hashInput.getBytes());
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
        StringBuilder hexString = new StringBuilder();
        //Code referenced from:
        //https://stackoverflow.com/questions/332079/in-java-how-do-i-convert-a-byte-array-to-a-string-of-hex-digits-while-keeping-l?fbclid=IwAR1LwS7_vTnRTYq6MRUO1F_Em0ymA598kbSZIpXEViUq2LPY6LUeff_f0PE
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    //Return nonce
    public BigInteger getNonce() {
        return nonce;
    }

    //Sets nonce for a block
    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }


    //    The proof of work methods finds a good hash.
//    It increments the nonce until it produces a good hash.
    public String proofOfWork() {
        String hashcalc = calculateHash();
        boolean flag = true;
        while (flag) {
            int diffmatch = 0;
            for (int i = 0; i < difficulty; i++) {
                if (hashcalc.charAt(i) == '0') {
                    diffmatch++;
                }
            }
            if (getDifficulty() == diffmatch) {
                flag = false;
            } else {
                setNonce(getNonce().add(new BigInteger(String.valueOf(1))));
                hashcalc = calculateHash();
            }
        }
        return hashcalc;
    }

    //Get difficulty of the block
    public int getDifficulty() {
        return difficulty;
    }

    //Set difficulty of the block
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    //Forming Json object
    public String toString() {
        JSONObject newObj = new JSONObject();
        try {
            newObj.put("index", getIndex());
            newObj.put("timeStamp", getTimestamp());
            newObj.put("data", getData());
            newObj.put("previousHash", getPreviousHash());
            newObj.put("nonce", getNonce());
            newObj.put("difficulty", getDifficulty());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newObj.toString();
    }

    //check prev hash of the block
    public String getPreviousHash() {
        return previousHash;
    }

    //set prev hash
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    //get block index
    public int getIndex() {
        return index;
    }

    //set block indexx
    public void setIndex(int index) {
        this.index = index;
    }

    //get timestamp
    public Timestamp getTimestamp() {
        return timestamp;
    }

    //set timestamp
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    //get block data
    public String getData() {
        return data;
    }

    //set block data
    public void setData(String data) {
        this.data = data;
    }

}
