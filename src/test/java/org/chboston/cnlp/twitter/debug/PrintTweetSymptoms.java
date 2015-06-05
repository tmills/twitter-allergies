package org.chboston.cnlp.twitter.debug;

import java.util.List;

import org.apache.ctakes.typesystem.type.refsem.UmlsConcept;
import org.apache.uima.resource.ResourceInitializationException;
import org.chboston.cnlp.twitter.TweetDictionaryProcessor;

public class PrintTweetSymptoms {
  public static void main(String[] args) throws ResourceInitializationException{
    TweetDictionaryProcessor proc = new TweetDictionaryProcessor(args[0]);
    
    while(proc.next()){
      String message = proc.getTweet();
      double[] loc = proc.getLocation();
      List<UmlsConcept> cuis = proc.getConcepts();
      
      
      System.out.println(String.format("****%s****", message));
      for(UmlsConcept con : cuis){
        System.out.println(con.getCui());
      }
      
    }
  }
}
