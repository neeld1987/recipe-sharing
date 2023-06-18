package helper;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class FileConverter {

    public FileConverter(){}

    public byte[] fromFile(String path) {
        byte[] bytes = new byte[0];
        try {
            bytes  = new ClassPathResource(path).getInputStream().readAllBytes();
        }catch (IOException e){
            e.printStackTrace();
        }
        return bytes;
    }
}
