package cz.martinkorecek.colab.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cz.martinkorecek.colab.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${security.encoding-strength}")
	private Integer encodingStrength;
	
	//TODO nejak rozumneji poresit pokud username nebude unique, takhle to prostě vyhodí chybu
	///////+ obecně takhle to zkrátka není správně zabezpečeno
	@RequestMapping(value = "/persistUser", method = RequestMethod.POST)
	public ResponseEntity<String> insertUser(@RequestBody Map<String, Object> body) {
		String passwordHash = new ShaPasswordEncoder(encodingStrength).encodePassword((String) body.get("password"), body.get("password"));
		userRepository.insertUser((String)(body.get("username")), passwordHash);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
