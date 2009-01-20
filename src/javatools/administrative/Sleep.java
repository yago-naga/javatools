package javatools.administrative;
import javatools.parsers.NumberFormatter;
import javatools.parsers.NumberParser;


public class Sleep {

  public static void sleep(String time) throws Exception {
    long seconds=(long)Double.parseDouble(NumberParser.getNumber(NumberParser.normalize(time)));
    Announce.progressStart("Sleeping "+NumberFormatter.formatMS(seconds*1000),seconds);
    for(long slept=0;slept<seconds;slept++) {
      Announce.progressAt(slept);    
      Thread.sleep(1000);
    } 
    Announce.progressDone();
  }

  
  /**  */
  public static void main(String[] args) throws Exception {
    if(args==null || args.length==0) {
      Announce.help("Sleep for a given time. E.g. 'sleep 3h 5min'");
    }
    String time="";
    for(int i=0;i<args.length;i++) time+=" "+args[i];
    sleep(time);
  }

}
