import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by prasanna on 4/10/17.
 */
public class ProductCipher {

    //Initialize block size for encode and decode
    public static final int BLOCK_SIZE = 5;
    //Initialize permutation pattern
    //Take block with size BLOCK_SIZE, {1,2,3,4,5} => {3,5,1,2,4}
    public static final List<Integer> PATTERN = Arrays.asList(3,5,1,2,4);

    //Initialize substitute value
    public static final int SUB_VALUE = -5;


    public static void main(String[] args){
        try {
            //Read the text file and get the input string to encord or decord
            //Whenever you encoded any string, encoded text file will be created and
            //encoded string will be saved.

            //For get the user inputs
            Scanner sc = new Scanner(System.in);

            //Boolean con variable for rerun the program untill user press q
            boolean con = true;
            while(con) {

                //For store the input message for encryp or decrype
                String message = "";

                //Let user select for encryption or decryption
                System.out.println("Press '1' for encrypt data or press '2' for decrypt data");
                String operation = sc.nextLine();

                //Select file name
                System.out.println("Insert name of the text file contain message [eg: sample.txt] ");

                //read the data from text file
                String path = sc.nextLine();
                FileReader reader = new FileReader(path);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    message += line + " ";
                }
                reader.close();

                //Hardcoded for testing purposes
                //message = "Prasanna";
                //message = "\\\\Kmn\\\u001Bii\u001B";

                //Print the input string
                System.out.println("Input message :- " + message);

                //Get the byte array of given string
                //Here I'm using UTF-8 for encoding characters.
                //UTF-8 is a character encoding capable of encoding all possible characters
                byte[] byteLstInput = message.getBytes("UTF-8");
                ArrayList<Byte> byteArrInput = new ArrayList<>();

                //Test the character encoded byte array
                //String chrEncodedByteArr = "[";
                for (byte chrByte : byteLstInput) {
                //  chrEncodedByteArr += String.valueOf(chrByte) + ",";
                    byteArrInput.add(chrByte);
                }
                //chrEncodedByteArr = chrEncodedByteArr.substring(0, chrEncodedByteArr.length() - 1) + "]";
                //System.out.println("Character encoded byte array  :- " + chrEncodedByteArr);

                if (operation.equals("1")) {
                    ArrayList<Byte> test = encodeString(byteArrInput);

                    byte[] result = new byte[test.size()];
                    for (int i = 0; i < test.size(); i++) {
                        result[i] = test.get(i).byteValue();
                    }

                    System.out.println("Encoded String :- " + new String(result, "UTF-8"));

                    //Write encoded data to a txt file for later decryption
                    File file = new File("EncodeText.txt");
                    // If file doesn't exists, then create it
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    // Write in file
                    bw.write(new String(result, "UTF-8"));
                    // Close connection
                    bw.close();
                    System.out.println("EncodeText.txt file updated!" );

                } else if (operation.equals("2")) {
                    ArrayList<Byte> test = decodeString(byteArrInput);

                    byte[] result = new byte[test.size()];
                    for (int i = 0; i < test.size(); i++) {
                        result[i] = test.get(i).byteValue();
                    }

                    System.out.println("Decoded String :- " + new String(result, "UTF-8"));
                } else {
                    System.out.println("Invalid input detected!");
                }
                System.out.println("Retry? [y/n]");
                operation = sc.nextLine();
                if (operation.equals("n") || operation.equals("N")){
                    con = false;
                }
                System.out.println();
            }
            System.out.println("Good Bye!");

        }catch (FileNotFoundException e){
            System.out.println("Your input file is not found!");
        }catch (Exception e){
            System.out.println("An error occurred while processing [" + e.toString() + "]");
        }
    }

    public static ArrayList<Byte> getDecodeSubstitutionArr(ArrayList<Byte> byteArrInput){
        //Create substitution array
        for(int i=0; i<byteArrInput.size();i++){
            byteArrInput.set(i, (byte) (byteArrInput.get(i) - SUB_VALUE));
        }
        //Return the substitute array
        return byteArrInput;
    }

    public static ArrayList<Byte> decodeString(ArrayList<Byte> byteArrInput){
        ArrayList<Byte> byteSubstituteArray = getDecodeSubstitutionArr(byteArrInput);
        ArrayList<Byte> decodedStringArr = new ArrayList<>();

        for(int i=0; i<byteSubstituteArray.size()-1; i+=BLOCK_SIZE){
            byte[] byteArr = new byte[BLOCK_SIZE];
            for(int j=0; j<BLOCK_SIZE; j++){
                byteArr[j] = byteSubstituteArray.get(i+j);
            }
            for(int j=0; j<BLOCK_SIZE; j++) {
                decodedStringArr.add(i + j, byteArr[PATTERN.indexOf(j+1)]);
            }
        }
        return decodedStringArr;
    }

    public static ArrayList<Byte> getEncodeSubstitutionArr(ArrayList<Byte> byteArrInput){
        //Check weather there are any blocks with characters less than BLOCK_SIZE
        //If so, replace them with UTF-8 value 32 (Space Character)
        while(byteArrInput.size() % 5 != 0){
            byteArrInput.add(Byte.valueOf("32"));
        }
        //Create substitution array
        for(int i=0; i<byteArrInput.size();i++){
            byteArrInput.set(i, (byte) (byteArrInput.get(i) + SUB_VALUE));
        }
        //Return the substitute array
        return byteArrInput;
    }

    public static ArrayList<Byte> encodeString(ArrayList<Byte> byteArrInput){
        ArrayList<Byte> byteSubstituteArray = getEncodeSubstitutionArr(byteArrInput);
        ArrayList<Byte> encodedStringArr = new ArrayList<>();

        for(int i=0; i<byteSubstituteArray.size(); i+=BLOCK_SIZE){
            byte[] byteArr = new byte[BLOCK_SIZE];
            for(int j=0; j<BLOCK_SIZE; j++){
                byteArr[j] = byteSubstituteArray.get(i+j);
            }
            for(int j=0; j<BLOCK_SIZE; j++) {
                encodedStringArr.add(i + j, byteArr[PATTERN.get(j) - 1]);
            }
        }
        return encodedStringArr;
    }
}
