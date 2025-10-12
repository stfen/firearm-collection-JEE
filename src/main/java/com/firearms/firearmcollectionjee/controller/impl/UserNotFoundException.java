package com.firearms.firearmcollectionjee.controller.impl;

/**
 * Runtime exception thrown when a user with a given id does not exist.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(java.util.UUID id) {
        super("User not found: " + id);
    }
}
