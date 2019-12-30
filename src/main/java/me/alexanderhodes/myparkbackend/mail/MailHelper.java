package me.alexanderhodes.myparkbackend.mail;

import me.alexanderhodes.myparkbackend.helper.FileImport;
import me.alexanderhodes.myparkbackend.helper.UrlHelper;
import me.alexanderhodes.myparkbackend.mail.model.MMail;
import me.alexanderhodes.myparkbackend.translations.EmailTranslations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

@Configuration()
public class MailHelper {

    @Autowired
    private FileImport fileImport;
    @Autowired
    private EmailTranslations emailTranslations;
    @Autowired
    private UrlHelper urlHelper;

    public MailHelper() {

    }

    public MMail createMail(String receiver, String key, List<AbstractMap.SimpleEntry<String, String>> placeholders) {
        String text = this.createText(key, placeholders);
        String subject = emailTranslations.getSubject(key);

        MMail mail = new MMail(receiver, subject, text);

        return mail;
    }

    private String createText(String key, List<AbstractMap.SimpleEntry<String, String>> placeholders) {
        String htmlBody = this.fileImport.getText("email-template.html");
        final StringBuffer buffer = new StringBuffer(htmlBody);

        String content = this.emailTranslations.getContent(key);
        AbstractMap.SimpleEntry<String, String> contentEntry =
                new AbstractMap.SimpleEntry("PLACEHOLDER_CONTENT", content);
        replacePlaceholder(buffer, contentEntry);

        // add standard placeholders for headline url and content
        placeholders.addAll(this.addPlaceholders(key));

        placeholders.forEach(simpleEntry -> {
            System.out.println("key: " + simpleEntry.getKey() + " - value: " + simpleEntry.getValue());
            // while loop is necessary because an entry can be in this template multiple times
            while (buffer.indexOf(simpleEntry.getKey()) > -1) {
                replacePlaceholder(buffer, simpleEntry);
            }
        });

        return buffer.toString();
    }

    private void replacePlaceholder(StringBuffer buffer, AbstractMap.SimpleEntry<String, String> simpleEntry) {
        int start = buffer.indexOf(simpleEntry.getKey());
        int end = simpleEntry.getKey().length();
        buffer.replace(start, start + end, simpleEntry.getValue());
    }

    private List<AbstractMap.SimpleEntry<String, String>> addPlaceholders (String key) {
        List<AbstractMap.SimpleEntry<String, String>> placeholders = new ArrayList<>();

        String headline = this.emailTranslations.getHeadline(key);
        String url = this.urlHelper.getFrontendUrl();
        // Important to add content as index 0
//        placeholders.add(0, new AbstractMap.SimpleEntry("PLACEHOLDER_CONTENT", content));
        placeholders.add(new AbstractMap.SimpleEntry("PLACEHOLDER_HEADLINE", headline));
        placeholders.add(new AbstractMap.SimpleEntry("PLACEHOLDER_URL", url));

        return placeholders;
    }


}
