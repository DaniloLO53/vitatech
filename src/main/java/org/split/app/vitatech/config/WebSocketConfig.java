package org.split.app.vitatech.config;

import org.split.app.vitatech.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private TokenService tokenService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilita o broker para tópicos gerais (/topic) e filas privadas (/queue) <- CORREÇÃO AQUI
        config.enableSimpleBroker("/topic", "/queue");

        // Prefixo para as mensagens enviadas do cliente para o servidor
        config.setApplicationDestinationPrefixes("/app");

        // Prefixo que o Spring vai intercetar para saber que é uma mensagem privada
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Ponto de entrada do túnel WebSocket. O front-end vai conectar-se aqui.
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    // Intercetador para validar o Token JWT no momento da conexão do WebSocket
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // Se for a primeira conexão do túnel
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);
                        String userEmail = tokenService.validateToken(token);

                        if (!userEmail.isEmpty()) {
                            // Autentica a sessão do WebSocket com o e-mail do utilizador
                            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userEmail, null, null);
                            accessor.setUser(auth);
                        }
                    }
                }
                return message;
            }
        });
    }
}