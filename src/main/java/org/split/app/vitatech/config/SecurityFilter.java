package org.split.app.vitatech.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.split.app.vitatech.models.User;
import org.split.app.vitatech.repositories.UserRepository;
import org.split.app.vitatech.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Tenta recuperar o token da requisição
        var token = this.recoverToken(request);

        if (token != null) {
            // 2. Valida o token e extrai o e-mail
            var login = tokenService.validateToken(token);

            if (!login.isEmpty()) {
                // 3. Procura o utilizador no banco de dados
                User user = userRepository.findByEmail(login)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                // 4. Cria a autorização com base no perfil (Role) do utilizador
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
                var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

                // 5. Salva as informações de autenticação no contexto do Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continua o fluxo normal da requisição (vai para o Controller se estiver tudo certo)
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para extrair apenas o token do cabeçalho "Authorization"
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;

        // O padrão da web é enviar o token como "Bearer eyJhbGci..."
        return authHeader.replace("Bearer ", "");
    }
}