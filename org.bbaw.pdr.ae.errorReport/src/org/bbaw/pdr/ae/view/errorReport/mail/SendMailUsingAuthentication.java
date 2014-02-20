/**
 * This file is part of Archiv-Editor.
 * 
 * The software Archiv-Editor serves as a client user interface for working with
 * the Person Data Repository. See: pdr.bbaw.de
 * 
 * The software Archiv-Editor was developed at the Berlin-Brandenburg Academy
 * of Sciences and Humanities, Jägerstr. 22/23, D-10117 Berlin.
 * www.bbaw.de
 * 
 * Copyright (C) 2010-2013  Berlin-Brandenburg Academy
 * of Sciences and Humanities
 * 
 * The software Archiv-Editor was developed by @author: Christoph Plutte.
 * 
 * Archiv-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Archiv-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Archiv-Editor.  
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
/*
 * @author: Christoph Plutte
 */
package org.bbaw.pdr.ae.view.errorReport.mail;

/*
 Some SMTP servers require a username and password authentication before you
 can use their Server for Sending mail. This is most common with couple
 of ISP's who provide SMTP Address to Send Mail.

 This Program gives any example on how to do SMTP Authentication
 (User and Password verification)

 This is a free source code and is provided as it is without any warranties and
 it can be used in any your code for free.

 Author : Sudhir Ancha
 */

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/*
 To use this program, change values for the following three constants,

 SMTP_HOST_NAME -- Has your SMTP Host Name
 SMTP_AUTH_USER -- Has your SMTP Authentication UserName
 SMTP_AUTH_PWD  -- Has your SMTP Authentication Password

 Next change values for fields

 emailMsgTxt  -- Message Text for the Email
 emailSubjectTxt  -- Subject for email
 emailFromAddress -- Email Address whose name will appears as "from" address

 Next change value for "emailList".
 This String array has List of all Email Addresses to Email Email needs to be sent to.


 Next to run the program, execute it as follows,

 SendMailUsingAuthentication authProg = new SendMailUsingAuthentication();

 */

/**
 * The Class SendMailUsingAuthentication.
 * @author Christoph Plutte
 */
public class SendMailUsingAuthentication
{
	/**
	 * SimpleAuthenticator is used to do simple authentication when the SMTP
	 * server requires it.
	 */
	private class SMTPAuthenticator extends javax.mail.Authenticator
	{
		@Override
		public PasswordAuthentication getPasswordAuthentication()
		{
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}

	/** The Constant SMTP_HOST_NAME. */
	private static final String SMTP_HOST_NAME = "mail.gmx.net";

	/** The Constant SMTP_AUTH_USER. */
	private static final String SMTP_AUTH_USER = "archiv-editor-buck-report@gmx.de";

	/** The Constant SMTP_AUTH_PWD. */
	private static final String SMTP_AUTH_PWD = "jGEhor23§$=io";

	/** The Constant emailMsgTxt. */
	private static final String emailMsgTxt = "Online Order Confirmation Message. Also include the Tracking Number.";

	/** The Constant emailSubjectTxt. */
	private static final String emailSubjectTxt = "Order Confirmation Subject";

	/** The Constant emailFromAddress. */
	private static final String emailFromAddress = "archiv-editor-buck-report@gmx.de";

	// Add List of Email address to who email needs to be sent to
	/** The Constant emailList. */
	private static final String[] emailList =
	{"plutte@bbaw.de"};

	/**
	 * The main method.
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(final String[] args) throws Exception
	{
		SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
		smtpMailSender.postMail(emailList, emailSubjectTxt, emailMsgTxt, emailFromAddress, null);
	}

	/**
	 * Post mail.
	 * @param recipients the recipients
	 * @param subject the subject
	 * @param message the message
	 * @param from the from
	 * @param attachment the attachment
	 * @throws MessagingException the messaging exception
	 */
	public final void postMail(final String[] recipients, final String subject, final String message,
			final String from, final String attachment) throws MessagingException
	{
		boolean debug = false;

		// Set the host smtp address
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");

		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);

		session.setDebug(debug);

		// create a message
		Message msg = new MimeMessage(session);

		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++)
		{
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		if (attachment != null)
		{
			msg.setFileName(attachment);
			// create the message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			// fill message
			messageBodyPart.setText(message);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();

			DataSource source = new FileDataSource(attachment);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(attachment);
			multipart.addBodyPart(messageBodyPart);
			// Put parts in message

			msg.setContent(multipart);

		}
		Transport.send(msg);
		//		Activator.getDefault().getPreferenceStore().setValue("MAIL_SMTP_AUTH_PWD", ""); //$NON-NLS-1$

	}

}
