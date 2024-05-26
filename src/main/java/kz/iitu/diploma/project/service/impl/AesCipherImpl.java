package kz.iitu.diploma.project.service.impl;

import com.sun.jdi.InternalException;
import kz.iitu.diploma.project.service.AesCipher;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Base64;

@Service
public class AesCipherImpl implements AesCipher {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CFB/PKCS5Padding";

    public String encrypt(byte[] key, String data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String key, String encryptedData) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    public String cipher(byte[] key, String message) {
        try {
            String command = "C:\\Users\\ramaz\\AppData\\Local\\Programs\\Python\\Python38\\python.exe"; // Путь к python.exe
            String scriptPath = "C:\\Users\\ramaz\\IdeaProjects\\project\\cipher.py"; // Путь к python script

            String[] pythonScriptArgs = new String[] {"encrypt", Arrays.toString(key), message};
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(command, scriptPath);
            processBuilder.command().addAll(Arrays.asList(pythonScriptArgs));

            Process process = processBuilder.start();

            // Чтение вывода скрипта
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            String hexCode = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                hexCode = line;
            }

            System.out.println("Error Output:");
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            return hexCode;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new InternalException();
    }

}
