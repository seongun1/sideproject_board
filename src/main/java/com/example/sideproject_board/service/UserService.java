package com.example.sideproject_board.service;

import com.example.sideproject_board.dto.JoinRequest;
import com.example.sideproject_board.dto.LoginRequest;
import com.example.sideproject_board.dto.UserDto;
import com.example.sideproject_board.domain.User;
import com.example.sideproject_board.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;
//
//로그인 ID 중복 검사 메서드
//회원가입 메서드
//로그인 메서드
//로그인한 Member 반환 메서드

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    private final EntityManager em;


    public void join(JoinRequest joinRequest){
        userRepository.save(joinRequest.toEntity());
    }
    public void securityJoin(JoinRequest joinRequest){
        if(userRepository.existsByLoginId(joinRequest.getLoginId())){
            return;
        }

        joinRequest.setPassword(encoder.encode(joinRequest.getPassword()));

        userRepository.save(joinRequest.toEntity());
    }
//    public void save (User user){
//        em.persist(user);
//    }
//    public User findOne(String username){
//        return em.find(User.class,username);
//    }
//    public List<User> findAll(){
//        return em.createQuery("select m from User m" , User.class)
//                .getResultList();
//    }
    // 중복확인
    public boolean checkIsDuplicate (String loginId){
        return userRepository.existsByLoginId(loginId);
    }

    // 회원가입
    @Transactional
    public void securityUserJoin(JoinRequest joinRequest){
        if(userRepository.existsByLoginId(joinRequest.getLoginId())){
            return;
        }
        joinRequest.setPassword(encoder.encode(joinRequest.getPassword()));

        userRepository.save(joinRequest.toEntity());
    }
    @Transactional(readOnly = true)
    public Map<String , String> validateHandling (Errors errors){
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()){
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName,error.getDefaultMessage());
        }
        return validatorResult;
    }

    //회원수정
    @Transactional
    public void modify(UserDto.Request dto){
        User user = userRepository.findById(dto.toEntity().getId()).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        String encPassword = encoder.encode(dto.toEntity().getPassword());
        user.modify(dto.toEntity().getNickname(),encPassword);
    }

    //로그인
    public User login(LoginRequest loginRequest){
        User findUser = userRepository.findByLoginId(loginRequest.getLoginId()).orElseThrow(()
                -> new UsernameNotFoundException("사용자가 없습니다.(No Such User)"));

        // 로그인 실패시
        if (findUser == null){
            return null;
        }
        if (!findUser.getPassword().equals(loginRequest.getPassword())){
            return null;
        }
        // 로그인 성공
        return findUser;
    }
    public User getLoginUserById(Long userId){
        if (userId == null) return null;

        Optional<User> findUser = userRepository.findById(userId);
        return findUser.orElse(null);
    }
    public User getLoginUserByloginId(String loginId){
        if (loginId == null) return null;

        Optional<User> findUser = userRepository.findByLoginId(loginId);
        return findUser.orElse(null);
    }
}
