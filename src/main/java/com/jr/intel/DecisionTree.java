package com.jr.intel;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;


/**
 * 
 * 
 * @author thejenix
 *
 * @param <C>
 */
public interface DecisionTree<C> {

    /**
     * 
     * 
     * @param attributes
     * @param classification
     * @return
     */
    public int add(Map<String, ?> attributes, C classification);
    
    public void rebuild();
    
    public C classify(Map<String, ?> attributes);
    
    public void save(OutputStream os);
    
    public void load(InputStream is);
}
