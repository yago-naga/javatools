package javatools.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import javatools.datatypes.PeekIterator;

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
 

  This class wraps a ResultSet into an Iterator over a given Class.
  It requires a method that can wrap a row of a ResultSet into an object 
  of the given class.<BR>
  Example:
  <PRE>
  // We need this class to define how to construct an Employer from a table row
  public static class EmployerWrapper implements ResultWrapper&lt;Employer&gt; {
  
     // Wraps the current row in a ResultSet into an Employer
     public Employer wrap(ResultSet r) {  
       return(new Employer(r.getString(1),r.getDouble(2)); 
     }
     
  }
  
  Database d=new OracleDatabase("scott","tiger");
  for(Employer e : d.query("SELECT * FROM employers WHERE salary&gt;1000",
                           new EmployerConstructor())) {
     System.out.println(e);
  }
  </PRE>
 */
public class ResultIterator<T> extends PeekIterator<T> {

  /** Wraps the current row in a ResultSet into a T*/
  public static interface ResultWrapper<T> {

    /** Wraps the current row in a ResultSet into a T*/
    public T wrap(ResultSet r) throws Exception;
  }

  /** Holds the resultSet*/
  protected ResultSet resultSet;

  /** Holds the constructor to be used for each row */
  protected ResultWrapper<T> constructor;

  /** Creates a ResultIterator for a ResultSet*/
  public ResultIterator(ResultSet s, ResultWrapper<T> cons) {
    resultSet = s;
    constructor = cons;
  }

  /** For subclasses*/
  protected ResultIterator() {
  }

  @Override
  public T internalNext() throws Exception {
    if (!resultSet.next()) return (null);
    return (constructor.wrap(resultSet));
  }

  /** Closes the resultset and the underlying statement*/
  @Override
  public void close() {
    Database.close(resultSet);
  }

  /** Closes the resultset */
  @Override
  public void finalize() {
    close();
  }

  /** ResultWrapper for a single Boolean column */
  public static final ResultWrapper<Boolean> BooleanWrapper = new ResultWrapper<Boolean>() {

    @Override
    public Boolean wrap(ResultSet r) throws SQLException {
      return r.getBoolean(1);
    }
  };

  /** ResultWrapper for a single String column */
  public static final ResultWrapper<String> StringWrapper = new ResultWrapper<String>() {

    @Override
    public String wrap(ResultSet r) throws SQLException {
      return (r.getString(1));
    }
  };

  /** ResultWrapper for String columns */
  public static final ResultWrapper<String[]> StringsWrapper = new ResultWrapper<String[]>() {

    @Override
    public String[] wrap(ResultSet r) throws SQLException {
      String[] result = new String[r.getMetaData().getColumnCount()];
      for (int i = 0; i < result.length; i++)
        result[i] = r.getString(i + 1);
      return (result);
    }
  };

  /** ResultWrapper for a single Long column. Returns NULL for NULL */
  public static final ResultWrapper<Long> LongWrapper = new ResultWrapper<Long>() {

    @Override
    public Long wrap(ResultSet r) throws SQLException {
      long l = r.getLong(1);
      return (r.wasNull() ? null : l);
    }
  };

  /** ResultWrapper for a single Double column. Returns NULL for NULL */
  public static final ResultWrapper<Double> DoubleWrapper = new ResultWrapper<Double>() {

    @Override
    public Double wrap(ResultSet r) throws SQLException {
      double l = r.getDouble(1);
      return (r.wasNull() ? null : l);
    }
  };

  /** ResultWrapper for several Doubles. Returns NULL for NULL */
  public static final ResultWrapper<Double[]> DoublesWrapper = new ResultWrapper<Double[]>() {

    @Override
    public Double[] wrap(ResultSet r) throws SQLException {
      Double[] result = new Double[r.getMetaData().getColumnCount()];
      for (int i = 0; i < result.length; i++) {
        result[i] = r.getDouble(i + 1);
        if (r.wasNull()) result[i] = null;
      }
      return (result);
    }
  };

  /** ResultWrapper for a single Integer column */
  public static final ResultWrapper<Integer> IntegerWrapper = new ResultWrapper<Integer>() {

    @Override
    public Integer wrap(ResultSet r) throws SQLException {
      int l = r.getInt(1);
      return (r.wasNull() ? null : l);
    }
  };

  /** ResultWrapper for several Integers. Returns NULL for NULL */
  public static final ResultWrapper<Integer[]> IntegersWrapper = new ResultWrapper<Integer[]>() {

    @Override
    public Integer[] wrap(ResultSet r) throws SQLException {
      Integer[] result = new Integer[r.getMetaData().getColumnCount()];
      for (int i = 0; i < result.length; i++) {
        result[i] = r.getInt(i + 1);
        if (r.wasNull()) result[i] = null;
      }
      return (result);
    }
  };
}
