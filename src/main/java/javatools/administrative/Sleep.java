package javatools.administrative;

import javatools.parsers.NumberFormatter;
import javatools.parsers.NumberParser;

/** 
Copyright 2016 Fabian M. Suchanek

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 


Sleeps for a given time
*/

public class Sleep {

  /** Sleeps for a given time
  * @throws InterruptedException 
  * @throws NumberFormatException */
  public static void sleep(String time) throws NumberFormatException, InterruptedException {
    sleep((long) Double.parseDouble(NumberParser.getNumber(NumberParser.normalize(time))));
  }

  /** Sleeps for a given time */
  public static void sleep(long seconds) throws InterruptedException {
    Announce.progressStart("Sleeping " + NumberFormatter.formatMS(seconds * 1000), seconds);
    for (long slept = 0; slept < seconds; slept++) {
      Announce.progressAt(slept);
      Thread.sleep(1000);
    }
    Announce.progressDone();
  }

  /** Sleeps for a given time */
  public static void main(String[] args) throws Exception {
    if (args == null || args.length == 0) {
      Announce.help("Sleep for a given time. E.g. 'sleep 3h 5min'");
    }
    String time = "";
    for (int i = 0; i < args.length; i++)
      time += " " + args[i];
    sleep(time);
  }

}
