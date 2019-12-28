package me.alexanderhodes.myparkbackend.helper;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.UUID;

@Component()
public class UuidGenerator {

    public String newId() {
        return UUID.randomUUID().toString();
    }

    public String newBase64Token(String secret) throws UnsupportedEncodingException {
        // 1. generate uuid
        String uuid = this.newId();
        // 2. append secret to uuid seperated by :
        StringBuffer buffer = new StringBuffer(uuid).append(":").append(secret);
        // 3. generate bytes from string of buffer
        byte[] bytes = buffer.toString().getBytes("UTF-8");
        // 4. encode byte array with base64
        String encoded = Base64.getEncoder().encodeToString(bytes);

        return encoded;
    }

}
