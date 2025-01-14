package com.Tingle.G4hosp.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Tingle.G4hosp.constant.Role;
import com.Tingle.G4hosp.dto.MemberFormDto;
import com.Tingle.G4hosp.entity.Med;
import com.Tingle.G4hosp.entity.Member;
import com.Tingle.G4hosp.entity.MemberMed;
import com.Tingle.G4hosp.repository.MedRepository;
import com.Tingle.G4hosp.repository.MemberMedRepository;
import com.Tingle.G4hosp.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService{
	 private final PasswordEncoder passwordEncoder;
	 private final MemberRepository memberRepository;
	 private final MemberImgService memberImgService;
	 private final MedRepository medRepository;
	 private final MemberMedRepository memberMedRepository;

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
		 Member doctor = memberRepository.save(member);
		 if(memberFormDto.getMedId() != null) {
			 Med med = medRepository.findById(Long.parseLong(memberFormDto.getMedId())).orElseThrow(EntityNotFoundException::new);
			 MemberMed memberMed = MemberMed.createMemberMed(doctor, med);
			 memberMedRepository.save(memberMed);			 
		 }
		 System.out.println(member);
		 return doctor;
	 }

	 // 관리자 페이지 -> 고객/의사 목록 조회
	 public List<Member> getMemberList(Role role){
		 return memberRepository.findByRole(role);
	 }
	
	 public Member findByLoginid(String loginid) {
			return memberRepository.findByLoginid(loginid);
	}	
	 
	 public Member findByMemberid(Long memid) {
		 return memberRepository.findById(memid).orElseThrow(EntityNotFoundException::new);
	 }
	
	 public Member findByMnameMtel(String memberName, String memberTel) {
		 return memberRepository.findbtMnameandMtel(memberName, memberTel);
	 }
	 
}
