package kz.iitu.diploma.project.controller;

import com.sun.jdi.InternalException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/python")
public class PythonController {

    @GetMapping("/createHashCode")
    public byte[] createHashCode() {
        try {
            String command = "python.exe"; // Путь к python.exe
            String scriptPath = "create_hash_code.py"; // Путь к python script
            ProcessBuilder processBuilder = new ProcessBuilder(command, scriptPath);
            Process process = processBuilder.start();

            // Чтение вывода скрипта
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            String hexCode = "";
            while ((line = reader.readLine()) != null) {
                hexCode = line;
            }

            return hexCode.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new InternalException();
    }

}
