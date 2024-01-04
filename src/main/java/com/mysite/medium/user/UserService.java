package com.mysite.medium.user;

import com.mysite.medium.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    public void generatePaidMemberships() {
        for (int i = 0; i < 100; i++) {
            SiteUser paidMember = new SiteUser();
            paidMember.setUsername("paid_member_" + i);
            paidMember.setPassword(passwordEncoder.encode("password")); // 적절한 패스워드 인코딩 로직 사용
            paidMember.setPaidMember(true);
            userRepository.save(paidMember);
        }
    }
}