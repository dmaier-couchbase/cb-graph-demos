package com.couchbase.graph.socialdemo.wiki;

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

import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author david
 */
public class WikiXMLParserTest {
    

    @Test
    public void test() throws Exception {
    
        String TEST_FILE = "/home/david/Git/dmaier-couchbase/cb-graph-demos/socialdemo/resources/Wikisimpsons-20160406063540.xml";
    
        WikiXMLParser parser = new WikiXMLParser(TEST_FILE);
        
        Map<String, WikiArticle> entries = parser.parse();
        
        entries.entrySet().stream().map((entry) -> entry.getValue()).forEach((value) -> {
            System.out.println(value.getRef());
        });   
        
    }
}
