package etfbl.mdp.services;

import java.util.ArrayList;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.*;
import javax.mail.internet.*;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.ServiceLogger;
import etfbl.mdp.models.Appointment;
import etfbl.mdp.models.Client;
import etfbl.mdp.models.CreateInvoiceDto;
import etfbl.mdp.models.VehiclePart;
import java.io.*;
import java.math.BigDecimal;

public class InvoiceService {
	
	private static ClientService clientService = new ClientService();
	private static VehiclePartService partService = new VehiclePartService();
	private static final String MAIL_SUBJECT = "Service invoice";

	public boolean sendInvoice(CreateInvoiceDto dto) {
		
		Properties props = loadMailConfig();
		
		String fileName = createInvoice(dto);
		File zipFile = toZipFile(fileName);
		
		String mailBody = "Dear " + dto.getAppointment().getClientUsername() + "," + System.lineSeparator() +
				"You can find the invoice of your recent vehicle service in the attachment." + System.lineSeparator() +
				"Sincerely, " + System.lineSeparator() + "MDP_VehicleService";
		
		String username = props.getProperty("mail.username");
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, props.getProperty("mail.password"));
			}
		});
		
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(clientService.getByUsername(dto.getAppointment().getClientUsername()).getEmail()));
			message.setSubject(MAIL_SUBJECT);
			System.out.println("salje se na mejl: " + clientService.getByUsername(dto.getAppointment().getClientUsername()).getEmail());
			MimeBodyPart textPart = new MimeBodyPart();
	        textPart.setText(mailBody);
	        
	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(textPart);
	        
	        if (zipFile != null && zipFile.exists()) {
	            MimeBodyPart attachmentPart = new MimeBodyPart();
	            attachmentPart.attachFile(zipFile);
	            multipart.addBodyPart(attachmentPart);
	        }
	        
	        message.setContent(multipart);
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			ServiceLogger.logger.severe(e.getMessage());
			return false;
		} catch (IOException e) {
			ServiceLogger.logger.severe(e.getMessage());
			return false;
		}
		
	}
	
	private static Properties loadMailConfig() {
		Properties allProp = new Properties();
		Properties mailProp = new Properties();
		
		try (FileInputStream fis = new FileInputStream(Constants.CONFIG_PATH)) {
            allProp.load(fis);
		} catch (IOException e) {
			ServiceLogger.logger.severe(e.getMessage());
		}
		
		for (String key : allProp.stringPropertyNames()) {
            if (key.startsWith("mail.")) {
                mailProp.put(key, allProp.getProperty(key));
            }
        }

		return mailProp;
	}
	
	private String createInvoice(CreateInvoiceDto dto) {
		
		Appointment app = dto.getAppointment();
		String fileName = app.getClientUsername() + "_" + app.getDateTime().toString() + ".txt";
		
		File folder = new File(Constants.INVOICE_FOLDER);
		if(!folder.exists())
			folder.mkdir();
		
		Client client = clientService.getByUsername(app.getClientUsername());
		
		try {
			PrintWriter pw = new PrintWriter(new File(Constants.INVOICE_FOLDER + File.separator + fileName));
			pw.println("Client: " + client.getName() + " " + client.getLastName());
			pw.println("Client username: " + client.getUsername());
			pw.println("Address: " + client.getAddress());
			pw.println("Phone number: " + client.getPhoneNumber());
			pw.println("Email: " + client.getEmail());
			pw.println("Vehicle: " + client.getVehicleModel());
			pw.println();
			pw.println("Service type: " + app.getType().getType());
			pw.println();
			pw.println("Used parts: ");
			BigDecimal totalPrice = dto.getInitialPrice();
			for(VehiclePart p : dto.getParts()) {
				pw.println(p);	
				totalPrice.add(p.getPrice());
				
				p.setAvailableQuantity(p.getAvailableQuantity() - 1);
				
				if(p.getAvailableQuantity() != 0)
					partService.updatePart(p);
				else
					partService.deletePart(p.getCode());
			}
			pw.println();
			pw.println("Total price: " + totalPrice.toString());
			pw.close();
		}
		catch(Exception e) {
			ServiceLogger.logger.severe(e.getMessage());
		}
		return Constants.INVOICE_FOLDER + File.separator + fileName;
	}
	
	private static File toZipFile(String filePath) {
		File inputFile = new File(filePath);
        File zipFile = new File(inputFile.getParent(), inputFile.getName() + ".zip");

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
             FileInputStream fis = new FileInputStream(inputFile)) {

            ZipEntry zipEntry = new ZipEntry(inputFile.getName());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
		} catch (IOException e) {
			ServiceLogger.logger.severe(e.getMessage());
		}
        return zipFile;
	}
}
