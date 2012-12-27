package com.jr.intel.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jr.intel.AmbiguousClassificationException;
import com.jr.intel.DecisionTree;
import com.jr.intel.UnknownClassificationException;

/**
 * A decision tree implementation that supports online training.  This is useful for classification learning
 * that is adjusted based on user interaction (e.g. down-voting or marking an example as unimportant)
 * 
 * Terminology:
 *  Attribute - a named value that is part of a 
 *  
 * Implementation notes:
 * 
 *  This implementation stores each attribute as a separate map, which maps the name of the attribute to
 *  a map of attribute values to possible classifications.  To add new training data, we iterate through
 *  the training attributes and for each attribute, add the classification as a possible classification.
 *  To classify a set of data, we look up each attribute value and find the intersection of the set of all
 *  possible classifications.  If it is possible to classify the set of attributes, at least one of the
 *  attributes will contain one and only one possible classification, which means the intersection will
 *  contain just that one classification.
 *  
 *  NOTE: it is possible to have unknown classifications (where we have no training data at all to support
 *  the attributes passed in), or ambiguous classifications (where we do not have enough training data
 *  for the set of attributes to properly classify it).  Both of these cases will throw an exception to
 *  allow the caller to handle them as they need to.
 *  
 * NOTE: this is currently just for boolean classification...eventually, I'll expand this to include other classification type
 * 
 * @author Jesse Rosalia
 *
 */
public class OnlineDecisionTree implements DecisionTree<Boolean> {

    private List<String> attributeNames;
    
    private Map<String, Map<Object, Set<Boolean>>> maps = new HashMap<String, Map<Object, Set<Boolean>>>();

    public OnlineDecisionTree(List<String> attributeNames) {
        this.attributeNames = attributeNames;
    }

    @Override
    public int add(Map<String, ?> attributes, Boolean classification) {
        //strategy: I think we want to build the tree backwards.  for each classification, on the first call to add, we build a tree with the attributes
        // passed in.  On the second call to add for that classification, we look for attributes that are the same...these attributes are the ones
        // that matter for classification.  Bubble these to the top and then wait for the next set of attributes.  We can achieve the same speed as a d-tree built
        // using the greedy algorithm by building "shortcuts" into our nodes.  This means that while we keep the whole tree around (to accommodate new evidence later)
        // we will not in practice traverse the whole tree unless we absolutely have to.
        
        for (String attName : this.attributeNames) {
            Map<Object, Set<Boolean>> attMap = this.maps.get(attName);
            if (attMap == null) {
                attMap = new HashMap<Object, Set<Boolean>>();
                this.maps.put(attName, attMap);
            }
            
            Object val = attributes.get(attName);
            Set<Boolean> possibilities = attMap.get(val);
            if (possibilities == null) {
                possibilities = new HashSet<Boolean>();
                attMap.put(val, possibilities);
            }
            possibilities.add(classification);
        }
        //TODO: reorder attributeNames so we get the quickest classification
        return 0;
    }

    @Override
    public void rebuild() {
        
    }

    @Override
    public Boolean classify(Map<String, ?> attributes) throws UnknownClassificationException, AmbiguousClassificationException {
        //start at the top of the tree, pulling out the attributes for each level.  traverse until we find a classification and return it
        //..if the attributes map is incomplete, we can either throw an exception
        
        Boolean retVal = null;
        Set<Boolean> classification = new HashSet<Boolean>();
        for (String attName : this.attributeNames) {
            Map<Object, Set<Boolean>> attMap = this.maps.get(attName);
            if (attMap == null) {
                throw new UnknownClassificationException(attributes);
            }
            
            Object val = attributes.get(attName);
            Set<Boolean> possibilities = attMap.get(val);
            if (possibilities == null) {
                continue;
//                //something is terribly wrong...real exception TBD
//                throw new RuntimeException();
            }
            if (classification.isEmpty()) {
                classification.addAll(possibilities);
            } else {
                classification.retainAll(possibilities);
            }
            //if we've zero'd in on the one class, break out...no need to search further
            if (classification.size() == 1) {
                retVal = classification.iterator().next();
                break;
            }
        }
        
        if (retVal == null) {
            throw new AmbiguousClassificationException(attributes, classification);
        }
        return retVal;
    }

    @Override
    public void save(OutputStream os) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void load(InputStream is) {
        // TODO Auto-generated method stub
        
    }

}
