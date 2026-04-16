package cat.udl.eps.softarch.demo.config;

import cat.udl.eps.softarch.demo.domain.Admin;
import cat.udl.eps.softarch.demo.domain.Creator;
import cat.udl.eps.softarch.demo.domain.Record;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.RecordRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

import java.time.ZonedDateTime;
import java.util.Arrays;

@Configuration
public class DBInitialization {

    @Value("${default-password}")
    String defaultPassword;

    @Value("${spring.profiles.active:}")
    private String activeProfiles;

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    public DBInitialization(UserRepository userRepository, RecordRepository recordRepository) {
        this.userRepository = userRepository;
        this.recordRepository = recordRepository;
    }

    @PostConstruct
    public void initializeDatabase() {

        // =========================
        // DEFAULT USER
        // =========================
        if (!userRepository.existsById("demo")) {
            User user = new User();
            user.setEmail("demo@sample.app");
            user.setId("demo");
            user.setPassword(defaultPassword);
            user.encodePassword();
            userRepository.save(user);
        }

        // =========================
        // ADMIN (1)
        // =========================
        if (!userRepository.existsById("admin")) {
            Admin admin = new Admin();
            admin.setId("admin");
            admin.setEmail("admin@sample.app");
            admin.setPassword(defaultPassword);
            admin.encodePassword();
            userRepository.save(admin);
        }

        // =========================
        // CREATORS (2)
        // =========================
        if (!userRepository.existsById("creator1")) {
            Creator c1 = new Creator();
            c1.setId("creator1");
            c1.setEmail("creator1@sample.app");
            c1.setPassword(defaultPassword);
            c1.encodePassword();
            userRepository.save(c1);
        }

        if (!userRepository.existsById("creator2")) {
            Creator c2 = new Creator();
            c2.setId("creator2");
            c2.setEmail("creator2@sample.app");
            c2.setPassword(defaultPassword);
            c2.encodePassword();
            userRepository.save(c2);
        }

    
        if (Arrays.asList(activeProfiles.split(",")).contains("test")) {

            if (!userRepository.existsById("test")) {
                User user = new User();
                user.setEmail("test@sample.app");
                user.setId("test");
                user.setPassword(defaultPassword);
                user.encodePassword();
                user = userRepository.save(user);

                Record record = new Record();
                record.setName("My test record");
                record.setDescription("A record used for testing purposes, nothing more, nothing less...");
                record.setCreated(ZonedDateTime.now());
                record.setModified(record.getCreated());
                record.setOwnedBy(user);

                recordRepository.save(record);
            }
        }
    }
}