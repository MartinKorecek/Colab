package cz.martinkorecek.colab.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import cz.martinkorecek.colab.entity.User;
import cz.martinkorecek.colab.repository.UserRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {  //psáno podle tutoriálu https://medium.com/@juliapassynkova/angular-springboot-jwt-integration-p-1-800a337a4e0

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(s);
		
		if (user == null) {
			throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("STANDARD_USER"));
		
		UserDetails userDetails = new org.springframework.security.core.userdetails.
                User(user.getUsername(), user.getPasswordHash(), authorities);    //POZOR; zatím jsem se rozhodl neimplementovat typy autority, proto vkládám 
																		          //zatím defaultně autoritu 'standard_user'

        return userDetails;
	}

}
