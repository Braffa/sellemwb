package com.braffa.sellemwb.cucumbertest;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
 
@RunWith(Cucumber.class)
@CucumberOptions(
features = "com.braffa.sellemwb.cucumbertest.features"
,glue={"com.braffa.sellemwb.cucumbertest.stepDefinition"}
)
 
public class TestRunner {
 
}