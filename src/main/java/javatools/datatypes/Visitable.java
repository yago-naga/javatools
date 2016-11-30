package javatools.datatypes;

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

This interface is for the common visitor design pattern.
*/
public interface Visitable<T> {

  /** Sends a visitor through all elements. Returns whatever the visitor returned. */
  public boolean receive(Visitor<T> visitor) throws Exception;
}
