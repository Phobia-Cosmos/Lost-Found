package org.hnust.context;

import org.hnust.dto.UserDTO;

public class BaseContext {

    public static ThreadLocal<UserDTO> threadLocal = new ThreadLocal<>();

    public static void setCurrentUser(UserDTO user) {
        threadLocal.set(user);
    }


    public static UserDTO getCurrentUser() {
        return threadLocal.get();
    }

    public static void removeCurrentUser() {
        threadLocal.remove();
    }

}
