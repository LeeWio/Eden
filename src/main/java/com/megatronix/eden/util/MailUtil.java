package com.megatronix.eden.util;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class MailUtil {

  @Resource
  private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String from;

  /**
   * Sends a simple text email.
   *
   * @param to      recipient email address.
   * @param subject the subject of the email.
   * @param text    the body text of the email.
   */
  public void sendSimpleMessage(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(from); // Use the default from address
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    mailSender.send(message);
  }

  /**
   * Sends an HTML email.
   *
   * @param to      recipient email address.
   * @param subject the subject of the email.
   * @param html    the body content of the email (HTML format).
   * @throws MessagingException if there is an issue creating or sending the
   *                            email.
   */
  public void sendHtmlMessage(String to, String subject, String html) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates multipart message (e.g., for
                                                                     // HTML)

    helper.setFrom(from);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(html, true); // true indicates HTML format
    mailSender.send(message);
  }

  /**
   * Sends a simple text email to multiple recipients.
   *
   * @param toList  List of recipient email addresses.
   * @param subject the subject of the email.
   * @param text    the body text of the email.
   */
  public void sendSimpleMessageToMultiple(List<String> toList, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(from); // Use the default from address
    message.setTo(toList.toArray(new String[0])); // Convert List to array
    message.setSubject(subject);
    message.setText(text);
    mailSender.send(message);
  }

  /**
   * Sends an HTML email to multiple recipients.
   *
   * @param toList  List of recipient email addresses.
   * @param subject the subject of the email.
   * @param html    the body content of the email (HTML format).
   * @throws MessagingException if there is an issue creating or sending the
   *                            email.
   */
  public void sendHtmlMessageToMultiple(List<String> toList, String subject, String html) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setFrom(from); // Use the default from address
    helper.setTo(toList.toArray(new String[0])); // Convert List to array
    helper.setSubject(subject);
    helper.setText(html, true); // true indicates HTML format
    mailSender.send(message);
  }

  /**
   * Sends an email with an attachment.
   * <p>
   * This method constructs an email message with the provided recipient, subject,
   * body text,
   * and attachment. The attachment file is added with a display name of
   * "Invoice".
   * The email is then sent using the configured {@link JavaMailSender}.
   * </p>
   *
   * @param to               The recipient's email address.
   * @param subject          The subject of the email.
   * @param text             The body text of the email.
   * @param pathToAttachment The file path of the attachment to be included in the
   *                         email.
   * @throws MessagingException if an error occurs while sending the email or
   *                            processing the attachment.
   */
  public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment)
      throws MessagingException {
    // Create a MimeMessage object, which is used to construct the email
    MimeMessage message = mailSender.createMimeMessage();

    // Create a MimeMessageHelper to simplify MimeMessage creation.
    // The 'true' flag indicates that this is a multipart message (i.e., it can
    // contain attachments).
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    // Set the sender's email address (from is previously defined)
    helper.setFrom(from);

    // Set the recipient's email address (to is passed as a parameter)
    helper.setTo(to);

    // Set the email's subject
    helper.setSubject(subject);

    // Set the email's body content
    helper.setText(text);

    // Create a FileSystemResource to represent the attachment file.
    // pathToAttachment is the file path provided as a parameter.
    FileSystemResource file = new FileSystemResource(new File(pathToAttachment));

    // Add the attachment to the email. "Invoice" is the display name of the
    // attachment.
    helper.addAttachment("Invoice", file);

    // Send the email via the mailSender
    mailSender.send(message);
  }
}
