package me.jibajo.dream_shop.service.user;

import me.jibajo.dream_shop.dto.UserDTO;
import me.jibajo.dream_shop.exception.AlreadyExistsException;
import me.jibajo.dream_shop.exception.ResourceNotFoundException;
import me.jibajo.dream_shop.model.User;
import me.jibajo.dream_shop.repository.UserRepository;
import me.jibajo.dream_shop.requests.CreateUserRequest;
import me.jibajo.dream_shop.requests.UpdateUserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        return Optional.of(createUserRequest)
                .filter(user -> !userRepository.existsByEmail(createUserRequest.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstName(createUserRequest.getFirstName());
                    user.setLastName(createUserRequest.getLastName());
                    user.setEmail(createUserRequest.getEmail());
                    user.setPassword(createUserRequest.getPassword());
                    return userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistsException(createUserRequest.getEmail()+" User already exists"));
    }

    @Override
    public User updateUser(UpdateUserRequest updateUserRequest, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(updateUserRequest.getFirstName());
            existingUser.setLastName(updateUserRequest.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new ResourceNotFoundException("User not found");
        });
    }

    @Override
    public UserDTO convertUserToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
