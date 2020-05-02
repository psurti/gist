package gist.mockito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class UserServiceTest {

    @Mock
    UserManager userManager;

    @DisplayName("Use of Mockito for saving UserManager")
    @Test
    void save() {
        final String name = "Prashant";
        UserService userService = new UserService(userManager);
        userService.save(name);

        Mockito.verify(userManager, Mockito.times(1)).save(Mockito.anyString());
        Mockito.verify(userManager, Mockito.times(1)).save(Mockito.isA(String.class));
        Mockito.verify(userManager, Mockito.times(1)).save(Mockito.startsWith("Pr"));
        Mockito.verify(userManager, Mockito.times(1)).save(Mockito.endsWith("nt"));
    }
}