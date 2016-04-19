package com.zf.assertion;

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class AssertionListener extends TestListenerAdapter{
	
	@Override
	public void onTestStart(ITestResult result) {
		Assertion.begin();
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		this.handleAssertion(tr);
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		this.handleAssertion(tr);
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		this.handleAssertion(tr);
	}
	
	private int index;
	
	private boolean isNew = false;
	
	private void handleAssertion(ITestResult tr){
		Throwable throwable = tr.getThrowable();    
		if(!Assertion.flag || throwable!=null){
			if(throwable==null){
				throwable = new Throwable();
				isNew = true;
	        } 
			StackTraceElement[] alltrace = new StackTraceElement[0];
			for (Error e : Assertion.errors) {
				alltrace = this.getAllStackTraceElement(tr, e, null, alltrace);
            }
			if(!isNew){
				alltrace = this.getAllStackTraceElement(tr, null, throwable, alltrace);
            }else{
            	isNew = false;
            }
            throwable.setStackTrace(alltrace);
            tr.setThrowable(throwable);
            Assertion.flag = true;   
            Assertion.errors.clear();
			tr.setStatus(ITestResult.FAILURE);  
		}
	}
	
	private StackTraceElement[] getAllStackTraceElement(ITestResult tr, Error e, Throwable throwable, StackTraceElement[] alltrace){
		StackTraceElement[] traces = (e==null?throwable.getStackTrace():e.getStackTrace());
		StackTraceElement[] et = this.getKeyStackTrace(tr, traces);
		String msg = (e==null?throwable.getMessage():e.getMessage());
		StackTraceElement[] message = new StackTraceElement[]{new StackTraceElement("message : "+msg+" in method : ", tr.getMethod().getMethodName(), tr.getTestClass().getRealClass().getSimpleName(), index)};
		index = 0;
		alltrace = this.merge(alltrace, message);
		alltrace = this.merge(alltrace, et);
		return alltrace;
	}
	
	private StackTraceElement[] getKeyStackTrace(ITestResult tr, StackTraceElement[] stackTraceElements){
        List<StackTraceElement> ets = new ArrayList<StackTraceElement>();
        for (StackTraceElement stackTraceElement : stackTraceElements) {  
            if(stackTraceElement.getClassName().contains("com.zf")){               
                ets.add(stackTraceElement);
                index = stackTraceElement.getLineNumber();
            }
        }
        StackTraceElement[] et = new StackTraceElement[ets.size()];
        for (int i = 0; i < et.length; i++) {
            et[i] = ets.get(i);
        }
        return et;
    }
	
	private StackTraceElement[] merge(StackTraceElement[] traces1, StackTraceElement[] traces2){
        StackTraceElement[] ste = new StackTraceElement[traces1.length+traces2.length];
        for (int i = 0; i < traces1.length; i++) {
            ste[i] = traces1[i];
        }
        for (int i = 0; i < traces2.length; i++) {
            ste[traces1.length+i] = traces2[i];
        }
        return ste;
    }
	
}
