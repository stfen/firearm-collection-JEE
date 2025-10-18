package com.firearms.firearmcollectionjee.web.observer;

import java.io.InputStream;
import java.util.UUID;

import com.firearms.firearmcollectionjee.model.User;
import com.firearms.firearmcollectionjee.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.control.RequestContextController;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.SneakyThrows;

@ApplicationScoped
public class InitializedData implements ServletContextListener {
    private final UserService userService;
    private final RequestContextController requestContextController;

    @Inject
    public InitializedData(UserService userService, RequestContextController requestContextController) {
        this.userService = userService;
        this.requestContextController = requestContextController;
    }

    public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object init) {
        init();
    }

    @SneakyThrows
    private void init() {
        requestContextController.activate();
        User user1 = User.builder()
                .id(UUID.fromString("178960ce-f5bf-4e54-82f3-8b10a69d7cce"))
                .login("ziomus")
                .email("ziomus@example.com")
                .build();

        User user2 = User.builder()
                .id(UUID.fromString("c461d210-8cea-4213-a19a-3cb856351d56"))
                .login("uzytnik2")
                .email("uzytnik2@example.com")
                .build();

        User user3 = User.builder()
                .id(UUID.fromString("70b21553-3690-4464-97a3-4481ce862b28"))
                .login("gracz2")
                .email("gracz2@example.com")
                .build();

        User user4 = User.builder()
                .id(UUID.fromString("70b21512-1234-5678-97a3-4481ce862b26"))
                .login("seima")
                .email("seima@example.com")
                .build();

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        userService.createUser(user4);
    }

    @SneakyThrows
    private byte[] getResourceAsByteArray(String path) {
        try (InputStream is = this.getClass().getResourceAsStream(path)) {
            if (is != null) {
                return is.readAllBytes();
            } else {
                throw new IllegalStateException("Unable to get resource %s".formatted(path));
            }
        }
    }

}
