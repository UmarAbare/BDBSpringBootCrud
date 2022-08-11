// 5 of
package net.bdb;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {

	@Autowired
	private UserRepository repo;

	@GetMapping("")
	public String viewHomePage() {

		return "BDB_Land";
	}

	@GetMapping("/register")
	public String showSignUpForm(Model model) {

		model.addAttribute("user", new User());

		return "BDB_Create";
	}

	@PostMapping("/process_register")
	public String processRegistration(User user) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		repo.save(user);

		return "register_success";

	}

	@GetMapping("/list_users")
	public String viewUsersList(Model model) {
		List<User> listUsers = repo.findAll();

		model.addAttribute("listUsers", listUsers);
		return "users";
	}

	@GetMapping("/update/{id}")
	public ModelAndView showUpdateForm(@PathVariable(name = "id") Long id) {

		ModelAndView modelAndView = new ModelAndView("update_form");
		Optional<User> user = repo.findById(id);

		modelAndView.addObject("user", user);

		return modelAndView;
	}

	@PostMapping("/process_update")

	public String processUpdate(User user) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		repo.save(user);

		return "redirect:/list_users";

	}

	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Long id) {
		repo.deleteById(id);
		return "redirect:/list_users";
	}

}
