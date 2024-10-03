package me.jibajo.dream_shop.service.user;

import me.jibajo.dream_shop.dto.UserDTO;
import me.jibajo.dream_shop.model.User;
import me.jibajo.dream_shop.requests.CreateUserRequest;
import me.jibajo.dream_shop.requests.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest createUserRequest);
    User updateUser(UpdateUserRequest updateUserRequest, Long userId);
    void deleteUser(Long userId);

    UserDTO convertUserToDTO(User user);

    User getAuthenticatedUser();
}
