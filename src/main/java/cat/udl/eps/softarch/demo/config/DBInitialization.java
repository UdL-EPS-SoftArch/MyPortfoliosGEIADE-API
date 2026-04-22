package cat.udl.eps.softarch.demo.config;
import cat.udl.eps.softarch.demo.domain.Record;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.RecordRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import cat.udl.eps.softarch.demo.domain.Admin;
import cat.udl.eps.softarch.demo.domain.Creator;
import cat.udl.eps.softarch.demo.domain.Profile;
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


    if (!userRepository.existsById("demo")) {
        User user = new User();
        user.setEmail("demo@sample.app");
        user.setId("demo");
        user.setPassword(defaultPassword);
        user.encodePassword();
        userRepository.save(user);
    }

    if (!userRepository.existsById("admin")) {
        Admin admin = new Admin();
        admin.setEmail("admin@sample.app");
        admin.setId("admin");
        admin.setPassword(defaultPassword);
        admin.encodePassword();
        userRepository.save(admin);
    }

    if (!userRepository.existsById("creator")) {
        Creator creator = new Creator();
        creator.setEmail("seed-creator@sample.app");
        creator.setId("creator");
        creator.setPassword(defaultPassword);
        creator.encodePassword();
        creator.setEnabled(true);
        Profile profile = new Profile();
        profile.setDescription("");
        profile.setVisibility(Profile.Visibility.PRIVATE);
        creator.setProfile(profile);
        userRepository.save(creator);
    }

    // ======================
    // 🧪 TEST DATA (tu lógica)
    // ======================
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
            record.setDescription("A record used for testing purposes...");
            record.setCreated(ZonedDateTime.now());
            record.setModified(record.getCreated());
            record.setOwnedBy(user);
            recordRepository.save(record);
        }
    }
}
}
