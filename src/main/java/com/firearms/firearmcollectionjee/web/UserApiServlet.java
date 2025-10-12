package com.firearms.firearmcollectionjee.web;

import com.firearms.firearmcollectionjee.component.DtoFunctionFactory;
import com.firearms.firearmcollectionjee.controller.api.UserControllerInterface;
import com.firearms.firearmcollectionjee.dto.user.GetUserResponse;
import com.firearms.firearmcollectionjee.dto.user.GetUsersResponse;
import com.firearms.firearmcollectionjee.dto.user.UserCreateRequestDto;
import com.firearms.firearmcollectionjee.dto.user.UserUpdateRequestDto;
import com.firearms.firearmcollectionjee.dto.user.function.RequestToUserFunction;
import com.firearms.firearmcollectionjee.dto.user.function.UpdateUserWithRequestFunction;
import com.firearms.firearmcollectionjee.dto.user.function.UserToResponseFunction;
import com.firearms.firearmcollectionjee.dto.user.function.UsersToResponseFunction;
import com.firearms.firearmcollectionjee.model.User;
import com.firearms.firearmcollectionjee.repository.impl.UserRepository;
import com.firearms.firearmcollectionjee.repository.api.UserRepositoryInterface;
import com.firearms.firearmcollectionjee.service.UserService;
import com.firearms.firearmcollectionjee.storage.DataStorage;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet exposing REST-like endpoints for User operations.
 * Patterned after provided example servlet, simplified to focus on Users.
 */
@WebServlet(urlPatterns = { UserApiServlet.Paths.API + "/*" })
@MultipartConfig(maxFileSize = 200 * 1024) // 200KB limit similar to sample
public class UserApiServlet extends HttpServlet {

    private UserControllerInterface userController;
    private DtoFunctionFactory dtoFactory;

    private RequestToUserFunction requestToUser;
    private UpdateUserWithRequestFunction updateUserWithRequest;
    private UserToResponseFunction userToResponse;
    private UsersToResponseFunction usersToResponse;

    private final Jsonb jsonb = JsonbBuilder.create();

    /**
     * Static paths.
     */
    public static final class Paths {
        public static final String API = "/api"; // base similar to example
    }

    /**
     * Regex patterns for routing.
     */
    public static final class Patterns {
        private static final Pattern UUID = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
        public static final Pattern USERS = Pattern.compile("/users/?");
        public static final Pattern USER = Pattern.compile("/users/(" + UUID.pattern() + ")");
        public static final Pattern USER_BY_LOGIN = Pattern.compile("/users/login/([a-zA-Z0-9_.-]{3,})");
        public static final Pattern USER_AVAILABILITY_LOGIN = Pattern.compile("/users/login/(.+)/available");
        public static final Pattern USER_AVAILABILITY_EMAIL = Pattern.compile("/users/email/(.+)/available");
        public static final Pattern USERS_BY_ROLE = Pattern.compile("/roles/([^/]+)/users/?");
        public static final Pattern USER_AVATAR = Pattern.compile("/users/(" + UUID.pattern() + ")/avatar");
        // Example of PATCH path just reusing USER pattern for partial update
    }

    @Override
    public void init() throws ServletException {
        super.init();
        // Expect controller & factory placed into servlet context by a bootstrap listener (not yet implemented)
        Object controllerAttr = getServletContext().getAttribute("userController");
        if (controllerAttr instanceof UserControllerInterface) {
            this.userController = (UserControllerInterface) controllerAttr;
        }
        Object factoryAttr = getServletContext().getAttribute("dtoFunctionFactory");
        if (factoryAttr instanceof DtoFunctionFactory) {
            this.dtoFactory = (DtoFunctionFactory) factoryAttr;
        } else {
            this.dtoFactory = new DtoFunctionFactory(); // fallback manual construction
        }
        this.requestToUser = dtoFactory.requestToUser();
        this.updateUserWithRequest = dtoFactory.updateUser();
        this.userToResponse = dtoFactory.userToResponse();
        this.usersToResponse = dtoFactory.usersToResponse();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = parseRequestPath(req);
        if (!Paths.API.equals(req.getServletPath())) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Avatar binary resource handled separately (content-type image)
        if (path.matches(Patterns.USER_AVATAR.pattern())) {
            UUID id = extractUuid(Patterns.USER_AVATAR, path);
            byte[] avatar = userController.getUserAvatar(id);
            if (avatar.length == 0) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Avatar not found");
                return;
            }
            resp.setContentType("image/png"); // assuming only PNG stored
            resp.setContentLength(avatar.length);
            resp.getOutputStream().write(avatar);
            return;
        }

        resp.setContentType("application/json");

        if (path.matches(Patterns.USERS.pattern())) {
            List<User> users = userController.getAllUsers().stream().toList();
            GetUsersResponse dto = usersToResponse.apply(users);
            resp.getWriter().write(jsonb.toJson(dto));
            return;
        } else if (path.matches(Patterns.USER.pattern())) {
            UUID id = extractUuid(Patterns.USER, path);
            Optional<User> user = userController.getUserById(id);
            if (user.isPresent()) {
                resp.getWriter().write(jsonb.toJson(userToResponse.apply(user.get())));
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
            return;
        } else if (path.matches(Patterns.USER_BY_LOGIN.pattern())) {
            String login = extractGroup(Patterns.USER_BY_LOGIN, path, 1);
            Optional<User> user = userController.getUserByLogin(login);
            if (user.isPresent()) {
                resp.getWriter().write(jsonb.toJson(userToResponse.apply(user.get())));
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
            return;
        } else if (path.matches(Patterns.USER_AVAILABILITY_LOGIN.pattern())) {
            String login = extractGroup(Patterns.USER_AVAILABILITY_LOGIN, path, 1);
            boolean available = userController.isLoginAvailable(login);
            resp.getWriter().write("{\"login\":\"" + escape(login) + "\",\"available\":" + available + "}");
            return;
        } else if (path.matches(Patterns.USER_AVAILABILITY_EMAIL.pattern())) {
            String email = extractGroup(Patterns.USER_AVAILABILITY_EMAIL, path, 1);
            boolean available = userController.isEmailAvailable(email);
            resp.getWriter().write("{\"email\":\"" + escape(email) + "\",\"available\":" + available + "}");
            return;
        } else if (path.matches(Patterns.USERS_BY_ROLE.pattern())) {
            String role = extractGroup(Patterns.USERS_BY_ROLE, path, 1);
            List<User> users = userController.getUsersByRole(role);
            resp.getWriter().write(jsonb.toJson(usersToResponse.apply(users)));
            return;
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = parseRequestPath(req);
        if (!Paths.API.equals(req.getServletPath())) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (path.matches(Patterns.USERS.pattern())) {
            UserCreateRequestDto createDto = jsonb.fromJson(req.getReader(), UserCreateRequestDto.class);
            User user = requestToUser.apply(createDto);
            try {
                user = userController.createUser(user);
            } catch (IllegalArgumentException ex) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return;
            }
            GetUserResponse responseDto = userToResponse.apply(user);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.addHeader("Location", createUrl(req, Paths.API, "users", user.getId().toString()));
            resp.setContentType("application/json");
            resp.getWriter().write(jsonb.toJson(responseDto));
            return;
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = parseRequestPath(req);
        if (!Paths.API.equals(req.getServletPath())) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Avatar upload
        if (path.matches(Patterns.USER_AVATAR.pattern())) {
            UUID id = extractUuid(Patterns.USER_AVATAR, path);
            if (req.getContentType() != null && req.getContentType().startsWith("multipart/")) {
                // Expect form field name 'avatar'
                var part = req.getPart("avatar");
                if (part == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing avatar part");
                    return;
                }
                userController.putUserAvatar(id, part.getInputStream());
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            } else {
                // Allow raw binary PUT (no multipart) as alternative
                userController.putUserAvatar(id, req.getInputStream());
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
        }

        if (!path.matches(Patterns.USER.pattern())) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        UUID id = extractUuid(Patterns.USER, path);
        UserUpdateRequestDto updateDto = jsonb.fromJson(req.getReader(), UserUpdateRequestDto.class);
        if (updateDto.getId() == null) {
            updateDto.setId(id); // enforce path id
        }
        Optional<User> existing = userController.getUserById(id);
        if (existing.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }
        User replacement = new User();
        replacement.setId(id);
        replacement.setLogin(updateDto.getLogin());
        replacement.setEmail(updateDto.getEmail());
        replacement.setBirthDate(updateDto.getBirthDate());
        replacement.setRoles(updateDto.getRoles());
        try {
            replacement = userController.updateUser(replacement);
        } catch (IllegalArgumentException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            return;
        }
        resp.setContentType("application/json");
        resp.getWriter().write(jsonb.toJson(userToResponse.apply(replacement)));
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = parseRequestPath(req);
        if (!Paths.API.equals(req.getServletPath()) || !path.matches(Patterns.USER.pattern())) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        UUID id = extractUuid(Patterns.USER, path);
        UserUpdateRequestDto patchDto = jsonb.fromJson(req.getReader(), UserUpdateRequestDto.class);
        Optional<User> existing = userController.getUserById(id);
        if (existing.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }
        User updated = updateUserWithRequest.apply(patchDto, existing.get());
        try {
            updated = userController.updateUser(updated);
        } catch (IllegalArgumentException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            return;
        }
        resp.setContentType("application/json");
        resp.getWriter().write(jsonb.toJson(userToResponse.apply(updated)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = parseRequestPath(req);
        if (!Paths.API.equals(req.getServletPath())) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (path.matches(Patterns.USER_AVATAR.pattern())) {
            UUID id = extractUuid(Patterns.USER_AVATAR, path);
            // If avatar absent, treat as no-op (or could return 404) - choosing id existence check
            Optional<User> u = userController.getUserById(id);
            if (u.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }
            userController.deleteUserAvatar(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        if (!path.matches(Patterns.USER.pattern())) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        UUID id = extractUuid(Patterns.USER, path);
        boolean deleted = userController.deleteUser(id);
        if (!deleted) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }

    private static UUID extractUuid(Pattern pattern, String path) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            return UUID.fromString(matcher.group(1));
        }
        throw new IllegalArgumentException("No UUID in path.");
    }

    private static String extractGroup(Pattern pattern, String path, int group) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            return matcher.group(group);
        }
        throw new IllegalArgumentException("No group in path.");
    }

    private String parseRequestPath(HttpServletRequest request) {
        String p = request.getPathInfo();
        return p != null ? p : "";
    }

    public static String createUrl(HttpServletRequest request, String... paths) {
        StringBuilder builder = new StringBuilder();
        builder.append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append(request.getContextPath());
        for (String path : paths) {
            builder.append("/")
                    .append(path, path.startsWith("/") ? 1 : 0, path.endsWith("/") ? path.length() - 1 : path.length());
        }
        return builder.toString();
    }

    private static String escape(String v) { return v.replace("\"", "\\\""); }
}
