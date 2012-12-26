package com.jr.intel.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.jr.intel.DecisionTree;

/**
 * A decision tree implementation that supports online training.  This is useful for classification learning
 * that is adjusted based on user interaction (e.g. down-voting or marking an example as unimportant)
 * 
 * NOTE: this is currently just for boolean classification...eventually, I'll expand this to include other classification type
 * 
 * @author Jesse Rosalia
 *
 */
public class OnlineDecisionTree implements DecisionTree<Boolean> {

    private List<String> attributeNames;

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
        return 0;
    }

    @Override
    public void rebuild() {
        
    }

    @Override
    public Boolean classify(Map<String, ?> attributes) {
        //start at the top of the tree, pulling out the attributes for each level.  traverse until we find a classification and return it
        //..if the attributes map is incomplete, we can either throw an exception
        
        return null;
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
