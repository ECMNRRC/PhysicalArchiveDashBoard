package com.dataserve.pad.db.command;



import javax.servlet.http.HttpServletRequest;

import com.dataserve.pad.manager.EmailManager;
import com.dataserve.pad.permissions.ActionType;
import com.dataserve.pad.permissions.Module;


public class SendMailComm extends CommandBase{
    public SendMailComm(HttpServletRequest request) {
        super(request);
    }
    
	@Override
    public String execute() throws Exception {
		try {
			
			String mailTemplate =  request.getParameter("mailTemplate");
	
			if (mailTemplate == null || mailTemplate.trim().equals("")) {
				throw new Exception("Error mailTemplate must be provided");
			}
			EmailManager emailManager = new EmailManager();
			return emailManager.sendHelpRequestEmail(mailTemplate,currentUserId);
		
		} catch (Exception e) {
			throw new Exception("Error sending email", e);
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
