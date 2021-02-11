package ds; /* prithvipoddar Prithvip */

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ComputeHashesModel {
    String binary64;
    String hexaBinary ;

    public void calculateAlgo(String getText, String hashMethod) {
        MessageDigest messageDigest;
        byte[] messageDigestValue;
        try {
            messageDigest = MessageDigest.getInstance(hashMethod);
            messageDigest.update(getText.getBytes()); //updating string  for conversion
            messageDigestValue = messageDigest.digest();//initiating the digest cycle
            messageDigest.getInstance(hashMethod).digest(getText.getBytes());
            binary64 = DatatypeConverter.printBase64Binary(messageDigestValue);//converted to binary64
            hexaBinary = DatatypeConverter.printHexBinary(messageDigestValue);//converted to hexbinary

        } catch (
                NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
    }
}
