package pl.sensi.news.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.sensi.news.article.AuthorityUserType;

import java.util.Optional;

public class SecurityUtils {

    public static String getLoggedInUserId() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    public static UserDetails getLoggedInUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static boolean isNotAuthorized(String authorId) {
        UserDetails currentUserDetails = SecurityUtils.getLoggedInUser();

        if (currentUserDetails.getAuthorities().contains(new SimpleGrantedAuthority(AuthorityUserType.ADMIN.name()))) {
            return false;
        }

        return !currentUserDetails.getUsername().equals(authorId);
    }

    public static String getCurrentUserIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        String ipAddress = Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                .map(ip -> ip.split(",")[0])
                .orElse(request.getHeader("X-Real-IP"));

        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }
}
