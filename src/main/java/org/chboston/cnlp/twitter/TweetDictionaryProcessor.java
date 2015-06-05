package org.chboston.cnlp.twitter;

import java.util.ArrayList;
import java.util.List;

import org.apache.ctakes.clinicalpipeline.ClinicalPipelineFactory;
import org.apache.ctakes.typesystem.type.refsem.UmlsConcept;
import org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention;
import org.apache.ctakes.typesystem.type.textsem.SignSymptomMention;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.pipeline.JCasIterator;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.chboston.cnlp.twitter.cr.TsvDirectoryCollectionReader;

public class TweetDictionaryProcessor {

  private CollectionReaderDescription reader = null;
  private AnalysisEngineDescription dictAe = null;
  private JCasIterator iter = null;
  private String tweet = null;
  private double[] location = null;
  private String date = null;
  private String user = null;
  private List<UmlsConcept> concepts = null;
  
  public TweetDictionaryProcessor(String inputDirectory) throws ResourceInitializationException{
    this.reader = TsvDirectoryCollectionReader.getDescription(inputDirectory);
    this.dictAe = ClinicalPipelineFactory.getFastPipeline();
    this.iter = (new JCasIterable(reader, dictAe)).iterator();
  }
  
  public boolean next(){
    boolean hasNext = false;
    
    if(this.iter.hasNext()){
      hasNext = true;
      JCas jcas = this.iter.next();
      this.tweet = jcas.getDocumentText();
//      this.location = jcas.getView("Geolocation").getDocumentText();
      try{
        this.user = jcas.getView("User").getDocumentText();
        this.date = jcas.getView("Date").getDocumentText();
      } catch (CASException e) {
        e.printStackTrace();
      }
      
      concepts = new ArrayList<>();
      for(SignSymptomMention ss : JCasUtil.select(jcas, SignSymptomMention.class)){
        for(UmlsConcept con : JCasUtil.select(ss.getOntologyConceptArr(), UmlsConcept.class)){
          concepts.add(con);
        }
      }
      for(DiseaseDisorderMention dd : JCasUtil.select(jcas, DiseaseDisorderMention.class)){
        for(UmlsConcept con : JCasUtil.select(dd.getOntologyConceptArr(), UmlsConcept.class)){
          concepts.add(con);
        }
      }
    }
    
    return hasNext;
  }
  
  public String getTweet(){
    return this.tweet;
  }
  
  public double[] getLocation(){
    return this.location;
  }
  
  public List<UmlsConcept> getConcepts(){
    return this.concepts;
  }
  
}
