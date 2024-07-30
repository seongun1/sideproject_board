package com.example.sideproject_board.service;

import com.example.sideproject_board.dto.UserDto;
import com.example.sideproject_board.domain.User;
import com.example.sideproject_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    // 회원가입
    @Transactional
    public void userJoin(UserDto.Request dto){
        dto.setPassword(encoder.encode(dto.getPassword()));

        userRepository.save(dto.toEntity());
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

}
