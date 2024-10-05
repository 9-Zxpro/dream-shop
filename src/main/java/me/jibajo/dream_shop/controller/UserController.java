package me.jibajo.dream_shop.controller;

import me.jibajo.dream_shop.dto.UserDTO;
import me.jibajo.dream_shop.exception.AlreadyExistsException;
import me.jibajo.dream_shop.exception.ResourceNotFoundException;
import me.jibajo.dream_shop.model.User;
import me.jibajo.dream_shop.requests.CreateUserRequest;
import me.jibajo.dream_shop.requests.UpdateUserRequest;
import me.jibajo.dream_shop.response.APIResponse;
import me.jibajo.dream_shop.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api-prefix}/users")
public class UserController {
    @Autowired
    private IUserService iUserService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<APIResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = iUserService.getUserById(userId);
            UserDTO userDTO = iUserService.convertUserToDTO(user);
            return ResponseEntity.ok(new APIResponse("Success", userDTO));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<APIResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        try {
            User user = iUserService.createUser(createUserRequest);
            UserDTO userDTO = iUserService.convertUserToDTO(user);
            return ResponseEntity.ok(new APIResponse("User Created Successfully", userDTO));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<APIResponse> updateUser(@RequestBody UpdateUserRequest updateUserRequest, @PathVariable Long userId) {
        try {
            User user = iUserService.updateUser(updateUserRequest, userId);
            UserDTO userDTO = iUserService.convertUserToDTO(user);
            return ResponseEntity.ok(new APIResponse("User updated Successfully", userDTO));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(CONFLICT).body(new APIResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable Long userId) {
        try {
            iUserService.deleteUser(userId);
            return ResponseEntity.ok(new APIResponse("User deleted Successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(CONFLICT).body(new APIResponse(e.getMessage(), null));
        }
    }
}
