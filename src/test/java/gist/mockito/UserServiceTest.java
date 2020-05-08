package gist.mockito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class UserServiceTest {

    @Mock
    UserManager userManager;

    @DisplayName("Use of Mockito for saving UserManager.")
    @Test
    void save() {
        final String name = "foobar";
        UserService userService = new UserService(userManager);
        userService.save(name);

        verify(userManager, times(1)).save(anyString());
        verify(userManager, times(1)).save(isA(String.class));
        verify(userManager, times(1)).save(startsWith("fo"));
        verify(userManager, times(1)).save(endsWith("ar"));
    }
}