package org.split.app.vitatech.controllers;

import org.split.app.vitatech.dtos.ChatMessageDTO;
import org.split.app.vitatech.models.User;
import org.split.app.vitatech.repositories.UserRepository;
import org.split.app.vitatech.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository; // 1. Injetamos o repositório aqui

    @MessageMapping("/chat.send")
    public void processMessage(@Payload ChatMessageDTO chatMessage, Principal principal) {

        // Salva a mensagem no banco de dados
        ChatMessageDTO savedMsg = messageService.saveMessage(principal.getName(), chatMessage);

        // 2. Busca o utilizador no banco para descobrir o E-MAIL dele
        User receiver = userRepository.findById(chatMessage.receiverId())
                .orElseThrow(() -> new RuntimeException("Destinatário não encontrado"));

        // 3. Envia a mensagem para o E-MAIL do destinatário (que é como a sessão dele está registada)
        messagingTemplate.convertAndSendToUser(
                receiver.getEmail(), // <-- CORREÇÃO AQUI
                "/queue/messages",
                savedMsg
        );
    }

    @GetMapping("/api/chat/history/{otherUserId}")
    public ResponseEntity<List<ChatMessageDTO>> getHistory(
            @PathVariable Integer otherUserId,
            @AuthenticationPrincipal User currentUser) {

        List<ChatMessageDTO> history = messageService.getConversationHistory(currentUser, otherUserId);
        return ResponseEntity.ok(history);
    }
}