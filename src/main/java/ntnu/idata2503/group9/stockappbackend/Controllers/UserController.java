package ntnu.idata2503.group9.stockappbackend.Controllers;

import ntnu.idata2503.group9.stockappbackend.Models.User;
import ntnu.idata2503.group9.stockappbackend.Security.JwtUtil;
import ntnu.idata2503.group9.stockappbackend.Services.AccessUserService;
import ntnu.idata2503.group9.stockappbackend.Services.UserService;
import ntnu.idata2503.group9.stockappbackend.dto.AuthenticationRequest;
import ntnu.idata2503.group9.stockappbackend.dto.AuthenticationResponse;
import ntnu.idata2503.group9.stockappbackend.dto.RegisterUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.json.JSONException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Rest controller that controls the endpoints for the user.
 *
 * @author Gruppe 4
 * @version 1.0
 */
@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AccessUserService accessUserService;

    private static final String JSONEEXCEPTIONMESSAGE = "The Field(s) in the request is missing or is null";
    private static final String SEVERE = "An error occurred: ";
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    /**
     * Endpoint that returns all users.
     * 
     * @return all users
     */
    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        Iterable<User> users = this.userService.getAll();
        if (!users.iterator().hasNext()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok((List<User>) users);
    }

    /**
     * Endpoint that returns a user based on the user id
     * 
     * @param id the id of the user that you want to return
     * @return the user and HTTP status OK or http status NOT_FOUNd if user was not
     *         found
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserFromId(@PathVariable long id) {
        User user = this.userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Endpoint that returns user based on the user email
     * 
     * @param email the email of the user that you want to return
     * @return The user and HTTP status OK, or HTTPS status NOT_FOUND if the user
     *         was not found
     */
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserFromEmail(@PathVariable String email) {
        User user = this.userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Endpoint that creates a new user.
     * 
     * @param registerUserDto the body of user that you want to create.
     * @return HTTP status CREATED if created, if not the INTERNAL_SERVER_ERROR.
     * @exception JSONException if an error occurs while creating the user.
     */
    @PostMapping("")
    public ResponseEntity<String> createUser(@RequestBody RegisterUserDto registerUserDto) {
        try {
            String errorMessage = accessUserService.tryCreateNewUser(registerUserDto.getEmail(),
                    registerUserDto.getPassword());
            ResponseEntity<String> response;
            if (errorMessage == null) {
                response = ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                response = ResponseEntity.badRequest().body(errorMessage);
            }
            return response;
        } catch (JSONException e) {
            LOGGER.severe(SEVERE + e.getMessage());
            return ResponseEntity.badRequest().body(JSONEEXCEPTIONMESSAGE);
        }
    }

    /**
     * Endpoint that update a user.
     * 
     * @param id   the id of the user that you want to update.
     * @param user the new user that you want the old user to be updated to.
     * @return HTTP status OK if updated, if not INTERNAL_SERVER_ERROR.
     * @exception JSONException if an error occurs while updating the user.
     */
    @PutMapping("")
    public ResponseEntity<String> updateUser(@PathVariable long id, @RequestBody User user) {
        try {
            User oldUser = this.userService.findById(id);
            if (oldUser == null) {
                return ResponseEntity.notFound().build();
            }
            this.userService.update(id, user);
            if (this.userService.findById(id) == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User didn't update");
            }
            return ResponseEntity.ok("User was updated");
        } catch (JSONException e) {
            LOGGER.severe(SEVERE + e.getMessage());
            return ResponseEntity.badRequest().body(JSONEEXCEPTIONMESSAGE);
        }
    }

    /**
     * Endpoint that deletes a user.
     * 
     * @param id the id of the user that you want to delete
     * @return HTTP status OK if deleted, if not INTERNAL_SERVER_ERROR.
     * @exception JSONException if an error occurs while deleting the user.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        try {
            if (!this.userService.delete(id)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User was not removed");
            }
            return ResponseEntity.ok("User was removed");
        } catch (JSONException e) {
            LOGGER.severe(SEVERE + e.getMessage());
            return ResponseEntity.badRequest().body(JSONEEXCEPTIONMESSAGE);
        }
    }

    /**
     * HTTP POST request to /authenticate
     *
     * @param authenticationRequest The request JSON object containing username and
     *                              password
     * @return OK + JWT token; Or UNAUTHORIZED
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
        final UserDetails userDetails = accessUserService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    /**
     * HTTP GET request to /sessionuser
     * 
     * @return OK + User object; Or UNAUTHORIZED
     */
    @GetMapping("/sessionuser")
    public ResponseEntity<?> getSesionUser() {
        User sessionUser = this.accessUserService.getSessionUser();
        System.out.println(sessionUser);
        if (sessionUser == null) {
            return new ResponseEntity<>("Didnt find user", HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(sessionUser);
    }
}
