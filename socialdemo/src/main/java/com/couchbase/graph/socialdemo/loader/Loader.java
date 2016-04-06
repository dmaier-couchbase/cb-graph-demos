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
package com.couchbase.graph.socialdemo.loader;

import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.graph.CBGraph;
import com.couchbase.graph.socialdemo.wiki.WikiArticle;
import com.couchbase.graph.socialdemo.wiki.WikiXMLParser;
import com.tinkerpop.blueprints.Vertex;
import java.util.List;
import java.util.Map;

/**
 * Load a Wiki XML file via CBGraph
 * 
 * @author david
 */
public class Loader {
   
    private final CBGraph target;
    private final String source;

    /**
     * 
     * @param source
     * @param target 
     */
    public Loader(String source, CBGraph target) {
        this.target = target;
        this.source = source;
    }
    
    public void load() throws Exception {
     
        WikiXMLParser parser = new WikiXMLParser(source);
        Map<String, WikiArticle> articles = parser.parse();
        
        //Load all entries as skeletons
        articles.entrySet().stream().map((entry) -> entry.getKey()).forEach((vId) -> {           
            
            WikiArticle a = articles.get(vId);
            
            Vertex v = target.addVertex(vId);
            v.setProperty("title", a.getTitle());
            v.setProperty("ref", a.getRef());
        });
        
        //Add the relationships from article links
        articles.entrySet().stream().map((entry) -> entry.getKey()).forEach((vId) -> {

            WikiArticle a = articles.get(vId);
            Vertex v = target.getVertex(vId);
            
            List<WikiArticle> refs = a.getLinks();
            
            for (WikiArticle ref : refs) {
                
                Vertex vRef = target.getVertex(ref.getRef());
                
                if (vRef != null) {
                    
                    try {
                        target.addEdge(null, v, vRef, "relatedTo");
                    
                    } catch (DocumentAlreadyExistsException e) {
                        
                        //Ignore
                    }
                }
            }
        });
        
        //Add properties from the table 
       
        
    }
}
