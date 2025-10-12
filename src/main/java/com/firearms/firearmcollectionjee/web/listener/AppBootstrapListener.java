package com.firearms.firearmcollectionjee.web.listener;

import com.firearms.firearmcollectionjee.component.DtoFunctionFactory;
import com.firearms.firearmcollectionjee.controller.impl.UserController;
import com.firearms.firearmcollectionjee.model.User;
import com.firearms.firearmcollectionjee.repository.api.UserRepositoryInterface;
import com.firearms.firearmcollectionjee.repository.impl.UserRepository;
import com.firearms.firearmcollectionjee.service.UserService;
import com.firearms.firearmcollectionjee.storage.DataStorage;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.time.LocalDate;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Application bootstrap listener responsible for:
 *  - creating shared infrastructure components (DataStorage, Repository, Service, Controller)
 *  - creating DTO function factory
 *  - seeding initial demo users (4 users) into in-memory storage via the service layer
 *
 * Having a single listener keeps ordering deterministic without needing web.xml listener ordering.
 */
@WebListener
public class AppBootstrapListener implements ServletContextListener {

    public static final String ATTR_DATA_STORAGE = "dataStorage";
    public static final String ATTR_USER_REPOSITORY = "userRepository";
    public static final String ATTR_USER_SERVICE = "userService";
    public static final String ATTR_USER_CONTROLLER = "userController";
    public static final String ATTR_DTO_FUNCTION_FACTORY = "dtoFunctionFactory";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        // Create core components
        DataStorage storage = new DataStorage();
        UserRepositoryInterface userRepository = new UserRepository(storage);
        UserService userService = new UserService(userRepository, ctx.getInitParameter("avatar.paths"));

        UserController userController = new UserController(userService);
        DtoFunctionFactory dtoFactory = new DtoFunctionFactory();

        // Register in context
        ctx.setAttribute(ATTR_DATA_STORAGE, storage);
        ctx.setAttribute(ATTR_USER_REPOSITORY, userRepository);
        ctx.setAttribute(ATTR_USER_SERVICE, userService);
        ctx.setAttribute(ATTR_USER_CONTROLLER, userController);
        ctx.setAttribute(ATTR_DTO_FUNCTION_FACTORY, dtoFactory);

        // Seed initial users (avoid duplicates if restarted in same JVM)
        seedUsers(userService);
    }

    private void seedUsers(UserService userService) {
        // Simple check: if any users exist, skip seeding
        if (userService.getUserCount() > 0) {
            return;
        }

        List<User> seeds = Arrays.asList(
                buildUser("admin", "admin@example.com", LocalDate.of(1985, 1, 10), List.of("ADMIN","USER")),
                buildUser("john", "john.doe@example.com", LocalDate.of(1992, 5, 23), List.of("USER")),
                buildUser("jane", "jane.doe@example.com", LocalDate.of(1994, 8, 14), List.of("USER","MODERATOR")),
                buildUser("guest", "guest@example.com", null, List.of("GUEST"))
        );

        seeds.forEach(u -> {
            try {
                userService.createUser(u);
            } catch (IllegalArgumentException ex) {
                // Ignore if any validation issue occurs (shouldn't with provided data)
                System.err.println("Seed user creation failed: " + ex.getMessage());
            }
        });
        System.out.println("[AppBootstrapListener] Seeded " + userService.getUserCount() + " users.");
    }

    private User buildUser(String login, String email, LocalDate birthDate, List<String> roles) {
        // Avoid relying on Lombok builder to reduce risk if annotation processing misbehaves.
        User u = new User();
        u.setId(UUID.randomUUID());
        u.setLogin(login);
        u.setEmail(email);
        u.setBirthDate(birthDate);
        u.setRoles(roles);
        u.setAvatarPath("");
        return u;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Nothing special; GC will reclaim in-memory storage.
    }
}
