package indexing;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.*;
/* A class to extract the fields from text files to be indexed
 * The user of this class has to
 * 1- provide the path of the text file to be parsed, 
 * 2- the List of fields to be parsed
 */
 
public class ElsevierDocInfo {
	String fieldsNamesPath;
	String documentTxtPath;
	private Hashtable elsevierTags = new Hashtable();
	private Hashtable docInfo = new Hashtable();
	ElsevierDocInfo (String fieldsNamesPath)
	{
		this.fieldsNamesPath = fieldsNamesPath;
		this.setTagsInfo();
	}
	
	public FieldType setNonIndexedFieldType()
	{
		FieldType ft = new FieldType(StringField.TYPE_STORED);
		ft.setStored(true);
		ft.setIndexed(false);
		ft.setTokenized(false);
		return ft;
	}
	
	public FieldType setIndexedTokenizedFieldType()
	{
		FieldType tokenizedType = new FieldType();
		tokenizedType.setIndexed(true);
		tokenizedType.setTokenized(true);
		tokenizedType.setStored(true);
		tokenizedType.setStoreTermVectors(true);
		tokenizedType.setStoreTermVectorOffsets(true);
		tokenizedType.setStoreTermVectorPositions(true);
		tokenizedType.setStoreTermVectorPayloads(true);
		return tokenizedType;
	}
	
	public FieldType setIndexedNonTokenizedFieldType()
	{
		FieldType nonTokenizedType = new FieldType();
		nonTokenizedType.setIndexed(true);
		nonTokenizedType.setTokenized(false);
		nonTokenizedType.setStored(true);
		
		nonTokenizedType.setStoreTermVectors(true);
		nonTokenizedType.setStoreTermVectorOffsets(true);
		nonTokenizedType.setStoreTermVectorPositions(true);
		nonTokenizedType.setStoreTermVectorPayloads(true);
	
		return nonTokenizedType;
	}
	
	
	private void setTagsInfo()
	{
		try
		{
			System.out.println(fieldsNamesPath);
			FileReader fieldNames_File = new FileReader(fieldsNamesPath);
			BufferedReader bufferReader = new BufferedReader(fieldNames_File);
			String line;
			// this line needs to be initialized dynamically
			elsevierTags.put("File_Name", 1);	
			while((line=bufferReader.readLine())!= null)
	    	{		
				String[] fields_W = line.split(" +");
				elsevierTags.put(fields_W[0], fields_W[1]);
				
			}
		}
		catch(Exception e){
            System.out.println("Error while reading file line by line:" + e.getMessage());                      
    	}		
		Set set = elsevierTags.entrySet();
	    Iterator it = set.iterator();
	    while (it.hasNext())
	    {
	      Map.Entry entry = (Map.Entry) it.next();
	  
	    }		
	}
	
	private boolean isField(String str)
	{
		boolean found = false;
		Set set = elsevierTags.entrySet();
	    Iterator it = set.iterator();
	    while (it.hasNext())
	    {
	      Map.Entry entry = (Map.Entry) it.next();
	      String field = entry.getKey().toString();
	      if (field.compareToIgnoreCase(str)==0) 
	      {
	    	found = true;
			break;
	      }
	      else 
	      {
				found = false;
	      }
	    }
		return found;
	}
	
	public void setDocInfo(String docTxtPath)
	{
		int count = 0;
		boolean found= false;
		this.documentTxtPath = docTxtPath;
		try
		{
			System.out.println(documentTxtPath);
			FileReader txt_File = new FileReader(documentTxtPath);
			BufferedReader bufferReader = new BufferedReader(txt_File);
			String line;
			String field_name = "";
			String field_content= "";
			while((line=bufferReader.readLine())!= null)
	    	{			
				if(isField(line))
				 {
					if (!field_name.isEmpty() && !field_content.isEmpty() )
					{
						docInfo.put(field_name, field_content);	
						field_name = line;
						field_content= "";
					}
					else
					{
						field_name = line;
					}
				 }
				 else 
				 {
					field_content = field_content.concat(" " + line) ;
				 }				 
			}					
	    }
		catch(Exception e){
            System.out.println("Error while reading file line by line:" + e.getMessage());                      
    	}
	}
	
	public Hashtable getDocInfo()
	{
		return docInfo;	 
	}
	
	public Hashtable getTagsInfo()
	{
		return elsevierTags;	 
	}
	
}
