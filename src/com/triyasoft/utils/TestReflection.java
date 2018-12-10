package com.triyasoft.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestReflection {

	public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		
		long start = System.currentTimeMillis();
		
		for(int i = 0 ; i < 1000; i++){
		
		  Class<?> c= Class.forName("com.triyasoft.utils.TestReflection");  
		  System.out.println(c.getName());  
		  Object t = c.newInstance();
		 Method[] methods =   c.getDeclaredMethods();
		 for (Method method : methods) {
			System.out.println(method.getName());
			if(method.getName().equals("invokeMe")){
			   method.invoke(t,"singh");
			}
		}
		 
	}
		 

     long end = System.currentTimeMillis();
     
     System.out.println(end-start);
	}
	
	
	void invokeMe(String lastname) {
		
		System.out.println("Hi Krishna "+lastname);
	}
}
