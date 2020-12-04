package com.COMP3095.gbc_pay.services.dashboard;

import com.COMP3095.gbc_pay.models.Message;
import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.repositories.MessageRepository;
import com.COMP3095.gbc_pay.services.user.UserService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }


    public List<Message> getMessages(Profile profile){
        return profile.getMessages();
    }

    public List<List<Object>> getFormattedMessages(Profile profile){
        List<List<Object>> formattedMessages = new ArrayList<>();

        for(Message m : profile.getMessages()){
            List<Object> temp = new ArrayList<>(){{
                add(m.getId());
                add(m.getSubject());
                add(m.getMessageBody());
                add(m.getReplyMessageBody());
                add(m.getSentDateTime().format(DateTimeFormatter.ofPattern("EE, LLL dd,yyyy h:ma")));
                add(m.isRead());
            }};

            formattedMessages.add(temp);
        }

        return formattedMessages;
    }

    public int getReadMessagesCount(Profile profile){
        return (int) getMessages(profile)
                .stream()
                .filter(Message::isRead)
                .count();
    }

    public int getUnReadMessagesCount(Profile profile){
        return (int) getMessages(profile)
                .stream()
                .filter(message -> !message.isRead())
                .count();
    }

    public Message getMessageById(Profile profile, int messageId){
        return getMessages(profile)
                .stream()
                .filter(message -> message.getId() == messageId)
                .findFirst()
                .orElse(null);
    }

    public void deleteMessage(Profile profile, Integer messageId){
        messageRepository.findById(Long.valueOf(messageId))
                .ifPresent(
                        message -> message.getProfiles()
                        .removeIf(profile1 -> profile1.getId().equals(profile.getId()))
                );

        Profile dbProfile = userService.findByProfileById(profile.getId());

        if(dbProfile != null){
            dbProfile.getMessages().removeIf(message -> message.getId().equals(Long.valueOf(messageId)));
        }

        userService.saveProfile(dbProfile);

        messageRepository.deleteById(Long.valueOf(messageId));
    }


    public void readMessage(int messageId){
        Message message = messageRepository.findById((long) messageId).orElse(null);
        if(message != null){
            message.setRead(true);
            messageRepository.save(message);
        }
    }

    public void sendMessage(Profile profile, Message message){
        // TODO
    }
}








