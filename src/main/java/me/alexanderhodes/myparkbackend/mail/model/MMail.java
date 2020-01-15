package me.alexanderhodes.myparkbackend.mail.model;

import lombok.Data;

@Data
public class MMail {

    private String receiver;
    private String cc;
    private String subject;
    private String content;

    public MMail(String receiver, String cc, String subject, String content) {
        this.receiver = receiver;
        this.cc = cc;
        this.subject = subject;
        this.content = content;
    }
}
