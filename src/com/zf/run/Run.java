package com.zf.run;

public class Run {

	public static void main(String[] args) {
		if(args==null || args.length==0){
			args = new String[]{"FirstPage", String.valueOf(System.currentTimeMillis()), System.getProperty("user.dir")};
		}
		System.setProperty("user.dir", args[2]);
		RunControl r = new RunControl();
		r.setTestName(args[1]);
		r.setTestCaseName(args[0]);
		r.setThreadCount(1);
		//r.setReRun(false);
		r.runTestNG();
	}

}
