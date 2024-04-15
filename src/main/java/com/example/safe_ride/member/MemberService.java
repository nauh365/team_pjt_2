package com.example.safe_ride.member;

import com.example.safe_ride.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepo memberRepo;

    public MemberDto readMember(String userId){
        Member member = memberRepo.findByUserId(userId)
                .orElseThrow(
                        ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")
                );
        return MemberDto.fromEntity(member);
    }
    @Transactional
    public void updateMember(String userId, UpdateDto dto) {

        Member member = memberRepo.findByUserId(userId)
                .orElseThrow(
                        ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")
                );
        //비밀번호와 비밀번호 체크가 동일한가
        if (!dto.getPassword().equals(dto.getPasswordCk())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 체크가 일치하지 않습니다.");
        }
        
        //null과 공백 체크 and 값이 변경되었다면
        if (!ObjectUtils.isEmpty(dto.getPassword()) &&
            !dto.getPassword().equals(member.getPassword())){
            member.setPassword(dto.getPassword());
        }
        if (!ObjectUtils.isEmpty(dto.getEmail()) &&
            !dto.getEmail().equals(member.getEmail())){
            member.setEmail(dto.getEmail());
        }
        if (!ObjectUtils.isEmpty(dto.getNickName()) &&
            !dto.getNickName().equals(member.getNickname())){
            member.setNickname(dto.getNickName());
        }
        if (!ObjectUtils.isEmpty(dto.getPhoneNumber()) &&
            !dto.getPhoneNumber().equals(member.getPhoneNumber())){
            member.setPhoneNumber(dto.getPhoneNumber());
        }
        if (!ObjectUtils.isEmpty(dto.getBirthday()) &&
            !dto.getBirthday().equals(member.getBirthday())){
            member.setBirthday(dto.getBirthday());
        }

        memberRepo.save(member);

    }
}
