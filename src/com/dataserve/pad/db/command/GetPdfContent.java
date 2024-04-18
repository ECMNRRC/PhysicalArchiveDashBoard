package com.dataserve.pad.db.command;

import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Base64;

public class GetPdfContent extends CommandBase {
    
    public GetPdfContent(HttpServletRequest request) {
        super(request);
    }
    
    @Override
    public String execute()  throws Exception{
    	
    	InputStream inputStream = null;
    	 String fileName = "";
    	try {
	        fileName = request.getParameter("fileName");
	
	    	File file = new File(getClass().getClassLoader().getResource("pdf/"+fileName+".pdf").getFile());
	    	inputStream = new FileInputStream(file);
	    	
	    	byte[] imagebytes = new byte[(int)file.length()];
	    	inputStream.read(imagebytes, 0, imagebytes.length);
	    	
	    	String imagestr = Base64.getEncoder().encodeToString(imagebytes);
	    	return imagestr;
    	}
    	catch (IOException e) {
    		throw new Exception("Error get File  '"
					+ fileName + "'", e);

        }
    	finally {
    	    if (inputStream != null) {
    	        try {
    	            inputStream.close();
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
    	    }
    	}
		  
    
    }
    
    @Override
    protected Module getModule() {
        return null;
    }

    @Override
    protected ActionType getActionType() {
        return null;
    }
}
