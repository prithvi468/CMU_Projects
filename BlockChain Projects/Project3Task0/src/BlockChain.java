//Prithvip Prithvi Poddar

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        } while (System.currentTimeMillis() - startTime < 1000);


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
        for (int i = 0; i < blockList.size(); i++) {
            Block b = blockList.get(i);
            String hashOutput = b.calculateHash();
            int diffCount = 0;
            String diff ="";
            //Matchong blocks difficulty
            for (int j = 0; j < b.difficulty; j++) {
                if (hashOutput.charAt(j) == '0') {
                    diffCount++;
                }
                diff= diff+"0";
            }
            if (diffCount != b.difficulty) {
                System.out.println("..Improper hash on node "+(i)+" Does not begin with "+diff);
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

    public static void main(String args[]) {
        BlockChain blockChain = new BlockChain();
        Block block1 = new Block(0, blockChain.getTime(), "Genesis", 2);
        blockChain.addBlock(block1);
        block1.setPreviousHash("");
        int inputselection;
        try {
            do {
                System.out.println("**********BLOCKCHAIN MENU**********");
                System.out.println("0. View basic BlockChain status");
                System.out.println("1. Add a transaction to the blockchain");
                System.out.println("2. Verify the blockchain");
                System.out.println("3. View the blockChain");
                System.out.println("4. Corrupt the Chain");
                System.out.println("5. Hide the corruption by repairing the chain");
                System.out.println("6. Exit");
                System.out.println("Select a option");
                Scanner scan = new Scanner(System.in);
                inputselection = scan.nextInt();
                switch (inputselection) {
                    case 0:
                        System.out.println("Current size of chain: " + blockChain.getChainSize());
                        int a = blockChain.hashesPerSecond();
                        System.out.println("Current hashes per second by this machine " + a);
                        System.out.println("Difficulty of most recent block: " + blockChain.getLatestBlock().getDifficulty());
                        System.out.println("Nonce for most recent block: " + blockChain.getLatestBlock().getNonce());
                        System.out.println("Chain hash: " + blockChain.hashChain);
                        break;
                    case 1:
                        System.out.println("Enter difficulty >0 ");
                        int inp = scan.nextInt();
                        System.out.println();
                        System.out.println("Enter transaction input ");
                        Scanner scanner12 = new Scanner(System.in);
                        String inputSel = scanner12.nextLine();
                        int index = blockChain.blockList.size();
                        Block block = new Block(index, blockChain.getTime(), inputSel, inp);
                        long currTime = System.currentTimeMillis();
                        blockChain.addBlock(block);
                        long currTime2 = System.currentTimeMillis();
                        System.out.println("Total execution time to add this block was" + (currTime2 - currTime) + " millisecond");
                        break;

                    case 2:
                        System.out.println("Veriying Entire Chain ");
                        long curr1Time = System.currentTimeMillis();
                        boolean validCheck = blockChain.isChainValid();
                        long curr2Time = System.currentTimeMillis();
                        long timetaken = curr2Time - curr1Time;
                        System.out.println("Chain verification : " + validCheck);
                        System.out.println("Total execution time required to verify the chain was " + timetaken + " milliseconds");
                        break;
                    case 3:
                        System.out.println("View the Blockchain");
                        String fetchJson = blockChain.toString();
                        fetchJson = fetchJson.replaceAll("\\\\", "");
                        System.out.println(fetchJson);
                        break;
                    case 4:
                        System.out.println("Corrupt the Blockchain");
                        System.out.println("Enter block ID of block to Corrupt ");
                        Scanner scan1 = new Scanner(System.in);
                        int x = scan1.nextInt();
                        if (x >= blockChain.getChainSize() || x < 0) {
                            System.out.println("No such block exists start again");
                            System.exit(0);
                        }
                        System.out.println("Enter new data for block " + x);
                        Scanner newScan = new Scanner(System.in);
                        String newInput = newScan.nextLine();
                        blockChain.blockList.get(x).setData(newInput);
                        System.out.println("Block " + x + " now holds " + newInput);
                        break;
                    case 5:
                        System.out.println("Repairing the entire chain");
                        long curr3Time = System.currentTimeMillis();
                        blockChain.repairChain();
                        long curr4Time = System.currentTimeMillis();
                        long timetaken1 = curr4Time - curr3Time;
                        System.out.println("Total execution time required to repair the chain was " + timetaken1 + " milliseconds");
                        break;
                    case 6:
                        break;
                    default:
                        System.out.println("Please enter a vaid selection");
                        break;
                }
            } while (inputselection != 6);
        } catch (Exception e) {
            System.out.println("You entered a garbage input please restart client");
            System.exit(0);
            e.printStackTrace();
        }
    }
}
