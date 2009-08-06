#!/usr/bin/env groovy

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collections;

def collator  = (RuleBasedCollator) Collator.getInstance(new Locale("en", "US", ""));

def list = new ArrayList();
System.in.eachLine() { line ->  
    list.add( line.trim() )
}  

Collections.sort( list, collator );

list.each() { item ->
    println item;
}
