import java.util.*;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
 
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class UserWriter{
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    private static final String userDelim = "_USER_";

    public void writeToDisk(String file, String key, HashMap<String,User> users) throws Exception{
        try{
            int cipherMode = Cipher.ENCRYPT_MODE;
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);
                
            byte[] outputBytes = cipher.doFinal(userMapToBytes(users));
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(outputBytes);
        } catch (Exception e){
            throw new Exception("Failed to write file");
        }
    }

    private byte[] userMapToBytes(HashMap<String,User> users){
        String byteStr = "";
        for(Map.Entry<String,User> userEntry : users.entrySet()){
            byteStr += userEntry.getValue().getUserId() + ";" +userEntry.getValue().getPassword() + ";" + userDelim;
        }
        return byteStr.getBytes();
    }

    public List<User> readFromDisk(String fileName, String key) throws Exception{
        List<User> users = new ArrayList();
        try {
            int cipherMode = Cipher.DECRYPT_MODE;
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);
             
            FileInputStream inputStream = new FileInputStream(fileName);
            File inputFile = new File("./address_book/users/users");
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);
             
            byte[] outputBytes = cipher.doFinal(inputBytes);
            String userData = new String(outputBytes);
            //Parse user data
            ArrayList<String> userStrings = new ArrayList(Arrays.asList(userData.split(userDelim)));
            for(String userStr : userStrings){
                users.add(User.createUserFromDBString(userStr));
            }
            inputStream.close();
        } catch (Exception e){
            throw new Exception("Failed to read file");
        }
        return users;
    }
}