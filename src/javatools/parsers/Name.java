package javatools.parsers;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javatools.administrative.D;
import javatools.datatypes.FinalMap;
import javatools.datatypes.FinalSet;
/** 
This class is part of the Java Tools (see http://mpii.de/~suchanek/downloads/javatools).
It is licensed under the Creative Commons Attribution License 
(see http://creativecommons.org/licenses/by/3.0) by 
Fabian M. Suchanek (see http://mpii.de/~suchanek).
  
If you use the class for scientific purposes, please cite our paper
  Fabian M. Suchanek, Georgiana Ifrim and Gerhard Weikum
  "Combining Linguistic and Statistical Analysis to Extract Relations from Web Documents" (SIGKDD 2006)

The class Name represents a name. There are three sub-types (subclasses) of names:
Abbreviations, person names and company names. These subclasses provide methods to
access the components of the name (like the family name). Use the factory method Name.of to
create a Name-object of the appropriate subclass.<BR>
Example:
<PRE>
  Name.isName("Mouse");
  --> true
  Name.isAbbreviation("PMM");
  --> true  
  Name.isPerson("Mickey Mouse");
  --> false
  Name.couldBePerson("Mickey Mouse");
  --> true
  Name.isPerson("Prof. Mickey Mouse");
  --> true
  Name.of("Prof. Dr. Fabian the Great III of Saarbruecken").describe()
  // equivalent to new PersonName(...) in this case
  -->
  PersonName
    Original: Prof. Dr. Fabian the Great III of Saarbruecken
    Titles: Prof. Dr.
    Given Name: Fabian
    Given Names: Fabian
    Family Name Prefix: null
    Attribute Prefix: the
    Family Name: null
    Attribute: Great
    Family Name Suffix: null
    Roman: III
    City: Saarbruecken
    Normalized: Fabian_Great
</PRE>
*/

public class Name {
  /** Holds the general default name */
  public static final String ANYNAME="NAME";

  // -----------------------------------------------------------------------------------
  //                 Punctation
  // -----------------------------------------------------------------------------------

  /** Contains romam digits */
  public static String roman="\\b(?:[XIV]++)\\b";
  
  /** Contains "of" */
  public static String of="\\bof\\b";
  
  /** Contains upper case Characters*/
  public static final String U="\\p{Lu}";

  /** Contains lower case Characters*/
  public static final String L="\\p{Ll}";
  
  /** Contains characters*/
  public static final String A="\\p{L}";
  
  /** Contains blank */
  public static final String B="(?:[\\s_]++)";

  /** Contains a word boundary*/
  public static final String BD="\\b";

  /** Contains blank with optional comma*/
  public static final String BC="[,\\s_]++";

  /** Contains digits */
  public static final String DG="\\d";

  /** Contains hypens*/
  public static final String H="-";
  
  /** Contains "|"*/
  public static final String or="|";


  /** Repeats the token with blanks one or more times*/
  public static String mul(String s) {
    return("(?:"+s+B+")*"+s);
  }

  /** Repeats the token with hyphens one or more times*/
  public static String mulHyp(String s) {
    return("(?:"+s+H+")*"+s);
  }

  /** optional component*/
  public static String opt(String s) {
    return("(?:"+s+")?");
  }

  /** optional multiple component*/
  public static String optMul(String s) {
    return("(?:"+s+")*");
  }

  /** alternavive */
  public static String or(String s1, String s2) {
    return("(?:"+s1+"|"+s2+")");
  }

  /** Capturing group*/
  public static String c(String s) {
    return("("+s+")");
  }

   
  // -----------------------------------------------------------------------------------
  //                 Family Name Prefixes
  // -----------------------------------------------------------------------------------

  /** Contains common family name prefixes (like "von") */
  public static final String familyNamePrefix="(?:"+
      "[aA]l|[dD][ea]|[dD]el|[dD]e las|[bB]in|[dD]e la|[dD]e los|[dD]i|[zZ]u[mr]|[aA]m|[vV][oa]n de[rnm]|[vV][oa][nm]|[dD]o|[dD]')";
  public static final Pattern familyNamePrefixPattern=Pattern.compile(familyNamePrefix);
  
  /** Says whether this String is a family name prefix */
  public static boolean isFamilyNamePrefix(String s) {
    return(familyNamePrefixPattern.matcher(s).matches());
  }
  
  // -----------------------------------------------------------------------------------
  //                 Attribute Prefixes
  // -----------------------------------------------------------------------------------
  
  /** Contains attribute Prefixes (like "the" in "Alexander the Great") */
  public static String attributePrefix="(?:"+
      "the|der|die|il|la|le)";

  public static Pattern attributePrefixPattern=Pattern.compile(attributePrefix);
  
  /** Says whether this String is an attribute Prefix (like "the" in "Alexander the Great") */
  public static boolean isAttributePrefix(String s) {
    return(s.matches(attributePrefix));
  }

  // -----------------------------------------------------------------------------------
  //                 Attribute Suffixes
  // -----------------------------------------------------------------------------------

  /** Contains common name suffixes (like "Junior") */
  public static final String familyNameSuffix="(?:"+
      "CBE|"+ // Commander
      "DBE|"+ // Knight or Dame Commander
      "GBE|"+ // Knight or Dame Grand Cross
      "[jJ]r\\.?|"+
      "[jJ]unior|"+
      "hijo|"+
      "hija|"+   
      "P[hH]\\.?[dD]\\.?|"+
      "KBE|"+ // Knight or Dame Commander
      "MBE|"+  // Member
      "M\\.?D\\.|"+
      "OBE|"+ // Officer
      "[sS]enior|"+
      "[sS]r\\.?)";
  
  public static final Pattern familyNameSuffixPattern=Pattern.compile(familyNameSuffix);
  
  /** Says whether this String is a person name suffix */
  public static boolean isPersonNameSuffix(String s) {
    return(familyNameSuffixPattern.matcher(s).matches());
  }

  // -----------------------------------------------------------------------------------
  //                 Titles
  // -----------------------------------------------------------------------------------

  /** Contains common titles (like "Mr.") */
  public static final String title="\\b(?:"+
        "[aA]dmiral\\b|"+
        "[aA]mbassador\\b|"+
        "[bB]ishop\\b|"+
        "[bB]brother\\b|"+
        "[cC]aptain\\b|"+
        //"cardinal\\b|"+ // Problems with "Cardinal Health"
        "[cC]hancellor\\b|"+
        "[cC]ol\\.|"+        
        "[cC]olonel\\b|"+
        "[cC]ommander\\b|"+
        "[cC]ongressman\\b|"+
        "[cC]ongresswoman\\b|"+
        "[dD]rs?\\.?|"+
        "[fF]ather\\b|"+
        //"general\\b|"+  // too many problems with this one (cf. "general motors")
        "[gG]gouverneur\\b|"+
        "[gG]ov\\.\\b|"+
        "[gG]overnor\\b|"+
        "[hH]onorable\\b|"+
        "[hH]onourable\\b|"+
        "[jJ]udge\\b|"+
        "[kK]ing\\b|"+
        "[lL]ady\\b|"+
        "[lL]ieutenant\\b|"+
        "[lL]ieutenant [gG]overnor\\b|"+
        "[lL]ord\\b|"+
        "[mM]aj\\.|"+
        "[mM]ajor\\b|"+        
        "[mM]aster\\b|"+
        "[mM]essrs\\.?|"+
        "[mM]iss\\b|"+
        "[mM]rs?\\.|"+
        "[mM]rs?\\b|"+
        "[mM]s\\.|"+
        "[mM]s\\b|"+
        "[pP]ope\\b|"+
        "[pP][hH]\\.?[dD]\\b|"+
        "[pP]resident\\b|"+
        "[pP]rof\\.?|"+
        "[pP]rofessor\\b|"+
        "[pP]rince\\b|"+        
        "[pP]rincess\\b|"+                
        "[rR]abbi\\b|"+
        "[rR]evd?\\.|"+
        "[rR]everend\\b|"+
        "[qQ]ueen\\b|"+
        "[sS]aint\\b|"+
        "[sS]t\\.|"+
        "[sS]t\\b|"+
        "[sS]ecretary\\b|"+
        "[sS]enator\\b|"+
        "[sS]ergeant\\b|"+
        "[sS]ir\\b|"+
        "[sS]ister\\b|"+
        "[sS]ultan\\b|" +
        "[eE]mperor\\b|"+
        "[eE]mpress)";

  public static final Pattern titlePattern=Pattern.compile(title);
  
  public static final String titles="(?:"+title+B+")*"+B+title;
  
  /** Says whether this String is a title */
  public static boolean isTitle(String s) {
    return(titlePattern.matcher(s).matches());
  }

  /** Contains those titles that go with the given name (e.g. "Queen" in "Queen Elisabeth"), lowercase*/
  public static Set<String> titlesForGivenNames=new FinalSet<String>(
        "brother","father","king","lady","pope","prince","princess","queen","sister","sultan",
        "emperor","empress","st","st.");

  // -----------------------------------------------------------------------------------
  //                 Company Name Suffixes
  // -----------------------------------------------------------------------------------

  /** Contains common company name suffixes (like "Inc") */
  public static final String companyNameSuffix="(?:"+
        "[cC][oO]\\.|"+
        "[cC][oO]\\b|"+
        "&"+B+"?[cC][oO]\\.|"+
        "&"+B+"?[cC][oO]\\b|"+
        "\\b[cC][oO][rR][pP]\\.|"+
        "\\b[cC][oO][rR][pP]\\b|"+
        "\\b[cC]orporation\\b|"+
        "\\b[iI][nN][cC]\\.|"+
        "\\b[iI][nN][cC]\\b|"+
        "\\b[iI]ncorp\\.?|"+
        "\\b[iI]ncorp\\b|"+
        "\\b[iI]ncorporation\\b|"+
        "\\b[lL][tT][dD]\\.|"+
        "\\b[lL][tT][dD]\\b|"+
        "\\b[lL]imited\\b)";

  public static final Pattern companyNameSuffixPattern=Pattern.compile(companyNameSuffix);
  
  /** Says whether this String is a company name suffix */
  public static boolean isCompanyNameSuffix(String s) {
    return(companyNameSuffixPattern.matcher(s).matches());
  }
  
  // -----------------------------------------------------------------------------------
  //                 Names
  // -----------------------------------------------------------------------------------

  /** Contains prepositions*/
  public static final String prep="(?:on|of|for)";
  
  /** Contains the pattern for names. Practically everything is a name if it starts with an uppercase letter*/
  public static final String laxName=BD+U+".*"+BD;

  /** Contains the pattern for names. Practically everything is a name if it starts with an uppercase letter*/
  public static final Pattern laxNamePattern=Pattern.compile(laxName);

  /** Contains a pattern that indicates strings that are very likely to be names*/
  public static final String safeName=BD+U+"("+H+"["+U+DG+"]|["+U+L+DG+"]){2,}"+BD;

  /** Contains a pattern that indicates strings that are very likely to be names*/
  public static final Pattern safeNamePattern=Pattern.compile(safeName);

  /** Contains a pattern that indicates strings that are very likely to be names*/
  public static final Pattern safeNamesPattern=Pattern.compile(safeName+optMul(B+opt(prep+B)+safeName));  

  /** Contains a pattern that indicates strings that are very likely to be names*/
  public static final Pattern safeNamesPatternNoPrep=Pattern.compile(safeName+optMul(B+safeName));  

  /** Tells whether a String is a name with high probability */
  public static boolean isName(String s) {
    return(safeNamePattern.matcher(s).matches());
  }

  /** Tells whether a String is a sequence of names with high probability */
  public static boolean isNames(String s) {
    return(safeNamesPattern.matcher(s).matches());
  }

  /** Tells whether a String could possibly be a name*/
  public static boolean couldBeName(String s) {
    return(laxNamePattern.matcher(s).matches());
  }

  // -----------------------------------------------------------------------------------
  //                 Names: Members
  // -----------------------------------------------------------------------------------

  /** Holds the original name */
  protected String original;
  
  /** Holds the normalized name */
  protected String normalized;
  
  /** Returns the original name */
  public String toString() {
    return(original);
  }
  
  /** Returns the letters and digits of the original name (eliminates punctuation)*/
  public String normalize() {
    if(normalized==null) normalized=original.replaceAll(B,"_").replaceAll("([\\P{L}&&[^\\d]&&[^_]])", "");
    return(normalized);
  }
  
  /** Constructor (for subclasses only; use Name.of instead!) */
  protected Name(String s) {
    original=s;
  }
    
  /** Returns a description */
  public String describe() {
    return("Name\n"+
           "  Original: "+original+"\n"+
           "  Normalized: "+normalize());
  }

  /** Returns the original name */
  public String original() {
    return(original);
  }

  // -----------------------------------------------------------------------------------
  //                 Abbreviations
  // -----------------------------------------------------------------------------------

  /** Contains the lax pattern for abbreviations */
  public static final Pattern laxAbbreviationPattern=Pattern.compile(BD+U+"["+U+DG+B+H+"]++"+BD);

  /** Contains the safe pattern for abbreviations */
  public static final Pattern safeAbbreviationPattern=Pattern.compile(BD+U+"["+U+DG+H+"\\.]++"+BD);

  /** Tells whether a string is an abbreviation with high probability*/
  public static boolean isAbbreviation(String word) {
     return(safeAbbreviationPattern.matcher(word).matches());
  }

  /** Tells whether a string could be abbreviation.*/
  public static boolean couldBeAbbreviation(String word) {
     return(laxAbbreviationPattern.matcher(word).matches());
  }

  public static class Abbreviation extends Name {
    public Abbreviation(String s) {
      super(s);
      if(!laxAbbreviationPattern.matcher(s).matches()) return;
    }
    public String normalize() {
      if(normalized==null) normalized=super.normalize().toUpperCase();
      return(normalized);
    }
    /** Returns a description */
    public String describe() {
      return("Abbreviation\n"+
             "  Original: "+original+"\n"+
             "  Normalized: "+normalize());
    }
    
  }

  // -----------------------------------------------------------------------------------
  //                 Company Names
  // -----------------------------------------------------------------------------------

  /** Contains the pattern for companies*/
  public static final Pattern laxCompanyPattern=Pattern.compile("("+laxNamePattern+")"+BC+"("+companyNameSuffix+")");

  /** Contains the safe pattern for companies*/
  public static final Pattern safeCompanyPattern=Pattern.compile("("+safeNamesPatternNoPrep+opt(opt(B)+"&"+opt(B)+safeNamesPatternNoPrep)+")"+BC+"("+companyNameSuffix+")");

  /** Tells if the string is a company name with high probability*/
  public static boolean isCompanyName(String s) {
    return(safeCompanyPattern.matcher(s).matches());
  }

  /** Tells if the string could be a company name */
  public static boolean couldBeCompanyName(String s) {
    return(laxCompanyPattern.matcher(s).matches());
  }

  public static class CompanyName extends Name {
    protected String name;
    protected String suffix;
    public CompanyName(String s) {
      super(s);
      Matcher m=laxCompanyPattern.matcher(s);
      if(!m.matches()) return;
      name=m.group(1);
      suffix=m.group(2);
    }
    /**Returns the name.*/
    public String name() {
      return name;
    }
    /**Returns the suffix.*/
    public String suffix() {
      return suffix;
    }
    public String normalize() {
      return(name);
    }
    /** Returns a description */
    public String describe() {
      return("CompanyName\n"+
             "  Original: "+original+"\n"+
             "  Name: "+name+"\n"+             
             "  Suffix: "+suffix+"\n"+             
             "  Normalized: "+normalize());
    }    
  }
  
  // -----------------------------------------------------------------------------------
  //                 Person Names
  // -----------------------------------------------------------------------------------

  /** A direct family name prefix (such as "Mc")*/
  public static final String directFamilyNamePrefix=BD+"(?:(?:al-|Mc|Di|De|Mac|O')"+B+"?)";

  /** The pattern "Name" */
  public static final String personNameComponent=U+L+"+";    

  /** The pattern "Name." */
  public static final String givenNameComponent=or(or(personNameComponent+BD,U+L+"*+\\."),U+BD);    

  /** The pattern "Name[-Name]" */
  public static final String givenName=BD+mulHyp(givenNameComponent);    

  /** The pattern (personNameComponent+B)+ */
  public static final String givenNames=mul(givenName);
  
  /** Name component with an optional familyNamePrefix and postfix*/
  public static final String familyName=BD+mulHyp(opt(directFamilyNamePrefix)+personNameComponent)+BD;

  /** Nickname '...' */
  public static final String nickName="(?:'[^']')";

  /** The pattern for person names */
  public static final Pattern laxPersonNamePattern=Pattern.compile(
        c(optMul(title+B))+
        c(optMul(givenName+B))+
        opt(c(nickName)+B)+
        opt(c(attributePrefix)+B)+ 
        opt(c(familyNamePrefix)+B)+
        c(familyName)+
        opt(BC+c(familyNameSuffix))+ 
        opt(B+c(roman))+ 
        opt(B+of+B+c(personNameComponent))+
        opt(B+c(nickName)));        

  /** The pattern for strings that are person names with high probability*/
  public static final String safePersonName=
      // Mr. Bob Carl Miller
      title+B+givenNames+B+opt(familyNamePrefix+B)+familyName+opt(BC+familyNameSuffix)+or+
      // Mr. Miller 
      title+B+opt(familyNamePrefix+B)+familyName+opt(BC+familyNameSuffix)+or+
      // Bob XI
      givenName+B+roman+or+
      // Bob Miller Jr.
      givenNames+B+opt(familyNamePrefix+B)+familyName+BC+familyNameSuffix+or+
      // Miller Jr.
      opt(familyNamePrefix+B)+familyName+BC+familyNameSuffix+or+
      // George W. Bush
      givenName+B+U+"\\."+B+opt(familyNamePrefix+B)+familyName+opt(BC+familyNameSuffix)+or+
      // George H. W. Bush
      givenName+B+U+"\\."+B+U+"\\."+B+opt(familyNamePrefix+B)+familyName+opt(BC+familyNameSuffix);
      ;
  
  public static final Pattern safePersonNamePattern=Pattern.compile(safePersonName);
    
  /** Returns true if it is possible that the string is a person name */
  public static boolean couldBePersonName(String s) {
    if(isCompanyName(s)) return(false);  
    return(laxPersonNamePattern.matcher(s).matches());
  }

  /** Returns true if it is highly probable that the string is a person name.*/
  public static boolean isPersonName(String m) {    
    return(safePersonNamePattern.matcher(m).matches());
  }

  public static class PersonName extends Name {
    protected String myTitles;
    protected String myGivenNames;
    protected String myFamilyNamePrefix;
    protected String myAttributePrefix;
    protected String myFamilyName;
    protected String myAttribute;
    protected String myFamilyNameSuffix;
    protected String myRoman;
    protected String myCity;
    protected String myNickname;
    /** Returns the n-th group or null */
    protected static String getComponent(Matcher m, int n) {
      if(m.group(n)==null || m.group(n).length()==0) return(null);
      String result=m.group(n);      
      if(result.matches(".+"+B)) return(result.substring(0,result.length()-1));
      if(result.matches(B+".+")) return(result.substring(1));    
      return(result);        
    }    
    /** Constructs a person name from a String */
    public PersonName(String s) {
      super(s);
      s=s.replace('_', ' ');
      Matcher m=laxPersonNamePattern.matcher(s);
      if(!m.matches()) return;
      myTitles=getComponent(m, 1);
      myGivenNames=getComponent(m, 2);
      myNickname=getComponent(m,3);      
      myFamilyName=getComponent(m, 6);
      myFamilyNamePrefix=getComponent(m, 5);
      String attr=getComponent(m, 4);
      if(attr!=null) {             
          myAttributePrefix=attr;
          myAttribute=myFamilyName;
          myFamilyName=null;
      }
      myFamilyNameSuffix=getComponent(m,7);
      myRoman=getComponent(m,8);
      myCity=getComponent(m,9);
      if(myNickname==null) myNickname=getComponent(m, 10);
      // Postprocessing: If the title applies to given names, make the family name the given name
      if(myGivenNames==null && myTitles!=null && titlesForGivenNames.contains(myTitles.toLowerCase())) {
        myGivenNames=myFamilyName;
        myFamilyName=null;
      }
      // Postprocessing: If we have no given name and a roman number, familyname is given name
      if(myGivenNames==null && myRoman!=null) {
        myGivenNames=myFamilyName;
        myFamilyName=null;
      }
      // Postprocessing: if familyname is a familynamesuffix, rearrange
      if(myFamilyName!=null && myGivenNames!=null && familyNameSuffixPattern.matcher(myFamilyName).matches()) {
        String[] g=myGivenNames.split(B);
        myFamilyNameSuffix=myFamilyName;
        myFamilyName=g[g.length-1];
        myGivenNames=g.length==1?null:myGivenNames.substring(0,myGivenNames.length()-myFamilyName.length());
      }
    }
    /** Returns the first given name or null*/
    public String givenName() {
      if(myGivenNames==null) return(null);
      if(myGivenNames.indexOf(' ')==-1) return(myGivenNames);
      return(myGivenNames.substring(0, myGivenNames.indexOf(' ')));
    }      
    /**Returns the attribute.*/
    public String attribute() {
      return myAttribute;
    }
    /**Returns the attributePrefix.*/
    public String attributePrefix() {
      return myAttributePrefix;
    }
    /**Returns the city.*/
    public String city() {
      return myCity;
    }
    /**Returns the nickname.*/
    public String nickname() {
      return myNickname;
    }
    /**Returns the familyName. */
    public String familyName() {
      return myFamilyName;
    }
    /**Returns the familyNamePrefix.*/
    public String familyNamePrefix() {
      return myFamilyNamePrefix;
    }
    /**Returns the familyNameSuffix.*/
    public String familyNameSuffix() {
      return myFamilyNameSuffix;
    }
    /**Returns the givenNames.*/
    public String givenNames() {
      return myGivenNames;
    }
    /**Returns the roman number.*/
    public String roman() {
      return myRoman;
    }
    /**Returns the titles.*/
    public String titles() {
      return myTitles;
    }
    /** Normalizes a person name.*/
    public String normalize() {
      String given=givenNames();
      
      // Try the family name
      if(myFamilyName!=null) { 
        String family=myFamilyName;
        if(myFamilyNameSuffix!= null && myFamilyNameSuffix.matches("[jJ].*")) family+=", Jr.";
        else if(myFamilyNameSuffix!= null && myFamilyNameSuffix.matches("[sS].*")) family+=", Sr.";
        if(given!=null) family=given+' '+family;
        return(family);
      }
      
      // Try the given name
      if(given!=null) {
        if(myRoman!=null && given!=null) given+=' '+myRoman;
        if(myAttribute!=null && given!=null) given+=' '+myAttribute;
        return(given);
      }
      
      // Return original
      return(original());
    }   
    /** Returns a description */
    public String describe() {
      return("PersonName\n"+
             "  Original: "+original+"\n"+
             "  Titles: "+titles()+"\n"+
             "  Given Name: "+givenName()+"\n"+
             "  Given Names: "+givenNames()+"\n"+
             "  Nickname: "+nickname()+"\n"+             
             "  Family Name Prefix: "+familyNamePrefix()+"\n"+             
             "  Attribute Prefix: "+attributePrefix()+"\n"+                          
             "  Family Name: "+familyName()+"\n"+             
             "  Attribute: "+attribute()+"\n"+                          
             "  Family Name Suffix: "+familyNameSuffix()+"\n"+                          
             "  Roman: "+roman()+"\n"+                                       
             "  City: "+city()+"\n"+                                       
             "  Normalized: "+normalize());
    }    
    
  }
  
  //----------------------------------------------------------------------------
  //              Stop words 
  //----------------------------------------------------------------------------
  
  /** Contains stopwords */
  public static FinalSet<String> stopWords=new FinalSet<String>(new String[]{
   "a",
   "about",
   "above",
   "across",
   "after",
   "afterwards",
   "again",
   "against",
   "all",
   "almost",
   "alone",
   "along",
   "also",
   "although",
   "always",
   "am",
   "among",
   "amongst",
   "an",
   "and",
   "another",
   "any",
   "anybody",
   "anyhow",
   "anyone",
   "anything",
   "anyway",
   "anyways",
   "anywhere",
   "apart",
   "appear",
   "appreciate",
   "appropriate",
   "around",
   "as",
   "aside",
   "ask",
   "asking",
   "associated",
   "at",
   "available",
   "away",
   "awfully",
   "b",
   "because",
   "before",
   "beforehand",
   "behind",
   "being",
   "below",
   "beside",
   "besides",
   "best",
   "better",
   "between",
   "beyond",
   "both",
   "brief",
   "but",
   "by",
   "c",
   "c'mon",
   "c's",
   "cause",
   "certain",
   "certainly",
   "changes",
   "clearly",
   "co",
   "com",
   "concerning",
   "consequently",
   "corresponding",
   "course",
   "currently",
   "d",
   "definitely",
   "described",
   "despite",
   "different",
   "down",
   "downwards",
   "during",
   "e",
   "each",
   "edu",
   "eg",
   "eight",
   "either",
   "else",
   "elsewhere",
   "enough",
   "entirely",
   "especially",
   "et",
   "etc",
   "even",
   "ever",
   "every",
   "everybody",
   "everyone",
   "everything",
   "everywhere",
   "ex",
   "exactly",
   "example",
   "except",
   "f",
   "far",
   "few",
   "fifth",
   "first",
   "five",
   "for",
   "former",
   "formerly",
   "forth",
   "four",
   "from",
   "further",
   "furthermore",
   "g",
   "greetings",
   "h",
   "happens",
   "hardly",
   "he",
   "he's",
   "hello",
   "help",
   "hence",
   "her",
   "here",
   "here's",
   "hereafter",
   "hereby",
   "herein",
   "hereupon",
   "hers",
   "herself",
   "hi",
   "him",
   "himself",
   "his",
   "hither",
   "hopefully",
   "how",
   "howbeit",
   "however",
   "i",
   "i'd",
   "i'll",
   "i'm",
   "i've",
   "ie",
   "if",
   "immediate",
   "in",
   "inasmuch",
   "inc",
   "indeed",
   "inner",
   "insofar",
   "instead",
   "into",
   "inward",
   "is",
   "isn't",
   "it",
   "it'd",
   "it'll",
   "it's",
   "its",
   "itself",
   "j",
   "just",
   "k",
   "l",
   "last",
   "lately",
   "later",
   "latter",
   "latterly",
   "least",
   "less",
   "lest",
   "let",
   "let's",
   "likely",
   "little",
   "ltd",
   "m",
   "mainly",
   "many",
   "may",
   "maybe",
   "me",
   "mean",
   "meanwhile",
   "merely",
   "might",
   "more",
   "moreover",
   "most",
   "mostly",
   "much",
   "must",
   "my",
   "myself",
   "n",
   "name",
   "namely",
   "nd",
   "near",
   "nearly",
   "necessary",
   "need",
   "needs",
   "neither",
   "never",
   "nevertheless",
   "next",
   "nine",
   "no",
   "nobody",
   "non",
   "none",
   "noone",
   "nor",
   "normally",
   "not",
   "nothing",
   "novel",
   "now",
   "nowhere",
   "o",
   "obviously",
   "of",
   "off",
   "often",
   "oh",
   "ok",
   "okay",
   "old",
   "on",
   "once",
   "one",
   "ones",
   "only",
   "onto",
   "or",
   "other",
   "others",
   "otherwise",
   "ought",
   "our",
   "ours",
   "ourselves",
   "out",
   "outside",
   "over",
   "overall",
   "own",
   "p",
   "particular",
   "particularly",
   "per",
   "perhaps",
   "placed",
   "please",
   "plus",
   "possible",
   "presumably",
   "probably",
   "provides",
   "q",
   "que",
   "quite",
   "qv",
   "r",
   "rather",
   "rd",
   "re",
   "really",
   "reasonably",
   "regardless",
   "relatively",
   "respectively",
   "s",
   "same",
   "saw",
   "second",
   "secondly",
   "see",
   "seeing",
   "self",
   "selves",
   "sensible",
   "sent",
   "serious",
   "seriously",
   "seven",
   "several",
   "shall",
   "she",
   "since",
   "six",
   "so",
   "some",
   "somebody",
   "somehow",
   "someone",
   "something",
   "sometime",
   "sometimes",
   "somewhat",
   "somewhere",
   "soon",
   "sorry",
   "still",
   "sub",
   "such",
   "sup",
   "sure",
   "t",
   "t's",
   "th",
   "than",
   "thank",
   "thanks",
   "thanx",
   "that",
   "that's",
   "thats",
   "the",
   "their",
   "theirs",
   "them",
   "themselves",
   "then",
   "thence",
   "there",
   "there's",
   "thereafter",
   "thereby",
   "therefore",
   "therein",
   "theres",
   "thereupon",
   "these",
   "they",
   "they'd",
   "they'll",
   "they're",
   "they've",
   "think",
   "third",
   "this",
   "thorough",
   "thoroughly",
   "those",
   "though",
   "three",
   "through",
   "throughout",
   "thru",
   "thus",
   "to",
   "together",
   "too",
   "toward",
   "towards",
   "truly",
   "twice",
   "two",
   "u",
   "un",
   "under",
   "unfortunately",
   "unless",
   "unlikely",
   "until",
   "unto",
   "up",
   "upon",
   "us",
   "useful",
   "usually",
   "uucp",
   "v",
   "value",
   "various",
   "very",
   "via",
   "viz",
   "vs",
   "w",
   "way",
   "we",
   "we'd",
   "we'll",
   "we're",
   "we've",
   "well",
   "went",
   "what",
   "what's",
   "whatever",
   "when",
   "whence",
   "whenever",
   "where",
   "where's",
   "whereafter",
   "whereas",
   "whereby",
   "wherein",
   "whereupon",
   "wherever",
   "whether",
   "which",
   "while",
   "whither",
   "who",
   "who's",
   "whoever",
   "whole",
   "whom",
   "whose",
   "why",
   "willing",
   "wish",
   "with",
   "within",
   "without",
   "wonder",
   "x",
   "y",
   "yes",
   "yet",
   "you",
   "you'd",
   "you'll",
   "you're",
   "you've",
   "your",
   "yours",
   "yourself",
   "yourselves",
   "z",
   "zero",
   });
  
  /** TRUE for stopwords */
  public static boolean isStopWord(String w) {
    return(stopWords.contains(w));
  }
  
  //----------------------------------------------------------------------------
  //                   American States
  //----------------------------------------------------------------------------

  public static Map<String,String> usStates=new FinalMap<String, String>(
      "AL", "Alabama",
      "AK", "Alaska",
      "AS", "American Samoa",
      "AZ", "Arizona",
      "AR", "Arkansas",
      "CA", "California",
      "CALIF", "California",
      "CO", "Colorado",
      "CT", "Connecticut",
      "DE", "Delaware",
      "DC", "District of Columbia",
      "FM", "Federated States of Micronesia",
      "FL", "Florida",
      "GA", "Georgia",
      "GU", "Guam",
      "HI", "Hawaii",
      "ID", "Idaho",
      "IL", "Illinois",
      "IN", "Indiana",
      "IA", "Iowa",
      "KS", "Kansas",
      "KY", "Kentucky",
      "LA", "Louisiana",
      "ME", "Maine",
      "MH", "Marshall Islands",
      "MD", "Maryland",
      "MA", "Massachusetts",
      "MI", "Michigan",
      "MN", "Minnesota",
      "MS", "Mississippi",
      "MO", "Missouri",
      "MT", "Montana",
      "NE", "Nebraska",
      "NV", "Nevada",
      "NH", "New Hampshire",
      "NJ", "New Jersey",
      "NM", "New Mexico",
      "NY", "New York",
      "NC", "North Carolina",
      "ND", "North Dakota",
      "MP", "Northern Mariana Islands",
      "OH", "Ohio",
      "OK", "Oklahoma",
      "OR", "Oregon",
      "PW", "Palau",
      "PA", "Pennsylvania",
      "PR", "Puerto Rico",
      "RI", "Rhode Island",
      "SC", "South Carolina",
      "SD", "South Dakota",
      "TN", "Tennessee",
      "TX", "Texas",
      "UT", "Utah",
      "VT", "Vermont",
      "VI", "Virgin Islands",
      "VA", "Virginia",
      "WA", "Washington",
      "WV", "West Virginia",
      "WI", "Wisconsin",
      "WY", "Wyoming"      
  );
  
  /** Returns TRUE for US States*/
  public static boolean isUSState(String s) {
    return(usStates.values().contains(s.replace('_',' ')));
  }

  /** Returns the US sate for an abbreviation (or NULL) */
  public static String unabbreviateUSState(String s) {
    if(s.endsWith(".")) s=Char.cutLast(s);
    return(usStates.get(s.toUpperCase()));
  }

  //----------------------------------------------------------------------------
  //                   Main
  //----------------------------------------------------------------------------
  
  /** Factory pattern */
  public static Name of(String s) {
    if(isCompanyName(s)) return(new CompanyName(s));
    if(couldBePersonName(s)) return(new PersonName(s));
    if(isAbbreviation(s)) return(new Abbreviation(s));    
    return(new Name(s));
  }
  
  /** Test routine */
  public static void main(String[] argv) throws Exception {
    D.p(Name.of("Mr. Jr").describe());
//    for(String s :new FileLines("./testdata/NameParserTest.txt")) {            
//        D.p(Name.of(s).describe());
//    }    
  }
}
