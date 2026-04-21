package com.loan.security;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.loan.constants.Role;
import com.loan.entity.User;

public class CustomUserDetail implements UserDetails{
	
	//db entity/user
			private User user;		

	public CustomUserDetail(User user) {
				super();
				this.user = user;
			}
	
	 public User getUser() {
	        return this.user;
	    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		
		Role role = user.getRole();
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public @Nullable String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
