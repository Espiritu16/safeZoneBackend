package com.utp.safezonebackend.shared.security;

import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class FiltroAutenticacionJwt extends OncePerRequestFilter {

    private final ServicioJwt servicioJwt;
    private final UsuarioRepository usuarioRepository;

    public FiltroAutenticacionJwt(ServicioJwt servicioJwt, UsuarioRepository usuarioRepository) {
        this.servicioJwt = servicioJwt;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String cabecera = request.getHeader("Authorization");
        if (cabecera == null || !cabecera.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = cabecera.substring(7);
        if (!servicioJwt.esTokenValido(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String usuarioId = servicioJwt.obtenerUsuarioId(token);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().isActivo()) {
            filterChain.doFilter(request, response);
            return;
        }

        Usuario usuario = usuarioOpt.get();
        var auth = new UsernamePasswordAuthenticationToken(
                usuario.getCorreo(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }
}
