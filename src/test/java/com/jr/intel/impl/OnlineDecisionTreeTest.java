package com.jr.intel.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jr.intel.AmbiguousClassificationException;
import com.jr.intel.DecisionTree;

public class OnlineDecisionTreeTest {

    @Test
    public void testClassifyWaterFowl() throws Exception {
        List<String> attributes = new ArrayList<String>();
        attributes.add("covering");
        attributes.add("sound");
        attributes.add("swim");
        attributes.add("fly");
        DecisionTree<Boolean> dt = new OnlineDecisionTree(attributes);
        
        //first, train and test with the same data to make sure we get back what we put in
        
        Map<String, Object> duck = new HashMap<String, Object>();
        duck.put("covering", "feathers");
        duck.put("sound",    "quack");
        duck.put("swim",     true);
        duck.put("fly",      false);
        dt.add(duck, true);

        assertTrue(dt.classify(duck));

        Map<String, Object> goose = new HashMap<String, Object>();        
        goose.put("covering", "feathers");
        goose.put("sound",    "honk");
        goose.put("swim",     true);
        goose.put("fly",      true);
        dt.add(goose, true);

        assertTrue(dt.classify(duck));
        assertTrue(dt.classify(goose));

        Map<String, Object> cow = new HashMap<String, Object>();  
        cow.put("covering", "leather");
        cow.put("sound",    "moo");
        cow.put("swim",     false);
        cow.put("fly",      false);        
        dt.add(cow, false);

        assertTrue(dt.classify(duck));
        assertTrue(dt.classify(goose));
        assertFalse(dt.classify(cow));

        Map<String, Object> dog = new HashMap<String, Object>();        
        dog.put("covering", "fur");
        dog.put("sound",    "bark");
        dog.put("swim",     true);
        dog.put("fly",      false);
        dt.add(dog, false);

        assertTrue(dt.classify(duck));
        assertTrue(dt.classify(goose));
        assertFalse(dt.classify(cow));
        assertFalse(dt.classify(dog));
        
        //next, try some other animals...based on the above classification, chicken will 
        // be considered a water fowl...
        Map<String, Object> chicken = new HashMap<String, Object>();        
        chicken.put("covering", "feathers");
        chicken.put("sound",    "cluck");
        chicken.put("swim",     false);
        chicken.put("fly",      false);

        assertTrue(dt.classify(chicken));
        //but we know it isnt, so we add this as a classification and test it
        dt.add(chicken, false);
        assertFalse(dt.classify(chicken));
        
        //and lets test rooster too, to make sure adding chicken will generalize to roosters
        Map<String, Object> rooster = new HashMap<String, Object>();        
        rooster.put("covering", "feathers");
        rooster.put("sound",    "crow");
        rooster.put("swim",     false);
        rooster.put("fly",      false);

        assertFalse(dt.classify(rooster));

        //a water animal (can swim) but definitely not a water fowl
        Map<String, Object> fish = new HashMap<String, Object>();        
        fish.put("covering", "scales");
        fish.put("sound",    "none");
        fish.put("swim",     true);
        fish.put("fly",      false);

        //NOTE: this will be ambiguous, because swim=true and fly=false can be either class=true or class=false
        try {
            assertFalse(dt.classify(fish));
            fail("Expected AmbiguousClassificationException");
        } catch (AmbiguousClassificationException e) {
            //add it to the decision tree and test it
            dt.add(fish, false);
            assertFalse(dt.classify(fish));
        }

        //penguins are, for the purposes of this test, water fowl
        //...but this will classify as false because hair is an unknown attribute value
        Map<String, Object> penguin = new HashMap<String, Object>();        
        penguin.put("covering", "hair");
        penguin.put("sound",    "none");
        penguin.put("swim",     true);
        penguin.put("fly",      false);
        assertFalse(dt.classify(penguin));
        dt.add(penguin, true);
        assertTrue(dt.classify(penguin));

        //test them all again just to make sure we didnt break anything
        assertTrue(dt.classify(duck));
        assertTrue(dt.classify(goose));
        assertFalse(dt.classify(cow));
        assertFalse(dt.classify(dog));
        assertFalse(dt.classify(chicken));
        assertFalse(dt.classify(rooster));
        assertFalse(dt.classify(fish));
        assertTrue(dt.classify(penguin));
    }

    @Test
    public void testClassifyTestCase1() throws Exception {
        List<String> attributeNames = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        
        Integer[][] data1TrainingExamples = {{0,1,1,0,0,1,0,1,0,1},                                  
                                             {0,0,1,1,0,1,1,0,0,1},                                  
                                             {1,1,1,1,0,1,0,0,1,0},                                  
                                             {1,0,1,0,1,0,1,1,1,1},                                  
                                             {0,0,1,0,0,0,0,0,0,1},                                  
                                             {1,0,0,0,1,0,0,0,0,0},                                  
                                             {1,0,0,0,0,1,1,1,1,0},                                  
                                             {1,0,0,0,0,0,1,0,1,0},                                  
                                             {1,1,0,1,1,1,1,1,1,1},                                  
                                             {0,0,0,0,1,1,0,1,1,0},                                  
                                             {1,0,1,0,1,1,0,0,0,1},                                  
                                             {1,0,0,1,1,1,1,0,1,1},                                  
                                             {1,1,0,1,0,1,0,0,0,0},                                  
                                             {0,1,0,0,1,0,1,0,1,1},                                  
                                             {1,0,0,0,0,1,1,1,1,1},                                  
                                             {0,1,0,1,1,0,1,1,1,0},                                  
                                             {0,0,0,1,0,0,1,1,1,0},                                  
                                             {1,0,1,1,0,0,0,0,0,1},                                  
                                             {0,1,1,0,1,0,0,1,0,0},                                  
                                             {1,0,1,0,0,0,1,1,1,0}};
        
        Integer[] data1TrainingLabels     = {0,0,0,1,1,1,0,1,0,0,0,0,0,1,0,1,1,1,1,1};
        
        //build the decision tree completely from the training examples
        DecisionTree<Boolean> dt = new OnlineDecisionTree(attributeNames);
        for (int ii = 0; ii < data1TrainingExamples.length; ii++) {
            Integer[] example = data1TrainingExamples[ii];
            Map<String, Integer> map = new HashMap<String, Integer>();
            for (int jj = 0; jj < example.length; jj++) {
                map.put(attributeNames.get(jj), example[jj]);
            }
            Boolean classification = data1TrainingLabels[ii] == 1;
            dt.add(map, classification);
        }
        
        Integer[][] data1TestExamples = {{0,1,0,0,0,0,1,1,0,0},                                      
                                         {1,1,1,1,1,1,0,1,1,0},                                      
                                         {0,1,0,0,0,0,1,0,1,0},                                      
                                         {1,1,0,1,0,0,1,0,0,1},                                      
                                         {0,1,1,1,0,1,0,0,1,1},                                      
                                         {0,0,0,0,0,0,1,0,0,1},                                      
                                         {1,1,1,1,0,0,1,0,0,1},                                      
                                         {1,1,0,0,1,0,1,1,1,0},                                      
                                         {0,1,0,0,0,0,0,0,1,0},                                      
                                         {0,0,1,1,0,1,1,1,0,1},                                      
                                         {1,0,0,1,0,0,0,0,0,1},                                      
                                         {0,1,1,1,1,1,0,1,1,1},                                      
                                         {0,1,0,0,0,0,1,0,0,0},                                      
                                         {1,1,0,0,1,0,1,1,0,1},                                      
                                         {1,1,1,1,0,1,0,1,1,1},                                      
                                         {1,0,0,1,1,1,1,0,0,1},                                      
                                         {1,1,1,0,1,1,1,1,1,1},                                      
                                         {0,1,1,0,1,1,1,1,0,0},                                      
                                         {1,0,1,1,0,1,0,0,1,0},                                      
                                         {1,0,0,1,0,1,0,0,1,1}};
                                                                                         
        Integer[] data1TestLabels = {1,0,1,1,0,1,1,1,1,0,1,0,1,1,0,0,0,0,0,0};

        //test the decision tree with the test data defined above
        for (int ii = 0; ii < data1TestExamples.length; ii++) {
            Integer[] test = data1TestExamples[ii];
            Map<String, Integer> map = new HashMap<String, Integer>();
            for (int jj = 0; jj < test.length; jj++) {
                map.put(attributeNames.get(jj), test[jj]);
            }
            Boolean classification = data1TestLabels[ii] == 1;
            assertEquals(classification, dt.classify(map));
        }

    }

    @Test
    public void testSaveAndLoad() {
        fail("Not yet implemented");
    }

}
