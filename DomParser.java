package indexing;

import java.io.*;

import javax.xml.parsers.DocumentBuilderFactory;  
import org.w3c.dom.Element;
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;  


//import org.apache.lucene.analysis
//import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import java.util.ArrayList;
  
public class DomParser {  
	Document document = null;  
	  
    public static void main(String[] args) {  
    	DomParser domParser = new DomParser(); 
    	try { 
        	ArrayList<File> files = new ArrayList<File>(); 
            
            String tagList = "c:\\workspace\\Elsevier_metadata_tags.txt"; // 
            String inputXmlDir = "c:\\WaterEcology\\";
            String fieldnames = "c:\\workspace\\Index_fields.txt";
            String outputTxtDir = "c:\\test_dataset\\txt\\";
            domParser.listf(inputXmlDir,files );
            
            for(int i = 0; i < files.size(); i++)
            {
            String path = files.get(i).toString();
            int index1 = path.lastIndexOf("\\");
            int index2 = path.indexOf(".");
            String txtpath = path.substring(index1+1, index2);
            String fName = txtpath;
           
            System.out.println(txtpath);
            
            txtpath = outputTxtDir + txtpath + "." + "txt";
            System.out.println(path);
            
            FileWriter outputTxt = null;
            FileWriter fields = null;
            //try {
                //create a temporary file
            outputTxt = new FileWriter(txtpath, false);
            outputTxt.write("File_Name");
            outputTxt.write("\r\n");
            outputTxt.write(fName);
            outputTxt.write("\r\n");
            fields = new FileWriter(fieldnames);
            domParser.parserXML(new File(path));  
            domParser.parseItems(tagList, outputTxt, fields);
            }
        } catch (Exception e){  
            e.printStackTrace();  
        }  
    }  
  
    /**
    * Method to extract all XML file in folders and sub folders
    */
    private void listf(String directoryName, ArrayList<File> files) 
    {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
               
            } else if (file.isDirectory()) {
                listf(file.getAbsolutePath(), files);
          }
        }
    }
    /** 
     * Method parses the input XML and creates a DOM representation 
     */  
    private void parserXML(File file) throws Exception   {  
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file); 
        
        System.out.println("--------");
    }  
    /** 
     * Method to parse items  
    */  
    private void parseItems(String tagList, FileWriter writer, FileWriter indexFields ){  
    	int count =0;
    	try{
    	FileReader tagsFile = new FileReader(tagList);
    	BufferedReader bufferReader = new BufferedReader(tagsFile);
    	String tag_line;
    	indexFields.write("File_Name" + " " + 1);
    	indexFields.write("\r\n");
    	while((tag_line=bufferReader.readLine())!= null)
    	{
    		System.out.println(tag_line);
    		String[] tags = tag_line.split("=");
    		System.out.println(tags[0] + ":" );
    		
    		NodeList itemNodesList = document.getElementsByTagName(tags[1]);
    		if (tags[0].compareToIgnoreCase("Authors")==0)
    		{
    			if (itemNodesList.getLength()!=0)
    			{
    				writer.write(tags[0]);
    				writer.write("\r\n");
    				indexFields.write(tags[0] + " " + tags[2]);
    				indexFields.write("\r\n");
    				for (int i = 0 ; i < itemNodesList.getLength(); i++)
    				{
    				Element  cElement = (Element) itemNodesList.item(i);
    				writer.write(cElement.getElementsByTagName("ce:given-name").item(0).getTextContent() + " " +
    						cElement.getElementsByTagName("ce:surname").item(0).getTextContent());
    				writer.write("\r\n");
    				}
    				
    			}
    		}
    		else
    		{
    		
    		if (itemNodesList.getLength()!=0)
			{
				writer.write(tags[0]);
				writer.write("\r\n");
				indexFields.write(tags[0] + " " + tags[2]);
				indexFields.write("\r\n");
				
				for (int i = 0 ; i < itemNodesList.getLength(); i++)
				{  
					Element itemElement = (Element)itemNodesList.item(i);  
					
					System.out.println(itemElement.getTextContent());
					String line = itemElement.getTextContent() ;
					writer.write(line);
					writer.write("\r\n");
				}
			}
    		}
    	}
    	writer.close();
    	indexFields.close();
    	
    	}
    	catch(Exception e){
            System.out.println("Error while reading file line by line:" + e.getMessage());                      
    	}
     }    
}  
