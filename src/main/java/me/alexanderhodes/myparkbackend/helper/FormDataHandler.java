package me.alexanderhodes.myparkbackend.helper;

import org.springframework.stereotype.Component;

@Component()
public class FormDataHandler {

    public String extract(String formData, int index) {
        String[] formDataArray = formData.split(System.lineSeparator());
        return formDataArray.length > index ? formDataArray[index] : null;
    }

}
