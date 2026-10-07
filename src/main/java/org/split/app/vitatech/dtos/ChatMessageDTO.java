package org.split.app.vitatech.dtos;

import java.time.LocalDateTime;

public record ChatMessageDTO(
        Integer id,
        Integer senderId,
        Integer receiverId,
        String content,
        LocalDateTime sentAt,
        Boolean isRead
) {}