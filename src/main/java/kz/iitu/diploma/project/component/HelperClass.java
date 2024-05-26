package kz.iitu.diploma.project.component;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

@Component
public class HelperClass {

    private static final Pattern pattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$");

    public String createRandomNumber() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    public Boolean verifyEmailAddress(String email) {
        return pattern.matcher(email).matches();
    }

    @SafeVarargs
    public static <T> List<T> listOf(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

}
