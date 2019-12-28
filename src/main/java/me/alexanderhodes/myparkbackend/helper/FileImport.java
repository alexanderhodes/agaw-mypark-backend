package me.alexanderhodes.myparkbackend.helper;

import org.apache.catalina.connector.InputBuffer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.stream.Collectors;

@Component()
public class FileImport {

    public String getText(String resourceLocation) {
        String result = "";
        try {
            Resource resource = new ClassPathResource(resourceLocation);
            InputStream inputStream = resource.getInputStream();

            result = new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
