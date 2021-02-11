//Prithvip Prithvi Poddar

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EchoServerTCP {
    static BigInteger n; // n is the modulus for both the private and public keys
    static BigInteger e; // e is the exponent of the public key

    public static void main(String args[]) {
        System.out.println("Running");
        BlockChain blockChain = new BlockChain();
        Block block1 = new Block(0, blockChain.getTime(), "Genesis", 2);
        blockChain.addBlock(block1);
        block1.setPreviousHash("");

        Socket clientSocket = null;
        // Initializing Hash Map
        Map<String, Integer> dataMap = new HashMap<>();
        try {
            int serverPort = 7777; // the server port we are using

            // Create a new server socket
            ServerSocket listenSocket = new ServerSocket(serverPort);

            while (true) {
                clientSocket = listenSocket.accept();
                Scanner in;
                in = new Scanner(clientSocket.getInputStream());
                String response = in.nextLine();
                Gson gson = new Gson();
                HashMap<String, String> clientInput = gson.fromJson(response, HashMap.class);
                JsonObject objRecved = gson.fromJson(response, JsonObject.class);
                objRecved.remove("signature");
                //Splitting the fetched string to get client id, e, n, operation method,value passed and signature
                String clientUserID = (String) clientInput.get("userid");
                String pubKey = clientInput.get("e").toString() + clientInput.get("n").toString();
                String servUserId = calculateAlgo(pubKey);
                String msgFetch = null;
                String selectMethod = (String) clientInput.get("input");
                int selection = Integer.parseInt(selectMethod);
                msgFetch = objRecved.toString();
                e = new BigInteger((String) clientInput.get("e"));
                n = new BigInteger((String) clientInput.get("n"));
                byte hashmsg[] = calculateSignature(msgFetch);//calculating signature from msg recieved
                BigInteger sigCheck = new BigInteger(hashmsg);//sig formed
                BigInteger signature = new BigInteger((String) clientInput.get("signature"));
                BigInteger decryptedmsg = signature.modPow(e, n);//decrypting signature passed for matching and establishing trust
                String finString = "";
                //verifying Clientt's user id if authentication && message quality by verifying signature
                JSONObject serverReply = new JSONObject();
                // Set up "out" to write to the client socket
                PrintWriter out;
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

                if (decryptedmsg.equals(sigCheck) && clientUserID.equals(servUserId)) {
                    //If user selected addition
                    switch (selection) {
                        case 0:
                            serverReply.put("hashpersecond", blockChain.hashesPerSecond());
                            serverReply.put("chainsize", blockChain.getChainSize());
                            serverReply.put("nonce", blockChain.getLatestBlock().getNonce());
                            serverReply.put("chainHash", blockChain.hashChain);
                            serverReply.put("difficulty", blockChain.getLatestBlock().getDifficulty());
                            out.println(serverReply.toString());
                            out.flush();
                            break;
                        case 1:
                            String trans = clientInput.get("transaction");
                            int index = blockChain.blockList.size();
                            String difficulty = clientInput.get("difficulty");
                            System.out.println(difficulty);
                            Block block = new Block(index, blockChain.getTime(), trans, Integer.parseInt(difficulty));
                            long currTime = System.currentTimeMillis();
                            blockChain.addBlock(block);
                            long currTime2 = System.currentTimeMillis();
                            serverReply.put("timediff1", currTime2 - currTime);
                            out.println(serverReply.toString());
                            out.flush();
                            break;
                        case 2:
                            long curr1Time = System.currentTimeMillis();
                            boolean validCheck = blockChain.isChainValid();
                            long curr2Time = System.currentTimeMillis();
                            long timetaken = curr2Time - curr1Time;
                            serverReply.put("timediff2", timetaken);
                            serverReply.put("validity", validCheck);
                            out.println(serverReply.toString());
                            out.flush();
                            break;
                        case 3:
                            String fetchJson = blockChain.toString();
                            fetchJson = fetchJson.replaceAll("\\\\", "");
                            serverReply.put("blockchainjson", fetchJson);
                            out.println(serverReply.toString());
                            out.flush();
                            break;
                        case 4:
                            String corId = clientInput.get("corruptId");
                            String newInp = clientInput.get("newInput");
                            blockChain.blockList.get(Integer.parseInt(corId)).setData(newInp);
                            String dummy = "dummy";
                            serverReply.put("dummy", dummy);
                            out.println(serverReply.toString());
                            out.flush();
                            break;
                        case 5:
                            long curr3Time = System.currentTimeMillis();
                            blockChain.repairChain();
                            long curr4Time = System.currentTimeMillis();
                            long timetaken1 = curr4Time - curr3Time;
                            serverReply.put("timetaken1", timetaken1);
                            out.println(serverReply.toString());
                            out.flush();
                            break;
                    }

                }


            }

            // Handle exceptions
        } catch (IOException | JSONException e) {
            System.out.println("Garbage value Entered ");
            System.out.println("IO Exception:" + e.getMessage());

            // If quitting (typically by you sending quit signal) clean up sockets
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                // ignore exception on close
            }
        }
    }

    //This method generates a signature from the text provided client id, e, n, operation method,value passed
    public static byte[] calculateSignature(String getText) {
        MessageDigest messageDigest;
        byte[] bytes = new byte[0];
        byte[] finalBytes;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            bytes = messageDigest.digest(getText.getBytes());

        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
        finalBytes = new byte[bytes.length + 1];
        finalBytes[0] = 0;

        for (int i = 1; i < finalBytes.length; i++) {
            finalBytes[i] = bytes[i - 1];
        }
        return finalBytes;
    }

    //Hashing and returing string
    public static String calculateAlgo(String getText) {
        MessageDigest messageDigest;
        byte[] finalbytes = new byte[20];
        //for significant bits logic taking last 20
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = messageDigest.digest(getText.getBytes());
            int starter = bytes.length - 20;
            int j = 0;
            for (int i = starter; i < bytes.length; i++) {
                finalbytes[j] = bytes[i];
                j++;
            }
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
        StringBuilder hexString = new StringBuilder();
        //Code referenced from:
        //https://stackoverflow.com/questions/332079/in-java-how-do-i-convert-a-byte-array-to-a-string-of-hex-digits-while-keeping-l?fbclid=IwAR1LwS7_vTnRTYq6MRUO1F_Em0ymA598kbSZIpXEViUq2LPY6LUeff_f0PE
        for (int i = 0; i < finalbytes.length; i++) {
            String hex = Integer.toHexString(0xFF & finalbytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
          return hexString.toString();
    }
}
