package fr.cda.disquesvinyles;

import sendinblue.*;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Mail.
 */
public class Mail {
	/**
	 * Send.
	 *
	 * @param recipient the recipient
	 * @param search    the search
	 */
	public static void send(String recipient, String search) {
		Dotenv dotenv = Dotenv.load();

		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
		apiKey.setApiKey(dotenv.get("API_SENDINBLUE"));

		try {
			//Enregistrement du fichier
			String path = "recherches/test.txt";
			PrintWriter print = new PrintWriter(new BufferedWriter
							(new FileWriter(path)));
			print.println(search);
			print.close();

			//Préparation de l'API SendInBlue
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
			attachment.setName("test.txt");
			byte[] encode = Files.readAllBytes(Paths.get("recherches/test.txt"));
			attachment.setContent(encode);
			List<SendSmtpEmailAttachment> attachmentList = new ArrayList<SendSmtpEmailAttachment>();
			attachmentList.add(attachment);

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
			LocalDate localDate = LocalDate.now();

			SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
			sendSmtpEmail.setSender(sender);
			sendSmtpEmail.setTo(toList);
			sendSmtpEmail.setReplyTo(replyTo);
			sendSmtpEmail.setHtmlContent(
							"<html>" +
											"<body>" +
											"<h1>Votre recherche du " + dtf.format(localDate) + " sur notre logiciel</h1>" +
											"<p>Vous trouverez ci joint votre recherche faites sur notre logiciel de scrapping. Vous pouvez également retrouver certains de ces articles sur notre boutique : <a href='bestmusic.camilledebusscher.tech'>bestmusic.camilledebusscher.tech</a></p>" +
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
