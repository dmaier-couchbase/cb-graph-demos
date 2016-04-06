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

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a Wiki article
 * 
 * @author david
 */
public class WikiArticle {
    
    private String title;
    private String id;
    private String text;
    private String ref;
    
    //Derived from the text
    private List<WikiArticle> links = new ArrayList<>();
    
    //Derived from the text
    private Map<String,String> props = new HashMap<>();

    public WikiArticle() {
    }

    public WikiArticle(String title, String id, String text) {
        this.title = title;
        this.id = id;
        this.text = text;
       
    }
    
    public boolean isEmpty() {
        
        return this.id == null || this.title == null;
    }
     
    /**
     * Parse the title to a reference
     * @return 
     * @throws java.lang.Exception
     */
    public String parseTitle() throws Exception {
       
       //Mediawiki uses URL encoding by then replacing some chars again
       //https://doc.wikimedia.org/mediawiki-core/master/php/GlobalFunctions_8php_source.html
       //wfUrlencode
              
        //String encTitle = URLEncoder.encode(this.title, "UTF-8");
        
        URI uri = new URI("http", "localhost", "/" + title, null);
        String encTitle = uri.toURL().getFile().toString().substring(1);
        
        Map<String,String> needle = new HashMap<>();
        needle.put("%22", "\"");
        needle.put("%20", "_");
        needle.put("%3B", ";");
        needle.put("%40", "@");
        needle.put("%24", "$");
        needle.put("%21", "!");
        needle.put("%2A", "*");
        needle.put("%28", "(");
        needle.put("%29", ")");
        needle.put("%2C", ",");
        needle.put("%2F", "/");
        needle.put("%7E", "~");
        needle.put("%3A", ":");
       
        for (Map.Entry<String, String> entry : needle.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            encTitle = encTitle.replace(key, value);
            
        }
        
       this.ref = encTitle;
       
       return ref;
    }
    
    /**
     * The text might contain links to other entries.
     * 
     * @return 
     */
    public List<WikiArticle> parseLinks() throws Exception {
        
        /* Internal links
        
           * [[Main Page]]
           * [[Main Page|different text]]
          
            The following links are ignored
         
           * [[Extension:DynamicPageList (Wikimedia)]]
           * [[Main Page#Concrete_Paragraph|different text2]]
           * [[Help:Contents|]]
           * [[#See also|different text]]
           * [[/example]]
           * [[:Category:Help]]
           * [[media:example.jpg]]
          
         --> The first part doesn't contain a ':', '#' or a '/'
         --> The second part is not allowed to be empty
        */
        
        Pattern p = Pattern.compile("\\[\\[.*?\\]\\]", Pattern.DOTALL);
        Matcher m = p.matcher(text);


        while (m.find()) {
            
           String found = m.group(); 
           String[] components = found.split("\\|");  
           String link = components[0];
           
           if (!link.contains("#") && !link.contains(":") && !link.contains("/")) {
               
               //Remove leading and tailing square brackets
               if (link.startsWith("[[")) link = link.substring(2);
               if (link.endsWith("]]")) link = link.substring(0, link.length()-2);
               
               WikiArticle linkedArticle = new WikiArticle();
               linkedArticle.setTitle(link);
               linkedArticle.parseTitle();
               
               this.links.add(linkedArticle);
           }
           
        }
        
        return null;
    }
    
    
    public void parseProps() {
        
        
        /* A character contains the following property list
        {{Character
        |name = Mark Brunell
        |image = 
        |gender = {{Male}}
        |hair = 
        |age = 
        |status = Alive
        |job = Football player
        |relatives = 
        |appearance = ''[[Flanders' Book of Faith]]''
        |voiced by = 
        }}
        */
  
        Pattern p = Pattern.compile("\\{\\{.*?\\}\\}", Pattern.DOTALL);
        
        Matcher m = p.matcher(text);
        
        while (m.find()) {
            
           String found = m.group(); 
         
           if (found.contains("|age") && found.contains("|gender") && found.contains("|status") && found.contains("|job")) {
               
               String[] foundProps = found.split("\\|");
               
               for (int i = 0; i < 10; i++) {
                   
                   String prop = foundProps[i];
                   
                   if (!prop.contains("{{") && !prop.contains("}}")) {
                
                       String[] pair = prop.split("=");
                       
                       String key = pair[0].trim();
                       String val = pair[1].trim();
                       
                       if (!val.equals("")) {
                        
                           this.props.put(key, val);
                       }
                   }
               }
           }
        }
    
    }
    
    
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the links
     */
    public List<WikiArticle> getLinks() {
        return links;
    }

    /**
     * @param links the links to set
     */
    public void setLinks(List<WikiArticle> links) {
        this.links = links;
    }
    
    /**
     * Get the reference of this entry
     * @return 
     */
    public String getRef() {
        return ref;
    }
  
    
}
