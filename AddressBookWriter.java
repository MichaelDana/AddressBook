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

public class AddressBookWriter{
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";
	private static final String recordDelim = "_ADDRECORD_";
	public void writeToDisk(String addressBookFile, 
							int numRecords,
							Iterator addressBookIterator) 
							throws IOException
	{
		// ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(addressBookFile));
		// out.writeInt(numRecords);
		// while(addressBookIterator.hasNext())
		// 	out.writeObject(addressBookIterator.next());

  	 	FileWriter writer = new FileWriter(addressBookFile,true);
		while(addressBookIterator.hasNext()){
			Map.Entry addressBookMapEntry = ((Map.Entry)addressBookIterator.next());
			writer.write(((Record)addressBookMapEntry.getValue()).toDbString() + "\n");
		}
		writer.close();
	}

	public void saveAddressBook(String fileName, String key, HashMap<String,Record> records) throws Exception{
		try{
            int cipherMode = Cipher.ENCRYPT_MODE;
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);
                
            byte[] outputBytes = cipher.doFinal(addressBookMapToBytes(records));
            FileOutputStream outputStream = new FileOutputStream(fileName);
            outputStream.write(outputBytes);
        } catch (Exception e){
            throw new Exception("Failed to write file");
        }
	}

	private byte[] addressBookMapToBytes(HashMap<String,Record> records){
		String byteStr = "";
		for(Map.Entry<String,Record> recordEntry : records.entrySet()){
			byteStr += recordEntry.getValue().toDbString() + recordDelim;
		}
		return byteStr.getBytes();
	}
	
	public List<Record> readFromDisk(String addressBookFile){
		List<Record> newRecords = null;
		try{
			FileReader reader = new FileReader(addressBookFile);
            BufferedReader in = new BufferedReader(reader);
			newRecords =  new ArrayList<>();
			String dbString;
            while ((dbString = in.readLine()) != null) {
                newRecords.add(Record.createRecordsFromArgs(Record.fromDbStringToArgsList(dbString)));
            }
            reader.close();
		} catch (Exception e){
			System.out.println(e);
			System.out.println(e.getStackTrace());
			return null;
		}

		return newRecords;
	}

	public List<Record> loadAddressBook(String fileName, String key) throws Exception{
		List<Record> records = new ArrayList<>();
		if(!new File(fileName).exists()){
            return records;
        }
		try {
            int cipherMode = Cipher.DECRYPT_MODE;
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);
             
            FileInputStream inputStream = new FileInputStream(fileName);
            File inputFile = new File(fileName);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);
             
            byte[] outputBytes = cipher.doFinal(inputBytes);
            String userData = new String(outputBytes);
            //Parse user data
            ArrayList<String> userStrings = new ArrayList(Arrays.asList(userData.split(recordDelim)));
            for(String userStr : userStrings){
				records.add(Record.createRecordsFromArgs(Record.fromDbStringToArgsList(userStr)));
            }
            inputStream.close();
        } catch (Exception e){
			System.out.println(e);
			throw new Exception("Failed to read file");
		}
		return records;
	}
}