package lt.sveikata.user;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lt.sveikata.user.UserRepository;

import lt.sveikata.user.User;

@RestController
@RequestMapping("/authorized-user")
@CrossOrigin("*")
public class AuthorizedUserController {

	@Autowired
	private UserRepository userRepository;
	
	private ModelMapper modelMapper = new ModelMapper();


	@RequestMapping("/get-user-infos/{username}")
	public ResponseEntity<?> getLoggedUser(@PathVariable("username") String username) {
		User loggedUser = userRepository.findByUserName(username);
		if (loggedUser == null)
			return ResponseEntity.notFound().build();
		else
			return ResponseEntity.ok(modelMapper.map(loggedUser, UserForClient.class));
	}
	

}