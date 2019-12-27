package me.alexanderhodes.myparkbackend.mail;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

public class MailService {

    private String apiKey;

    public MailService() {
        this.apiKey = System.getenv().get("AGWA_MYPARK_SENDGRID_API");
    }

    public void send (String sender, String receiver, String subject, String text) throws IOException {

        Mail mail = this.createConfiguration(sender, receiver, subject, text);

        SendGrid sendGrid = new SendGrid(this.apiKey);

        Request request = this.createRequest(mail);

        Response response = sendGrid.api(request);

        this.handleResponse(response);
    }

    private Mail createConfiguration (String sender, String receiver, String subject, String text) {
        Email from = new Email(sender);
        Email to = new Email(receiver);
        Content content = new Content("text/html", text);

        Mail mail = new Mail(from, subject, to, content);

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
