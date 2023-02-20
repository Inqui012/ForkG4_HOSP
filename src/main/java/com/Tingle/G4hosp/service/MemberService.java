package com.Tingle.G4hosp.service;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Tingle.G4hosp.dto.MemberFormDto;
import com.Tingle.G4hosp.entity.Member;
import com.Tingle.G4hosp.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService{
	 private final PasswordEncoder passwordEncoder;
	 private final MemberRepository memberRepository;
	 private final MemberImgService memberImgService;

	@Override
	public UserDetails loadUserByUsername(String loginid) throws UsernameNotFoundException {
		Member member = memberRepository.findByLoginid(loginid);
		
		if(member == null) {
			throw new UsernameNotFoundException(loginid);
		}
		return User.builder()
				.username(member.getLoginid())
				.password(member.getPwd())
				.roles(member.getRole().toString())
				.build();
	}
	 
	 public Member saveMember(MemberFormDto memberFormDto, MultipartFile file) throws Exception {
		 Member member = Member.createMember(memberFormDto, passwordEncoder);
		 memberImgService.saveMemberImg(member, file);
		 memberRepository.save(member);
		 System.out.println(member);
		 return member ;
	 }

	
	 public Member findByLoginid(String loginid) {
			return memberRepository.findByLoginid(loginid);
	}	
	
	 
	 
}
