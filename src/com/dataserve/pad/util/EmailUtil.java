package com.dataserve.pad.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.dataserve.pad.util.ConfigManager;


public class EmailUtil {
	
	public static void main(String[] args) {
		EmailUtil mailUtil = new EmailUtil();
		try {
			String emailTemplate = "<div style='text-align: center;'><br />new email is ceates</div>";
			List<String> emailToLsit = new ArrayList<String>();
			emailToLsit.add("MElSayed.eg@dataserve.com.sa");
			emailToLsit.add("MElSayed.eg@dfddfdfrgf");
			mailUtil.sendMail("mail util ",
					emailToLsit,
					emailTemplate 
					);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public static boolean sendMail(String subject, List<String> to, String mailTemplate) throws EmailException {
        boolean status = false;

        try {
			if(to !=null && to.size() >0){
				/*System.out.println("mail: " + ConfigManager.getEmailName() + "\n password: "
						+ ConfigManager.getEmailPassword() + "\n port:" + ConfigManager.getEmailPort() + "\n provider:"
						+ ConfigManager.getEmailProvider() + "\n subject: " + "" + subject + "\n to:"
						+ to);*/
				
				// 1) get the session object
//				System.out.println(" 1) get the session object");
				Properties properties = System.getProperties();
				//properties.setProperty("mail.smtp.protocol", "smtp");

				// Setup mail server 
				properties.setProperty("mail.smtp.host", ConfigManager.getEmailProvider());
				// TLS Port 
				properties.setProperty("mail.smtp.port",ConfigManager.getEmailPort()+"");
				
				
				properties.put("mail.smtp.ssl.trust", ConfigManager.getEmailProvider());
				properties.put("mail.smtp.auth", "true");
				// enable STARTTLS 
				properties.put("mail.smtp.starttls.enable", "false"); 
				// if we set starttls = true add this
				// Use the following if you need SSL
	            //properties.put("mail.smtp.ssl.enable", "true");
//				properties.put("mail.smtp.socketFactory.port", emailPort);
//				properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//				properties.put("mail.smtp.socketFactory.fallback", "false");
//							

				Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(ConfigManager.getEmailName(), ConfigManager.getEmailPassword());
					}
				});

				// 2) compose message
//				System.out.println("2) compose message");

				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(ConfigManager.getEmailName()));
//				message.addRecipient(RecipientType.TO, new InternetAddress(to));
				
				InternetAddress[] toInternetAdress = new InternetAddress[to.size()];
				
			   for (int i = 0; i < to.size(); i++) {
				   toInternetAdress[i] = new InternetAddress(to.get(i));
			   }
				
				message.addRecipients(RecipientType.TO, toInternetAdress);
				
				message.setSubject(MimeUtility.encodeText(subject, "UTF-8", null));

				// body
				MimeBodyPart text = new MimeBodyPart();
				text.setContent(mailTemplate, "text/html;charset=UTF-8");

				MimeBodyPart attach;
				MimeMultipart mp = new MimeMultipart();
				mp.addBodyPart(text);
				
				
				/*if (contentInfoList != null) {
					for (ContentInfo contentInfo : contentInfoList) {
						System.out.println("inside content info");
						attach = new MimeBodyPart();
						ByteArrayDataSource bds = new ByteArrayDataSource(
								contentInfo.getContent(),
								contentInfo.getContentType());
						attach.setDataHandler(new DataHandler(bds));
						attach.setFileName(MimeUtility.encodeText(
								contentInfo.getName(), "UTF-8", null));
						mp.addBodyPart(attach);
					}
				}*/
				
				mp.setSubType("mixed");
				message.setContent(mp);
				message.saveChanges();

				// 3) send message
				Transport.send(message);
//				System.out.println("3) send message");

				status = true;

			
			}else{
				status = true;
			}
			
			
		} catch (MessagingException e) {
            throw new EmailException("Error sending mail", e);
        } catch (UnsupportedEncodingException e) {
            throw new EmailException("Error encoding email content", e);
        }

        return status;
    }
}
