package com.zf.annotation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.zf.interpreter.ConstantUtil;
import com.zf.util.DefinedException;

public class ParseComponent {
	
	private List<String> allClass = new ArrayList<String>(); 
	
	public void loadAllComponent(){     
        this.listAllFiles(System.getProperty("user.dir")+File.separator+ConstantUtil.COMPILE_PATH+File.separator+ConstantUtil.COMPONENT_PATH.replace(".","/"));
        this.getPageInstance();
    }
	
	private void listAllFiles(String path){
        path = path.replace("\\", "/");
        File file = new File(path);
        if(file.isFile() && file.getName().endsWith(".class")){
            String filePath = file.getPath().replace("\\", "/");  
            filePath = path.substring(System.getProperty("user.dir").length()+1);
            int startIndex = ConstantUtil.COMPILE_PATH.length()+1;
            int endIndex = filePath.lastIndexOf(".class");
            allClass.add(filePath.substring(startIndex, endIndex).replace("/", "."));   
        }else if(file.isDirectory()){
            File[] files = file.listFiles();
            for (File f : files) {
            	//System.out.println(f.getPath());
                this.listAllFiles(f.getPath());
            }
        }
    }
     
    private void getPageInstance(){
        for (String clazz : allClass) {
            try {              
                Class<?> c = Class.forName(clazz);               
                if(c.isAnnotationPresent(Component.class)){
                	if(ConstantUtil.ALL_COMPONENT.containsKey(c.getSimpleName())){
                		throw new DefinedException("Duplicated component: "+c.getSimpleName());
                	}
                	ConstantUtil.ALL_COMPONENT.put(c.getSimpleName(), c.newInstance());
                }
            } catch (ClassNotFoundException e) {               
            	throw new DefinedException(e.getMessage());
            } catch (InstantiationException e) {               
            	throw new DefinedException(e.getMessage());
            } catch (IllegalAccessException e) {
            	throw new DefinedException(e.getMessage());
            } catch (SecurityException e) {
            	throw new DefinedException(e.getMessage());
            } catch (IllegalArgumentException e) {
            	throw new DefinedException(e.getMessage());
            }
        }
    }   
    
    public static void main(String[] args) {
    	ParseComponent pc = new ParseComponent();
    	pc.loadAllComponent();
	}
	
}
