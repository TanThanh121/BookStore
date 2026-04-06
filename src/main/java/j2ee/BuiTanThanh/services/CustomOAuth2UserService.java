package j2ee.BuiTanThanh.services;

import j2ee.BuiTanThanh.constants.Provider;
import j2ee.BuiTanThanh.entities.User;
import j2ee.BuiTanThanh.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final IUserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        User user = userRepository.findByUsername(email).orElse(null);
        if (user == null) {
            user = new User();
            user.setUsername(email);
            user.setEmail(email);
            // Google users don't have a local password, set a dummy one or leave empty if
            // your DB allows
            user.setPassword("");
            user.setProvider(Provider.GOOGLE.value);
            userRepository.save(user);
        }
        return oauth2User;
    }
}
