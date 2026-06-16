package com.wutao.dormItem.ui.web.auth;

import com.wutao.dormItem.domain.enums.UserRole;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
@ConditionalOnProperty(name = "app.ui.mode", havingValue = "gui")
public class WebAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String uri = request.getRequestURI();

        if (isPublicResource(uri)) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect("/login");
            return false;
        }

        if (isAdminOnlyResource(uri)) {
            UserRole role = (UserRole) session.getAttribute("currentUserRole");
            // 如果 role 为 null 或不是 ADMIN，则拒绝访问
            if (role != UserRole.ADMIN) {
                response.sendRedirect("/access-denied");
                return false;
            }
        }

        return true;
    }

    private boolean isPublicResource(String uri) {
        return uri.equals("/login")
                || uri.equals("/register")
                || uri.equals("/access-denied")
                || uri.startsWith("/css/")
                || uri.startsWith("/js/")
                || uri.startsWith("/images/")
                || uri.startsWith("/error");
    }

    private boolean isAdminOnlyResource(String uri) {
        return uri.startsWith("/admin/users");
    }
}