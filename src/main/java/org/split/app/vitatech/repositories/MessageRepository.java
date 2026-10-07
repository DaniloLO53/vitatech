package org.split.app.vitatech.repositories;

import org.split.app.vitatech.models.Message;
import org.split.app.vitatech.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    // 1. Carregar Histórico de Chat (Muito Importante)
    // Uma Query customizada (JPQL) que traz toda a conversa entre duas pessoas,
    // independentemente de quem enviou ou recebeu, ordenada da mensagem mais antiga para a mais recente.
    @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.receiver = :user2) " +
           "OR (m.sender = :user2 AND m.receiver = :user1) ORDER BY m.sentAt ASC")
    List<Message> findConversation(@Param("user1") User user1, @Param("user2") User user2);

    // 2. Notificações / Badges
    // Conta quantas mensagens um utilizador tem sem ler (para mostrar um ícone vermelho no front-end, por exemplo).
    long countByReceiverAndIsReadFalse(User receiver);

    // 3. Buscar mensagens não lidas de um remetente específico
    // Útil para quando o utilizador abre a janela do chat e o sistema precisa marcar
    // todas as mensagens daquela pessoa específica como "Lidas".
    List<Message> findBySenderAndReceiverAndIsReadFalse(User sender, User receiver);
}