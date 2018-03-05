package cz.martinkorecek.colab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cz.martinkorecek.colab.entity.User;
import cz.martinkorecek.colab.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${security.encoding-strength}")
	private Integer encodingStrength;
	
	// takhle to není správně zabezpečeno (pro můj maturitní projekt to snad nevadí, ale na případnou produkci rozhodně potřeba opravit!)
	@RequestMapping(value = "/persistUser", method = RequestMethod.POST)
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public ResponseEntity<String> persistUser(@RequestBody User user) {
		//String passwordHash = new ShaPasswordEncoder(encodingStrength).encodePassword(user.getPasswordHash(), user.getPasswordHash());
		String passwordHash = new ShaPasswordEncoder(encodingStrength).encodePassword(user.getPasswordHash(), null);
		userRepository.insertUser(user.getUsername(), passwordHash);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
