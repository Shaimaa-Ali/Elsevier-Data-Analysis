package indexing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.util.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneIndex {
	String indexPath; // path to the Lucene index to be created
	String dataPath;  // path to the data files to be indexed.
	ArrayList<File> files = new ArrayList<File>(); 
	String[] fieldNames ; // field names and weights should be in one hash map
	double[] weights; 		// to be implemented 
	int maxFields;
	ElsevierDocInfo docInfo;
	LuceneIndex(String fieldNameFile_path)
	{
		this.docInfo = new ElsevierDocInfo(fieldNameFile_path);
	}
	
	
	public void setIndexPath(String i_Path)
	{
		indexPath = i_Path;
	}
	public int createIndex(String d_Path, String i_Path, String suffix) 
	{
		int numIndexed = 0;
		IndexWriter writer = null;
		ArrayList<File> files =  new ArrayList<File>();
		Hashtable indexContent = new Hashtable();
		Hashtable tagsNames = new Hashtable();
		Document indexDoc;
		listf(d_Path, files);
		try
		{
			SimpleAnalyzer analyzer = new SimpleAnalyzer(Version.LUCENE_4_9);
			IndexWriterConfig indexWriter_config = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
			indexWriter_config.setOpenMode(OpenMode.CREATE);
			indexWriter_config.setRAMBufferSizeMB(2048);
			Directory dir = FSDirectory.open(new File(i_Path));
			writer = new IndexWriter(dir, indexWriter_config);
		}
		catch(IOException e)
		{
		  System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}	
		
		for(int i = 0; i < files.size(); i++)
        {
		docInfo.setDocInfo(files.get(i).getPath());
		indexDoc = new Document(); 
		indexContent = docInfo.getDocInfo();
		tagsNames = docInfo.getTagsInfo();
		Set set_content = indexContent.entrySet();
		Set set_tags = indexContent.entrySet();
	    Iterator it_content = set_content.iterator();
	    Iterator it_tags = set_tags.iterator();
	    while (it_content.hasNext())
	    {
	    	Map.Entry entry = (Map.Entry) it_content.next();
	    	String fieldName = entry.getKey().toString();
	    	String content = entry.getValue().toString();
	    	//In order to make the creation of the index fields type dynamic 
	    	// We need to create a file that contains a list of field names together with their way of tokenization
	    	
	    	if (fieldName.compareToIgnoreCase("File_Name")==0)
	    	{
	    		System.out.println(fieldName + " ---" + content);
	    		Field docField = new Field(fieldName, content,docInfo.setNonIndexedFieldType());
	    		indexDoc.add(docField);
	       	}
	    	if(fieldName.compareToIgnoreCase("Source_Title")==0
	    			|| fieldName.compareToIgnoreCase("Authors")==0
	    			|| fieldName.compareToIgnoreCase("Keywords")==0)
	    	{
	    		System.out.println(fieldName + " ---" + content);
	    		Field docField = new Field(fieldName, content,docInfo.setIndexedNonTokenizedFieldType());
	    		indexDoc.add(docField);
	    		int boost_value = Integer.valueOf((String) tagsNames.get(fieldName));
	    		docField.setBoost( boost_value);
	    	}
	    		
	    	if (fieldName.compareToIgnoreCase("Document_Title")==0
	    			||fieldName.compareToIgnoreCase("Abstract")==0
	    			||fieldName.compareToIgnoreCase("Text")==0)
	    			
	    	{	
	    		
	    		Field docField = new Field(fieldName, content,docInfo.setIndexedTokenizedFieldType());
	    		indexDoc.add(docField);
	    		int boost_value = Integer.valueOf((String) tagsNames.get(fieldName));
	    		docField.setBoost( boost_value);
	    	}			
	    	    	    	
	    }
	    try
    	{
    		writer.addDocument(indexDoc);
    		numIndexed = numIndexed + 1; 
    		System.out.println("-------------------");
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.getMessage());
    	}
    	
	    
        }
	    try
    	{
	    	writer.commit();
	    	writer.close();
    	}catch(Exception e)
    	{
    		System.out.println(e.getMessage());
    	}
		
	    
		return numIndexed;			
	}
	
	private void listf(String directoryName, ArrayList<File> files) 
	{
	   File directory = new File(directoryName);
	   System.out.println(directoryName);
	   File[] fList = directory.listFiles();
	   System.out.println(fList.length);
	   for (File file : fList)
	   {
		   if (file.isFile()) 
		   {
			   files.add(file);
	       } else if (file.isDirectory()) {
	                listf(file.getAbsolutePath(), files);
	       		}
	   }
	 }
	
	public void searchIndex(String Term)
	{	
	}
	public double docSimilarity()
	{
		double simValue =0.0;
		return simValue;
	}	
}
