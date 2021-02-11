//Prithvip Prithvi Poddar

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BlockChain {
    List<Block> blockList; //list of blocks
    String hashChain; // latest hash value

    //Initialize blockchain contructor
    public BlockChain() {
        blockList = new ArrayList<>();
        hashChain = "";
    }

    //Get current time
    public Timestamp getTime() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp;
    }

    //Get lastest block
    public Block getLatestBlock() {
        return blockList.get(blockList.size() - 1);
    }

    //get chain size
    public int getChainSize() {
        return blockList.size();
    }

    //calculate hashes per second
    public int hashesPerSecond() {

        MessageDigest messageDigest;
        String hashInput = "00000000";
        int hashCount = 0;
        byte[] bytes = new byte[0];
        //for significant bits logic taking last 20
        long startTime = System.currentTimeMillis();
        long stopTime;
        //do this till 1 second complets and increment the hash count
        do {
            try {
                messageDigest = MessageDigest.getInstance("SHA-256");
                bytes = messageDigest.digest(hashInput.getBytes());
            } catch (NoSuchAlgorithmException exception) {
                exception.printStackTrace();
            }
            hashCount++;
            stopTime = System.currentTimeMillis();
        } while (stopTime - startTime < 1000);


        return hashCount;

    }

    //Add new block on pressing 1
    public void addBlock(Block newBlock) {
        newBlock.setPreviousHash(hashChain);
        hashChain = newBlock.proofOfWork();
        blockList.add(newBlock);
    }

    //Converting into accurate JSON format
    public String toString() {
        //using json library wil be adding jar in the zip file
        JSONArray storeJson = new JSONArray();
        for (int i = 0; i < getChainSize(); i++) {
            String blockString = blockList.get(i).toString();
            storeJson.put(blockString);
        }
        JSONObject jObj = new JSONObject();
//        Appending the json
        try {
            jObj.put("ds_chain", storeJson);
            jObj.put("chainHash", hashChain);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObj.toString();
    }

    //Checking the hashchain is valid
    public boolean isChainValid() {
        boolean isValid = true;
        //Iterating over entire chain
        String disp = "";
        for (int i = 0; i < blockList.size(); i++) {
            Block b = blockList.get(i);
            String hashOutput = b.calculateHash();
            int diffCount = 0;
            //Matchong blocks difficulty
            for (int j = 0; j < b.difficulty; j++) {
                if (hashOutput.charAt(j) == '0') {
                    diffCount++;
                }
                disp = disp +"0";
            }
            if (diffCount != b.difficulty) {
                System.out.println("..Improper hash on node"+(i)+"Does not begin with "+disp);
                isValid = false;
                break;
            } else {
                //Checking previous hash conditions
                if (blockList.size() != i + 1) {
                    if (!b.calculateHash().equals(blockList.get(i + 1).getPreviousHash())) {
                        isValid = false;
                    }
                } else {
                    if (!blockList.get(i).calculateHash().equals(hashChain)) {
                        isValid = false;
                    }
                }
            }
        }
        if (isValid == false) {
            return false;
        } else
            return true;
    }

    public void repairChain() {
        //browsing through chain
        for (int i = 0; i < blockList.size(); i++) {
            //if not last block or only block
            if (i != blockList.size() - 1) {
                String getHash = blockList.get(i).calculateHash();
                String nextBlockPrevHash = blockList.get(i + 1).getPreviousHash();
                if (!getHash.equals(nextBlockPrevHash)) {
                    blockList.get(i + 1).setPreviousHash(blockList.get(i).proofOfWork());
                }
            }
            //if last block or only block
            else {
                String getHash = blockList.get(i).calculateHash();
                if (!getHash.equals(hashChain)) {
                    hashChain = blockList.get(i).proofOfWork();
                }
            }
        }
    }

}
