/*
 * Copyright 2016 david.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.couchbase.graph.socialdemo.wiki;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parses the exported Wiki XML by returning the entries
 * 
 * @author david
 */
public class WikiXMLParser {
    
    private final File xmlFile;
    
    private final Map<String, WikiArticle> entries = new HashMap<>();
    
    public WikiXMLParser(String xmlFileLoc) {
        
        this.xmlFile = new File(xmlFileLoc);
    }
   
    /**
     * Parse the entries in the XML file
     * @return 
     * @throws java.lang.Exception 
     */
    public Map<String,WikiArticle> parse() throws Exception {
        
        DocumentBuilder dB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        
        Document doc = dB.parse(xmlFile);
       
        
        NodeList allPages = doc.getElementsByTagName("page");
                
        for (int i = 0; i < allPages.getLength(); i++) {

            WikiArticle e = new WikiArticle();
            
            Node page = allPages.item(i);
            
            NodeList pageAttribs = page.getChildNodes();
            
            for (int j = 0; j < pageAttribs.getLength(); j++) {
                
                Node pageAttr = pageAttribs.item(j);
                String attrName = pageAttr.getNodeName();
                String attrValue = pageAttr.getTextContent();
                
                if (attrName.equals("id")) {
                    
                    e.setId(attrValue);
                }
                
                if (attrName.equals("title")) {
                    
                    e.setTitle(attrValue);
                }
                
                if (attrName.equals("revision")) {
                 
                    NodeList revAttribs = pageAttr.getChildNodes();
                    
                    for (int k = 0; k < revAttribs.getLength(); k++) {
                        
                        Node revAttr = revAttribs.item(k);
                        String revAttrName = revAttr.getNodeName();
                        String revAttrVal = revAttr.getTextContent();
                        
                        if (revAttrName.equals("text")) {
                            
                            e.setText(revAttrVal);
                        }             
                    }
                }
            }
            
            if (!e.isEmpty()) {
                
                String eTitle = e.parseTitle();
                e.parseLinks();
                
                //TODO: Test
                //e.parseProps();
                
                this.entries.put(eTitle, e);
            }
        }
        
        return entries;
        
    }

    public File getXmlFile() {
        return xmlFile;
    }

    public Map<String, WikiArticle> getEntries() {
        return entries;
    }
    
    
    
    
}
