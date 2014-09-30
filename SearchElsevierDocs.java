package indexing;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class SearchElsevierDocs {
	 public static void main(String[] args) 
	 {
		 String indexPath = "c:\\test_dataset\\index\\";
		 Query query;
		 
		 String queryfile = "c:\\workspace\\test_queries.txt"; // the file containing the list of queries
		 String queryresult = "c:\\workspace\\result.txt";
		 
		try
		{
		Directory dir = FSDirectory.open(new File(indexPath));
		SimpleAnalyzer analyzer = new SimpleAnalyzer(Version.LUCENE_4_9);
		DirectoryReader ireader = DirectoryReader.open(dir);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		FileReader fieldNames_File = new FileReader(queryfile);
		BufferedReader bufferReader = new BufferedReader(fieldNames_File);
		FileWriter outputTxt = null;
        outputTxt = new FileWriter(queryresult, false);
		String line;
		// this line needs to be initialized dynamically
		while((line=bufferReader.readLine())!= null)
    	{		
			QueryParser parser = new QueryParser(Version.LUCENE_4_9, "Text", analyzer);
			query = parser.parse(line);
			 
			ScoreDoc[] hits = isearcher.search(query,3).scoreDocs;
			 outputTxt.write(line + "::");
			 outputTxt.write("\r\n");
			 for (int i = 0; i < hits.length; i++) {
			      Document hitDoc = isearcher.doc(hits[i].doc);
			      
			  //Replace the string in the hitDoc.get with the field names you have    
			      outputTxt.write(hitDoc.get("File_Name"));
			      outputTxt.write("\r\n");
			      outputTxt.write(String.valueOf(hits[i].score));
			      System.out.println(hitDoc.get("File_Name"));
			      outputTxt.write("\r\n");
			      outputTxt.write(hitDoc.get("Document_Title"));
			      System.out.println(hitDoc.get("Document_Title"));
			      outputTxt.write("\r\n");
			      outputTxt.write(hitDoc.get("Authors"));
			      System.out.println(hitDoc.get("Authors"));
			      outputTxt.write("\r\n");
			      outputTxt.write(hitDoc.get("Abstract"));
			      outputTxt.write("\r\n");
			    System.out.println(hitDoc.get("Abstract"));
			    }
			  outputTxt.write("-------------------------------");
			  outputTxt.write("\r\n");
			}
		    ireader.close();
	    dir.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	 }
}
