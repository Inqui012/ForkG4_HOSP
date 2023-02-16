package com.Tingle.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		//로그인한 사용자의 정보를 등록자와 수정자로 지정합니다.
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = "";
		
		if (authentication != null) {
			userId = authentication.getName(); //사용자의 이름을 가져옵니다.
		}
		
		return Optional.of(userId); //사용자의 이름(userId)을 등록자와 수정자로 지정해줍니다.
	}

}
