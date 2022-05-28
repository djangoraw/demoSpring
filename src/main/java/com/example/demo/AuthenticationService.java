package com.example.demo;

import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.requests.UserLoginRequest;
import com.example.demo.requests.UserRegisterRequest;
import com.example.demo.responses.UserActionResponse;
import com.example.demo.responses.UserCheckResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UsersRepository usersRepository;

    public boolean isUserLogged(HttpSession session) {
        Long loggedUserId = this.getLoggedUserId(session);
        return loggedUserId != null;
    }

    public boolean isUserLogged(HttpServletRequest request) {
        return this.isUserLogged(request.getSession());
    }

    public Long getLoggedUserId(HttpSession session) {
        return (Long) session.getAttribute("LOGGED_USER_ID");
    }

    public Long getLoggedUserId(HttpServletRequest request) {
        return this.getLoggedUserId(request.getSession());
    }

    public UserCheckResponse checkUser(HttpServletRequest request) {
        Long loggedUserId = getLoggedUserId(request);
        if (loggedUserId == null) {
            return new UserCheckResponse(false, "Currently, You are not logged in.", null);
        }
        Optional<UserEntity> userOptional = this.usersRepository.findById(loggedUserId);
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            return new UserCheckResponse(true, "Currently, You are logged in.", userEntity);
        } else {
            return new UserCheckResponse(false, "Logged in user doesn't exists.", null);
        }
    }

    public UserActionResponse loginUser(HttpServletRequest request, UserLoginRequest data) {
        HttpSession session = request.getSession();
        if (this.isUserLogged(session)) {
            return new UserActionResponse(false, "Login operation failed (You are already logged in).");
        }
        Optional<UserEntity> userOptional = this.usersRepository.findByUsernameAndPassword(data.getUsername(), data.getPassword());
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            session.setAttribute("LOGGED_USER_ID", userEntity.getId());
            return new UserActionResponse(true, "Login operation succeed.");
        }
        return new UserActionResponse(false, "Login operation failed (Incorrect user credentials).");
    }

    public UserActionResponse logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (isUserLogged(session)) {
            session.removeAttribute("LOGGED_USER_ID");
            return new UserActionResponse(true, "Logout operation succeed.");
        }
        return new UserActionResponse(false, "Logout operation failed (Currently, You are not logged in).");
    }

    public UserActionResponse registerUser(UserRegisterRequest data) {
        UserEntity userEntity = new UserEntity(null, data.getUsername(), data.getPassword(), data.getEmail());
        try {
            this.usersRepository.save(userEntity);
            return new UserActionResponse(true, "Register operation succeed.");
        } catch (Throwable ex) {
            if (ex instanceof DataIntegrityViolationException) {
                return new UserActionResponse(false, "Register operation failed (username or email is used already by someone).");
            }
            return new UserActionResponse(false, "Register operation failed.");
        }
    }
}