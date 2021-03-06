/*******************************************************************************
 * Copyright 2012 Pearson Education
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.semantictools.index.api;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.semantictools.context.renderer.model.ContextProperties;
import org.semantictools.context.renderer.model.DocumentMetadata;
import org.semantictools.context.renderer.model.GlobalProperties;
import org.semantictools.context.renderer.model.ServiceDocumentation;
import org.semantictools.context.view.DefaultDocumentPrinter;
import org.semantictools.frame.api.LinkManager;
import org.semantictools.index.model.SchemaReference;
import org.semantictools.index.model.ServiceDocumentationList;

/**
 * Prints an index to all of the documentation artifacts including ontologies,
 * data types, rest services.
 * 
 * @author Greg McFall
 *
 */
public class LinkedDataIndexPrinter extends DefaultDocumentPrinter {
  
  private LinkedDataIndex index;
  private LinkManager linkManager;
  private File indexFile;
  
  
  public LinkedDataIndexPrinter(File indexFile, LinkedDataIndex index) {
    super();
    this.indexFile = indexFile;
    File pubDir = indexFile.getParentFile();
    this.index = index;
    linkManager = new LinkManager();
    linkManager.setBaseURI(pubDir.toString().replace("\\", "/") + "/");
    pubDir.mkdirs();
    
    DocumentMetadata metadata = new GlobalProperties();
    metadata.setCss("uml/uml.css");
    setMetadata(metadata);
  }
  
  public void printIndex() throws IOException {
    clear();
    
    beginHTML();
    pushIndent();
    printOntologies();
    printDatatypes();
    printServices();
    printMediaTypes();
    popIndent();    
    endHTML();
    
    writeFile();
    
  }




  private void printMediaTypes() {
    List<ContextProperties> list = index.listAllMediaTypes();
    Collections.sort(list);
    
    indent();
    println("<H2>Media Types</H2>");
    indent();
    println("<UL>");
    pushIndent();
    for (ContextProperties context : list) {

      String mediaType = context.getMediaType();
      String mediaTypeHref = linkManager.relativize(context.getMediaTypeDocFile());
      print("<LI>");
      printAnchor(mediaTypeHref, mediaType);
      println();
      
    }
    
    popIndent();
    indent();
    println("</UL>");
    
  }

  private void printDatatypes() {

    List<SchemaReference> list = index.listDatatypes();
    
    indent();
    println("<H2>Data Types</H2>");
    indent();
    println("<UL>");
    pushIndent();
    for (SchemaReference r : list) {
      String label = r.getSchemaLabel();
      String href = linkManager.relativize(r.getSchemaDocPath());
      indent();
      print("<LI>");
      printAnchor(href, label);
      println();
    }
    popIndent();
    indent();
    println("</UL>");
    
  }

  private void printOntologies() {
    
    List<SchemaReference> list = index.listOntologies();
    Collections.sort(list);
    
    indent();
    println("<H2>Ontologies</H2>");
    indent();
    println("<UL>");
    pushIndent();
    for (SchemaReference r : list) {
      String label = r.getSchemaLabel();
      String href = linkManager.relativize(r.getSchemaDocPath());
      indent();
      print("<LI>");
      printAnchor(href, label);
      println();
    }
    popIndent();
    indent();
    println("</UL>");
    
  }

  private void writeFile() throws IOException {
    
    FileWriter writer = new FileWriter(indexFile);
    try {
      String text = popText();
      writer.write(text);
      writer.flush();
    } finally {
      writer.close();
    }
    
  }

  private void printServices() {

    List<ServiceDocumentationList> megaList = index.listServices();
    if (megaList.isEmpty()) return;
    
    indent();
    println("<H2>REST Services</H2>");
    indent();
    println("<UL>");
    pushIndent();
    
    Collections.sort(megaList, new Comparator<ServiceDocumentationList>() {
      @Override
      public int compare(ServiceDocumentationList a,  ServiceDocumentationList b) {
        return a.getRdfTypeLocalName().compareTo(b.getRdfTypeLocalName());
      }
    });
    
    
    for (ServiceDocumentationList list : megaList) {
      switch (list.size()) {
      case 0 : 
        // Do nothing
        break;
        
      case 1:
        printServiceDocumentation(list.getRdfTypeLocalName(), list.get(0));
        break;
        
      default :
        printMultipleServicesForType(list);
        break;
      }
      
    }
    
    
    popIndent();
    println("</UL>");
    
    
  }


  private void printServiceDocumentation(String rdfTypeLocalName,  ServiceDocumentation serviceDocumentation) {
    print("<LI> ");
    String href = linkManager.relativize(serviceDocumentation.getServiceDocumentationFile());
    printAnchor(href, rdfTypeLocalName + " Service");
    println();
//    pushIndent();
//    print("<DIV ");
//    printAttr("class", "mediatype");
//    println(">");
//    pushIndent();
//    for (ContextProperties context : serviceDocumentation.listContextProperties()) {
//      String mediaType = context.getMediaType();
//      String mediaTypeHref = linkManager.relativize(context.getMediaTypeDocFile());
//      print("<DIV>");
//      printAnchor(mediaTypeHref, mediaType);
//      println("</DIV>");
//    }
//    
//    popIndent();
//    println("</DIV>");
    
    popIndent();
  }
  

  private void printMultipleServicesForType(ServiceDocumentationList list) {
    // TODO Auto-generated method stub
    
  }


}
