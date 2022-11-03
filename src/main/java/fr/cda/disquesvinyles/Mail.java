package fr.cda.disquesvinyles;

import sendinblue.*;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Mail {
	public static void send(String recipient, File files) {
		Dotenv dotenv = Dotenv.load();

		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
		apiKey.setApiKey(dotenv.get("API_SENDINBLUE"));

		try {
			TransactionalEmailsApi api = new TransactionalEmailsApi();
			SendSmtpEmailSender sender = new SendSmtpEmailSender();

			sender.setEmail(dotenv.get("SENDER_EMAIL"));
			sender.setName("Camille");

			List<SendSmtpEmailTo> toList = new ArrayList<SendSmtpEmailTo>();
			SendSmtpEmailTo to = new SendSmtpEmailTo();
			to.setEmail(recipient);
			toList.add(to);

			SendSmtpEmailReplyTo replyTo = new SendSmtpEmailReplyTo();
			replyTo.setEmail(dotenv.get("SENDER_EMAIL"));
			replyTo.setName("Camille");

			SendSmtpEmailAttachment attachment = new SendSmtpEmailAttachment();
			attachment.setName("resultat.txt");
			byte[] encode = Files.readAllBytes(Paths.get(files.toURI()));
			attachment.setContent(encode);
			List<SendSmtpEmailAttachment> attachmentList = new ArrayList<SendSmtpEmailAttachment>();
			attachmentList.add(attachment);

			SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
			sendSmtpEmail.setSender(sender);
			sendSmtpEmail.setTo(toList);
			sendSmtpEmail.setReplyTo(replyTo);
			sendSmtpEmail.setHtmlContent(
							"<html>" +
											"<body>" +
											"<h1>Ceci est un titre d'un mail</h1>" +
											"<p>Ceci est un contenu de mail</p>" +
											"</body>" +
											"</html>"
			);

			sendSmtpEmail.setSubject("Résultats de la demande - ScrapCamille");
			sendSmtpEmail.setAttachment(attachmentList);

			CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
			System.out.println(response.toString());

		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}
}
