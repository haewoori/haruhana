package hae.woori.onceaday.auth;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	public JwtAuthFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			if (jwtUtil.validate(token)) {
				String userId = jwtUtil.getUserId(token);
				// 유저 정보 로드해서 SecurityContext에 등록
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null,
					List.of());
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}

		filterChain.doFilter(request, response);
	}
}