package org.chboston.cnlp.twitter;

import java.io.File;

import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.chboston.cnlp.twitter.cr.TsvDirectoryCollectionReader;

public class TweetDictionaryProcessor {

  private File[] fileList;
  private CollectionReaderDescription reader = null;
  
  public TweetDictionaryProcessor(String inputDirectory) throws ResourceInitializationException{
    this.fileList = fileList;
    this.reader = TsvDirectoryCollectionReader.getDescription(inputDirectory);
  }
  
  
}
