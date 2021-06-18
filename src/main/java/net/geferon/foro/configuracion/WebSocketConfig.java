package net.geferon.foro.configuracion;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import net.geferon.foro.helpers.JwtUtils;

@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authorization = accessor.getNativeHeader("X-Authorization");
                    
                    if (authorization != null) {
	                    logger.debug("X-Authorization: {}", authorization);
	
	                    String accessToken = authorization.get(0);
	                    String username = jwtUtils.getUsernameFromToken(accessToken);
	                    
	                    if (username != null) {
	                    	UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	                    	
	                    	if (jwtUtils.validateToken(accessToken, userDetails)) {
	                    		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
	                    				new UsernamePasswordAuthenticationToken(
	                    						userDetails, null, userDetails.getAuthorities());
	            				
	            				accessor.setUser(usernamePasswordAuthenticationToken);
	            				logger.debug("User " + userDetails.getUsername() + " has logged in via Sockets!");
	                    	}
	                    }
                    }
                }
                return message;
            }
        });
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws");
		registry.addEndpoint("/ws")
			.setAllowedOrigins("*")
			.withSockJS();
	}
}
