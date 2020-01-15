package me.alexanderhodes.myparkbackend.mail;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import me.alexanderhodes.myparkbackend.mail.model.MMail;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MailService {

    private static final String SENDER = "hello@agwa-mypark.herokuapp.com";
    private String apiKey;

    public MailService() {
        this.apiKey = System.getenv().get("AGWA_MYPARK_SENDGRID_API");
    }

    public void send (MMail mmail) throws IOException {
        this.send(SENDER, mmail.getReceiver(), mmail.getCc(), mmail.getSubject(), mmail.getContent());
    }

    public void send(String receiver, String cc, String subject, String text) throws IOException {
        this.send(SENDER, receiver, cc, subject, text);
    }

    private void send(String sender, String receiver, String cc, String subject, String text) throws IOException {
        Mail mail = this.createConfiguration(sender, receiver, cc, subject, text);
        SendGrid sendGrid = new SendGrid(this.apiKey);
        Request request = this.createRequest(mail);

        Response response = sendGrid.api(request);

        this.handleResponse(response);
    }

    private Mail createConfiguration(String sender, String receiver, String ccReceiver, String subject, String text) {
        Mail mail = new Mail();
        Personalization personalization = new Personalization();

        Email from = new Email(sender);
        Email to = new Email(receiver);
        Content content = new Content("text/html", text);

        if (ccReceiver != null && ccReceiver.length() > 0 && !receiver.equals(ccReceiver)) {
            Email cc = new Email(ccReceiver);

            personalization.addCc(cc);
        }

        personalization.setSubject(subject);
        personalization.addTo(to);

        mail.setFrom(from);
        mail.addContent(content);
        mail.addPersonalization(personalization);

        return mail;
    }

    private Request createRequest (Mail mail) throws IOException {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        return request;
    }

    private void handleResponse (Response response) {
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
    }

}
