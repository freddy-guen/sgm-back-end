package fr.guen.dev.sgm.common.mapper;

import fr.guen.dev.sgm.models.User;
import fr.guen.dev.sgm.payload.common.UserInfoDTO;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    private UserMapper(){}

    public static UserInfoDTO mapUserToUserInfoDTO(User user) {
        return new UserInfoDTO(
                user.getFirsName(),
                user.getLastName(),
                user.getContactNumber(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );
    }

    /*public static User mapUserInfoDtoToUser(UserInfoDTO userInfoDTO) {
        return new User(
                user.getFirsName(),
                user.getLastName(),
                user.getContactNumber(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );
    }*/

    public static List<UserInfoDTO> mepUserListToUserInfoDtoList(List<User> users){
        List<UserInfoDTO> userInfoDTOList = new ArrayList<>();
        for (User user : users) {
            userInfoDTOList.add(mapUserToUserInfoDTO(user));
        }
        return userInfoDTOList;
    }
}
