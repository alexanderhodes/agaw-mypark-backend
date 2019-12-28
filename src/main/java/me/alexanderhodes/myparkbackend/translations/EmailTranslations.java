package me.alexanderhodes.myparkbackend.translations;

import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component()
public class EmailTranslations {

    public static final String REMINDER = "Mail.0";
    public static final String PARKINGSPACE_ASSIGNMENT = "Mail.1";
    public static final String BOOKING_SUCCESS_DATE = "Mail.2";
    public static final String COSTCENTRE_REQUEST = "Mail.3";
    public static final String ADMIN_RIGHTS_REMOVED = "Mail.4";
    public static final String PARKINGSPACE = "Mail.5";
    public static final String ACCOUNT_DELETED = "Mail.6";
    public static final String ADMIN_RIGHTS = "Mail.7";
    public static final String NEW_ACCOUNT = "Mail.8";
    public static final String RESET_PASSWORD = "Mail.9";
    public static final String PARKINGSPACE_FREE = "Mail.10";
    public static final String WAY_CHANGED = "Mail.11";
    public static final String BOOKING_SUCCESS_TODAY = "Mail.12";
    public static final String BOOKING_NOT_SUCCESS = "Mail.13";

    private ResourceBundle resourceBundle;

    public EmailTranslations() {
        this.resourceBundle = ResourceBundle.getBundle("emails");
    }

    public String getSubject (String key) {
        return this.resourceBundle.getString(key).split("; ")[0];
    }

    public String getHeadline(String key) {
        return this.resourceBundle.getString(key).split("; ")[1];
    }

    public String getContent(String key) {
        return this.resourceBundle.getString(key).split("; ")[2];
    }

}
