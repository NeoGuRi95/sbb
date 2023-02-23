package com.mysite.sbb.user;

import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mysite.sbb.CommonUtil;
import com.mysite.sbb.exception.ErrorCode;
import com.mysite.sbb.exception.UserDataIntegrityViolationException;
import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.exception.EmailException;
import com.mysite.sbb.mail.TempPasswordMail;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TempPasswordMail tempPasswordMail;
    private final CommonUtil commonUtil;

    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        try {
            this.userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserDataIntegrityViolationException(ErrorCode.SIGN_UP_FAIL);
        }
        return user;
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser =
            this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }

    @Transactional
    public void modifyPassword(String email) throws EmailException {
        String tempPassword = commonUtil.createTempPassword();
        SiteUser user = userRepository.findByEmail(email)
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND_BY_EMAIL));
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);
        tempPasswordMail.sendSimpleMessage(email, tempPassword);
    }
}
