package com.triyasoft.utils;

import com.triyasoft.model.Buyer;

public class JsonWrapper {
	
private String Result;
private Object[] Records ;
private int TotalRecordCount;


public String getResult() {
	return Result;
}
public void setResult(String result) {
	Result = result;
}
public Object[] getRecords() {
	return Records;
}
public void setRecords(Object[] records) {
	Records = records;
}
public int getTotalRecordCount() {
	return TotalRecordCount;
}
public void setTotalRecordCount(int totalRecordCount) {
	TotalRecordCount = totalRecordCount;
}



}
