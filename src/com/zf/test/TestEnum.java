package com.zf.test;

public enum TestEnum {
	
	OK(200,"success"),
	FAIL(300,"fail");
	
	private int retCode;
	
	private String msg;
	
	private TestEnum(int retCode, String msg) {
		this.retCode = retCode;
		this.msg = msg;
	}

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return retCode+" "+msg;
	}

	public static void main(String[] args) {
		TestEnum[] vs = TestEnum.values();
		System.out.println(vs[0].getMsg());
		System.out.println(TestEnum.OK.getRetCode());
		System.out.println(TestEnum.OK.getMsg());
		System.out.println(TestEnum.valueOf("OK").getMsg());
	}
	
}
