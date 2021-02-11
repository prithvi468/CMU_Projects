//Prithvip Prithvi Poddar

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;


public class EchoClientTCP {

    static BigInteger n; // n is the modulus for both the private and public keys
    static BigInteger e; // e is the exponent of the public key
    static BigInteger d; // d is the exponent of the private key


    public static void main(String args[]) {
        BlockChain blockChain = new BlockChain();
        EchoClientTCP echoClientTCP = new EchoClientTCP();
        echoClientTCP.generateRsaKeys();
        String signature;
        int inputselection;
        String pubKey = generateRsaKeys();
        //Displaying Public private key and user ID
        System.out.println("Public key is " + pubKey);
        System.out.println("Private key is " + d.toString() + n.toString());
        String userId = calculateAlgo(pubKey);
        System.out.println("User ID is " + userId);
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
                JSONObject newObj = new JSONObject();
                JsonObject fetchVar;
                switch (inputselection) {
                    case 0:
                        //Creating JSON Object
                        newObj = new JSONObject();
                        try {
                            newObj.put("userid", userId + "");
                            newObj.put("input", inputselection + "");
                            newObj.put("e", e + "");
                            newObj.put("n", n + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        fetchVar = view(newObj);
                        //fetching data from function that returns reply from server
                        System.out.println("Current size of chain: " + fetchVar.get("chainsize"));
                        System.out.println("Current hashes per second by this machine " + fetchVar.get("hashpersecond"));
                        System.out.println("Difficulty of most recent block: " + fetchVar.get("difficulty"));
                        System.out.println("Nonce for most recent block: " + fetchVar.get("nonce"));
                        System.out.println("Chain hash: " + fetchVar.get("chainHash"));
                        break;
                    case 1:
                        System.out.println("Enter difficulty >0");
                        int inp = scan.nextInt();
                        System.out.println("Enter transaction input ");
                        Scanner scanner12 = new Scanner(System.in);
                        String inputSel = scanner12.nextLine();
                        JSONObject newObj1 = new JSONObject();
                        //Creating JSON Object
                        try {
                            newObj1.put("userid", userId + "");
                            newObj1.put("input", inputselection + "");
                            newObj1.put("e", e + "");
                            newObj1.put("n", n + "");
                            newObj1.put("difficulty", inp + "");
                            newObj1.put("transaction", inputSel + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        fetchVar = view(newObj1);
                        //fetching data from function that returns reply from server
                        System.out.println("Total execution time to add this block was" + fetchVar.get("timediff1").toString() + " millisecond");
                        break;

                    case 2:
                        newObj = new JSONObject();
                        //Creating JSON Object
                        try {
                            newObj.put("userid", userId + "");
                            newObj.put("input", inputselection + "");
                            newObj.put("e", e + "");
                            newObj.put("n", n + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Veriying Entire Chain");
                        fetchVar = view(newObj);
                        //fetching data from function that returns reply from server
                        System.out.println("Chain verification : " + fetchVar.get("validity").toString());
                        System.out.println("Total execution time required to verify the chain was " + fetchVar.get("timediff2").toString() + " milliseconds");
                        break;
                    case 3:
                        //Creating JSON Object
                        newObj = new JSONObject();
                        try {
                            newObj.put("userid", userId + "");
                            newObj.put("input", inputselection + "");
                            newObj.put("e", e + "");
                            newObj.put("n", n + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("View the Blockchain");
                        fetchVar = view(newObj);
                        //fetching data from function that returns reply from server
                        System.out.println(fetchVar.get("blockchainjson").toString().replaceAll("\\\\", ""));
                        break;
                    case 4:
                        //Creating JSON Object
                        System.out.println("Corrupt the Blockchain");
                        System.out.println("Enter block ID of block to Corrupt ");
                        Scanner scan1 = new Scanner(System.in);
                        int corruptId = scan1.nextInt();
                        System.out.println("Enter new data for block " + corruptId);
                        Scanner newScan = new Scanner(System.in);
                        String newInput = newScan.nextLine();
                        newObj = new JSONObject();
                        try {
                            newObj.put("userid", userId + "");
                            newObj.put("input", inputselection + "");
                            newObj.put("e", e + "");
                            newObj.put("n", n + "");
                            newObj.put("corruptId", corruptId + "");
                            newObj.put("newInput", newInput + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        fetchVar = view(newObj);
                        //fetching data from function that returns reply from server
                        System.out.println("Block " + corruptId + " now holds " + newInput);
                        System.out.println();
                        break;
                    case 5:
                        //Creating JSON Object
                        newObj = new JSONObject();
                        try {
                            newObj.put("userid", userId + "");
                            newObj.put("input", inputselection + "");
                            newObj.put("e", e + "");
                            newObj.put("n", n + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Repairing the entire chain");
                        fetchVar = view(newObj);
                        //fetching data from function that returns reply from server
                        System.out.println("Total execution time required to repair the chain was " + fetchVar.get("timetaken1").toString() + " milliseconds");
                        break;
                    case 6:
                        //if this option exit
                        System.exit(0);
                        break;
                }
            } while (inputselection != 6);
        } catch (Exception ex) {
            System.out.println("You entered a garbage input please restart client");
            System.exit(0);
            ex.printStackTrace();
        }
    }


    //return public key from here
    public static String generateRsaKeys() {
        // Each public and private key consists of an exponent and a modulus
        Random rnd = new Random();
        BigInteger p = new BigInteger(400, 100, rnd);
        BigInteger q = new BigInteger(400, 100, rnd);
        // Step 2: Compute n by the equation n = p * q.
        n = p.multiply(q);
        // Step 3: Compute phi(n) = (p-1) * (q-1)
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        // Step 4: Select a small odd integer e that is relatively prime to phi(n).
        e = new BigInteger("65537");
        // Step 5: Compute d as the multiplicative inverse of e modulo phi(n).
        d = e.modInverse(phi);
        // Step 6: (e,n) is the RSA public key// Step 7: (d,n) is the RSA private key// Modulus for both keys
        // Generating and Printing the public key and private key
        String pubKey = e.toString() + n.toString();

        String privKey = d.toString() + n.toString();

        return pubKey;
    }

    //Hashing and returing string
    public static String calculateAlgo(String getText) {
        MessageDigest messageDigest;
        byte[] finalbytes = new byte[20];
        try {
            //for significant bits logic taking last 20
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
//        https://stackoverflow.com/questions/332079/in-java-how-do-i-convert-a-byte-array-to-a-string-of-hex-digits-while-keeping-l?
//        fbclid=IwAR1LwS7_vTnRTYq6MRUO1F_Em0ymA598kbSZIpXEViUq2LPY6LUeff_f0PE
        for (int i = 0; i < finalbytes.length; i++) {
            String hex = Integer.toHexString(0xFF & finalbytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    //This method generates a signature from the text provided client id, e, n, operation method,value passed
    public static byte[] calculateSignature(String getText) {
        MessageDigest messageDigest;
        byte[] bytes = new byte[0];
        byte[] finalBytes;
        //Hashing the signature using  SHA 256
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

    //This function sends Json to server and recieves response
    public static JsonObject view(JSONObject jsonObj) {
        Socket clientSocket = null;
        Gson reply = null;
        //initalizing gson
        Gson gson = new Gson();
        JsonObject reply1 = null;
        try {
            int serverPort = 7777;
            clientSocket = new Socket("localhost", serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
            //computing private key
            String sigObj = jsonObj.toString();
            byte[] hashmsg = calculateSignature(sigObj);
            BigInteger m = new BigInteger(hashmsg);
            BigInteger c = m.modPow(d, n);
            //Signing and appending to the final string to be sent
            String signature = c.toString();
            jsonObj.put("signature", signature);
            //Sending data to server
            out.println(jsonObj.toString());
            out.flush();
            //Recieving From server
            reply1 = gson.fromJson(in.readLine(), JsonObject.class);


        } catch (IOException | JSONException e) {
            System.out.println("IO Exception:" + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                // ignore exception on close
            }
        }

        return reply1;
    }


}