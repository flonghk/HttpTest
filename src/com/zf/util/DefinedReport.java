package com.zf.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DefinedReport implements IReporter {
	
	private String name;
	
	private int testsPass = 0;

	private int testsFail = 0;

	private int testsSkip = 0;

	public DefinedReport(String name) {
		this.name = name;
	}

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {
		List<ITestResult> list = new ArrayList<ITestResult>();
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> suiteResults = suite.getResults();
			for (ISuiteResult suiteResult : suiteResults.values()) {
				ITestContext testContext = suiteResult.getTestContext();
				IResultMap passedTests = testContext.getPassedTests();
				testsPass = testsPass + passedTests.size();
				IResultMap failedTests = testContext.getFailedTests();
				testsFail = testsFail + failedTests.size();
				IResultMap skippedTests = testContext.getSkippedTests();
				testsSkip = testsSkip + skippedTests.size();
				IResultMap failedConfig = testContext.getFailedConfigurations();
				list.addAll(this.listTestResult(passedTests));
				list.addAll(this.listTestResult(failedTests));
				list.addAll(this.listTestResult(skippedTests));
				list.addAll(this.listTestResult(failedConfig));
			}
		}
		this.sort(list);
		this.outputResult(list);
	}

	private ArrayList<ITestResult> listTestResult(IResultMap resultMap) {
		Set<ITestResult> results = resultMap.getAllResults();
		return new ArrayList<ITestResult>(results);
	}

	private void sort(List<ITestResult> list) {
		Collections.sort(list, new Comparator<ITestResult>() {
			@Override
			public int compare(ITestResult r1, ITestResult r2) {
				if (r1.getStartMillis() > r2.getStartMillis()) {
					return 1;
				} else {
					return -1;
				}
			}
		});
	}

	private void outputResult(List<ITestResult> list) {
		try {
			String path = System.getProperty("user.dir")+File.separator+"test-result/"+name+"."+testsPass+"."+testsFail+"."+testsSkip+".txt";
			BufferedWriter output = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(new File(path)),"UTF-8"));
			List<ReportInfo> listInfo = new ArrayList<ReportInfo>();
			for (ITestResult result : list) {
				String tn = result.getTestContext().getCurrentXmlTest().getParameter("testCase");
				Map<String, String> params = this.getParams(result);
				long spendTime = result.getEndMillis() - result.getStartMillis();
				String status = this.getStatus(result.getStatus());
				List<String> log = Reporter.getOutput(result);
				ReportInfo info = new ReportInfo();
				info.setName(tn);
				info.setParameters(params);
				info.setSpendTime(spendTime);
				info.setStatus(status);
				info.setLog(log);
				listInfo.add(info);
			}
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			output.write(gson.toJson(listInfo));
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("unchecked")
	private Map<String, String> getParams(ITestResult result){
		Object[] ps = result.getParameters();
		if(ps==null || ps.length==0){
			return new HashMap<String, String>();
		}
		return (Map<String, String>) ps[0];
	}

	private String getStatus(int status) {
		String statusString = null;
		switch (status) {
		case 1:
			statusString = "SUCCESS";
			break;
		case 2:
			statusString = "FAILURE";
			break;
		case 3:
			statusString = "SKIP";
			break;
		default:
			break;
		}
		return statusString;
	}
	
}
