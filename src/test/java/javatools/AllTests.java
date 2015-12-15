package javatools;

import javatools.datatypes.DateParserTest;
import javatools.datatypes.MultiMapTest;
import javatools.parsers.NumberParserTest;
import javatools.util.ArrayUtilsTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/** A test suite containing all unit-tests of the project */

@RunWith(Suite.class)
@SuiteClasses({DateParserTest.class,MultiMapTest.class,NumberParserTest.class, ArrayUtilsTest.class})
public class AllTests {

}
