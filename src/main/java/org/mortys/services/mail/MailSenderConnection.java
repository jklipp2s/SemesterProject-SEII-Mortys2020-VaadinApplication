package org.mortys.services.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class MailSenderConnection {

	private static MailSenderConnection mailsender = null;

	private Session mailSession;
	final private String username = Zugangsdaten.USERNAME;
	final private String password = Zugangsdaten.PASSWORD;
	final private String smtp = Zugangsdaten.SMTP;
	final private String port = Zugangsdaten.PORT;


	private MailSenderConnection() {

	}

	public static MailSenderConnection getInstance() {
		if (mailsender == null)
			mailsender = new MailSenderConnection();
		return mailsender;
	}

	public void logout() {
		mailSession = null;
		System.out.println("Vom Mail-Server ausgeloggt.");
	}

	public void login() {
		login(smtp, port, Zugangsdaten.USERNAME, Zugangsdaten.PASSWORD);
	}

	private void login(String smtpHost, String smtpPort, String username, String password) {
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.socketFactory.port", smtpPort);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", smtpPort);

		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};

		this.mailSession = Session.getDefaultInstance(props, auth);
		System.out.println("Am Mail-Server eingeloggt.");
	}


	private synchronized void send(String senderMail, String senderName, String receiverAddresses, String subject, String message)
			throws MessagingException, IllegalStateException, UnsupportedEncodingException {
		login();
		if (mailSession == null) {
			throw new IllegalStateException("Noch nicht eingeloggt?");
		}

		MimeMessage msg = new MimeMessage(mailSession);
		msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		msg.addHeader("format", "flowed");
		msg.addHeader("Content-Transfer-Encoding", "8bit");

		msg.setFrom(new InternetAddress(senderMail, senderName));
		//msg.setReplyTo(InternetAddress.parse("vm8443393004031142v@vmail.inf.h-brs.de", false));
		msg.setSubject(subject, "UTF-8");
		msg.setText(message, "UTF-8");
		msg.setSentDate(new Date());

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverAddresses, false));

		System.out.println("Versende E-Mail...");
		Transport.send(msg);
		System.out.println("E-Mail versendet.");
		logout();
	}

	public void send(List<String> receiverAddresses, String subject, String message)
			throws MessagingException, IllegalStateException, UnsupportedEncodingException {

		String recString = "";

		for (String tmp : receiverAddresses) {
			recString += tmp + ",";
		}
		send(Zugangsdaten.SENDERMAIL, Zugangsdaten.SENDERNAME, recString, subject, message);
	}

	public void send(String receiverAddresses, String subject, String message)
			throws MessagingException, IllegalStateException, UnsupportedEncodingException {
		send(Zugangsdaten.SENDERMAIL, Zugangsdaten.SENDERNAME, receiverAddresses, subject, message);
	}
}
