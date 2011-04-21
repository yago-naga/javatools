package javatools.parsers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javatools.administrative.Announce;




/**
 * Class representing human languages 
 *
 * @author Gerard de Melo
 */
public class Language implements Comparable<Language>{
	String id;
  protected static final Map<String,String> supported= new HashMap<String,String>();
  static{
    supported.put("en","English");
    supported.put("de","German");
    supported.put("fr","French");
    supported.put("es","Spanish");
    supported.put("it","Italian");    
  }
  
	  
	/**
	 * Constructor
	 * @param id  ISO 639-1 language code
	 */
  public Language(String id) throws LanguageNotSupportedException{
    if(!supportedLanguage(id))
      throw new LanguageNotSupportedException();
    this.id = id;
  }
  
	protected static final Language generateLanguage(String id) {
    Language lang=null;
    try{
      lang= new Language(id);
    }catch(LanguageNotSupportedException ex){Announce.error(ex);}    
    return lang;
	}
	
  public final boolean supportedLanguage(String lang){    
    for(String supLang :supported.keySet())
      if(supLang.equals(lang))
        return true;
    return false;
  }
  
  public final String getLongForm(){
    return supported.get(id);
  }
  
	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}
	
	@Override
	public final boolean equals(Object other) {
		if (this == other)  // same object
			return true;
		if (other == null)
			return false;
		if (!(other instanceof Language))
			return false;
		// now check contents
		Language otherLanguage = (Language) other;
		return (this.id.equals(otherLanguage.id));
	}
	
	@Override
	public final int hashCode() {
		if (id == null)
			return 0;
		else
			return id.hashCode();
	}
	
	public final int compareTo(Language other) {
        return id.compareTo(other.id); 
    }


  
	/**
	 * Modern English
	 */ 
	public static final Language ENGLISH = generateLanguage("en");
	
	/**
	 * German
	 */
	public static final Language GERMAN = generateLanguage("de");
	
	/**
	 * French
	 */
	public static final Language FRENCH = generateLanguage("fr");
	
	/**
	 * Spanish
	 */
	public static final Language SPANISH = generateLanguage("es");
	
	/**
	 * Italian
	 */
	public static final Language ITALIAN = generateLanguage("it");
  
  
  
  // ---------------------------------------------------------------------
  //           Exceptions
  // ---------------------------------------------------------------------

  public static class LanguageNotSupportedException extends Exception{
    private static final long serialVersionUID = 1L;
    public LanguageNotSupportedException(){
      super();
    }
    public LanguageNotSupportedException(String message){
      super(message);
    }
    public LanguageNotSupportedException(String message, Throwable cause){
      super(message,cause);
    }       
  }
  
}
