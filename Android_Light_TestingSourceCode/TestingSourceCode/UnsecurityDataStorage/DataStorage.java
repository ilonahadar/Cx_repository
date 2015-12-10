import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.omg.CORBA.Environment;


public class DataStorage {
	private void WriteToFile(String what_to_write) { 
       try{ 
        	String number = "my secret number";
            File root = Environment.getExternalStorageDirectory(); 
            if(root.canWrite()) { 
                File dir = new File(root + "write_to_the_SDcard"); 
                File datafile = new File(dir, number + ".extension"); 
                FileWriter datawriter = new FileWriter(datafile); 
                BufferedWriter out = new BufferedWriter(datawriter); 
                out.write(what_to_write); 
                out.close(); 
             } 
        
	}catch (IOException e)
	{
		
	}

}
}
