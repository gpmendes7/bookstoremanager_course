package com.rodrigopeleias.bookstoremanager.users.utils;

import com.rodrigopeleias.bookstoremanager.users.dto.MessageDTO;
import com.rodrigopeleias.bookstoremanager.users.entity.User;

public class MessageDTOUtils {

    public static MessageDTO creationMessage(User createdUser) {
        return returnMessage(createdUser, "created");
    }

    public static MessageDTO updatedMessage(User updatedUser) {
        return returnMessage(updatedUser, "updated");
    }

    public static MessageDTO returnMessage(User updatedUser, String action) {
        String createdUserUsername = updatedUser.getUsername();
        Long createdId = updatedUser.getId();
        String createdUserMessage = String.format("User %s with ID %d successfully %s", createdUserUsername, createdId, action);
        return MessageDTO.builder()
                .message(
                        createdUserMessage)
                .build();
    }

}
