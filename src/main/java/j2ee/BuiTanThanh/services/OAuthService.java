package j2ee.BuiTanThanh.services;

import j2ee.BuiTanThanh.constants.Provider;
import j2ee.BuiTanThanh.entities.User;
import j2ee.BuiTanThanh.entities.Role;
import j2ee.BuiTanThanh.repositories.IRoleRepository;
import j2ee.BuiTanThanh.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OAuthService extends DefaultOAuth2UserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setUsername(email);
            user.setEmail(email);
            user.setName(name);
            user.setPassword(new BCryptPasswordEncoder().encode("123456"));
            user.setProvider(Provider.GOOGLE.value);

            Role userRole = roleRepository.findRoleByName("USER");
            if (userRole != null) {
                user.getRoles().add(userRole);
            }
            userRepository.save(user);
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });

        return new DefaultOAuth2User(
                authorities,
                oauth2User.getAttributes(),
                "email");
    }
}
