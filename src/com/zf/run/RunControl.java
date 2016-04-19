package com.zf.run;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.zf.assertion.AssertionListener;
import com.zf.util.DefinedReport;

public class RunControl {
	
	private String testName;

	private String testCaseName;

	private String[] tests;

	//private boolean reRun = false;

	private int threadCount = 1;

	/*public void setReRun(boolean reRun) {
		this.reRun = reRun;
	}*/

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	private void getTestsName() {
		tests = testCaseName.split(",");
	}

	private List<XmlSuite> xmlSuites() {
		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		XmlSuite suite = this.xmlSuite();
		suites.add(suite);
		return suites;
	}

	private XmlSuite xmlSuite() {
		XmlSuite suite = new XmlSuite();
		suite.setName("Automation Suite");
		List<XmlTest> xmlTests = new ArrayList<XmlTest>();
		for (String testname : tests) {
			xmlTests.add(this.xmlTest(suite, testname, this.xmlClass()));
		}
		if (threadCount > 1) {
			suite.setParallel("tests");
			suite.setThreadCount(threadCount);
		}
		// List<String> listListeners = new ArrayList<String>();
		// listListeners.add("com.test.util.ResultListener");
		// listListeners.add("com.test.util.NewReporter");
		// suite.setListeners(listListeners);
		// System.out.println(suite.toXml());
		return suite;
	}

	private XmlTest xmlTest(XmlSuite suite, String name, List<XmlClass> classes) {
		XmlTest test = new XmlTest(suite);
		test.setName(name);
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("testCase", name);
		test.setParameters(parameters);
		test.setClasses(classes);
		return test;
	}

	private List<XmlClass> xmlClass() {
		List<XmlClass> classes = new ArrayList<XmlClass>();
		classes.add(new XmlClass("com.zf.testcase.TestCollection"));
		return classes;
	}

//	private XmlSuite failedXmlSuite(String reportDir) {
//		XmlSuite suite = new XmlSuite();
//		List<String> files = new ArrayList<String>();
//		files.add(reportDir + "/testng-failed.xml");
//		suite.setSuiteFiles(files);
//		return suite;
//	}

//	private void runFailedTestCases(TestNG testng, NewReporter nr) {
//		if (testng.hasFailure()) {
//			nr.updateScreenIndex();
//			List<XmlSuite> failedSuites = new ArrayList<XmlSuite>();
//			failedSuites.add(this.failedXmlSuite(testng.getOutputDirectory()));
//			testng.setXmlSuites(failedSuites);
//			testng.run();
//		}
//	}

	public void runTestNG() {
		this.getTestsName();
		//NewReporter nr = new NewReporter();
		AssertionListener al = new AssertionListener();
		DefinedReport dr = new DefinedReport(testName);
		TestNG testng = new TestNG();
		testng.addListener(dr);
		testng.addListener(al);
		testng.setXmlSuites(this.xmlSuites());	
		testng.run();
//		if (reRun)
//			this.runFailedTestCases(testng, nr);
	}
}
