package org.split.app.vitatech.services;

import org.split.app.vitatech.dtos.ChatMessageDTO;
import org.split.app.vitatech.models.Message;
import org.split.app.vitatech.models.User;
import org.split.app.vitatech.repositories.MessageRepository;
import org.split.app.vitatech.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Salvar e processar uma nova mensagem
    public ChatMessageDTO saveMessage(String senderEmail, ChatMessageDTO data) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Remetente não encontrado"));

        User receiver = userRepository.findById(data.receiverId())
                .orElseThrow(() -> new RuntimeException("Destinatário não encontrado"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(data.content());
        message.setSentAt(LocalDateTime.now());
        message.setIsRead(false);

        Message savedMessage = messageRepository.save(message);
        return convertToDTO(savedMessage);
    }

    // 2. Carregar o histórico da conversa entre dois utilizadores
    public List<ChatMessageDTO> getConversationHistory(User currentUser, Integer otherUserId) {
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        List<Message> messages = messageRepository.findConversation(currentUser, otherUser);
        return messages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Método auxiliar para conversão
    private ChatMessageDTO convertToDTO(Message m) {
        return new ChatMessageDTO(
                m.getId(), m.getSender().getId(), m.getReceiver().getId(),
                m.getContent(), m.getSentAt(), m.getIsRead()
        );
    }
}