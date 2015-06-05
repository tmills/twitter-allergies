package org.chboston.cnlp.twitter.cr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.component.CasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.chboston.cnlp.twitter.Tweet;

public class TsvDirectoryCollectionReader extends CasCollectionReader_ImplBase {

  public static final String PARAM_INPUT_DIRECTORY = "InputDirectory";
  @ConfigurationParameter(name=PARAM_INPUT_DIRECTORY)
  private String inputDirectory=null;
  private List<File> fileList = null;
  private BufferedReader reader = null;
  private int numFiles = 0;
  private String nextLine = null;
  
  @Override
  public void initialize(UimaContext context)
      throws ResourceInitializationException {
    super.initialize(context);
    
    fileList = new ArrayList<>(FileUtils.listFiles(new File(inputDirectory), new String[]{ ".raw.tsv"}, true));
    numFiles = fileList.size();
    
    if(fileList.size() > 0){
      try {
        reader = new BufferedReader(new FileReader(fileList.remove(0)));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
        throw new ResourceInitializationException(e);
      }
    }
  }

  public static CollectionReaderDescription getDescription(String inputDirectory) throws ResourceInitializationException {
    return CollectionReaderFactory.createReaderDescription(TsvDirectoryCollectionReader.class, 
        TsvDirectoryCollectionReader.PARAM_INPUT_DIRECTORY, 
        inputDirectory);
  }

  @Override
  public void getNext(CAS arg0) throws IOException, CollectionException {
    JCas jcas = null;
    
    try {
      jcas = arg0.getJCas();
    } catch (CASException e) {
      e.printStackTrace();
      throw new CollectionException(e);
    }
    
    String[] fields = nextLine.split("\\t");
    Tweet tweet = null;
    try {
      tweet = new Tweet(nextLine);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    jcas.setDocumentText(tweet.getMessage());
    
    try {
      jcas.createView("Geolocation");
      jcas.getView("Geolocation").setDocumentText(tweet.getLocation());
      
      jcas.createView("Date");
      jcas.getView("Date").setDocumentText(tweet.getDate());
      
      jcas.createView("User");
      jcas.getView("User").setDocumentText(tweet.getUser());
      
    } catch (CASException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void close() throws IOException {
    if(reader != null){
      try{
        reader.close();
      }catch(IOException e){
        // do nothing
      }finally{
        reader = null;
      }
    }
  }

  @Override
  public Progress[] getProgress() {
    return new Progress[]{ new ProgressImpl((numFiles-fileList.size()), numFiles, "Files")};
  }

  @Override
  public boolean hasNext() throws IOException, CollectionException {
    // never had any
    if(reader == null) return false;
    
    // try reading next line:
    nextLine = reader.readLine();
    
    // if there is a line:
    if(nextLine != null) return true;
    
    // no line, try reading the next file:
    while(!fileList.isEmpty()){
      reader = new BufferedReader(new FileReader(fileList.remove(0)));
      nextLine = reader.readLine();
      if(nextLine != null) return true;
    }
    
    // file list is empty
    return false;
  }
}
