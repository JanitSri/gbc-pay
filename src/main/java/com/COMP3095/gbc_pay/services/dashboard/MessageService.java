package com.COMP3095.gbc_pay.services.dashboard;

import com.COMP3095.gbc_pay.models.Message;
import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.repositories.MessageRepository;
import com.COMP3095.gbc_pay.services.user.RoleService;
import com.COMP3095.gbc_pay.services.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final RoleService roleService;

    public MessageService(MessageRepository messageRepository, UserService userService, RoleService roleService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.roleService = roleService;
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
                add(m.isHasReply());
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

    public void deleteMessage(Integer messageId){
        Message message = messageRepository.findById(Long.valueOf(messageId)).orElse(null);

        if(message != null){
            Set<Profile> tempProfiles = message.getProfiles();
            message.getProfiles().clear();
            messageRepository.save(message);

            for(Profile p: tempProfiles){
                p.getMessages().removeIf(message1 -> message1.getId().equals(Long.valueOf(messageId)));
                userService.saveProfile(p);
            }
        }

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
        List<Profile> adminProfiles = new ArrayList<>(roleService.getRole("ADMIN").getProfiles());
        Random r = new Random();
        Profile randomAdminProfile = adminProfiles.get(r.nextInt(adminProfiles.size()));

        Profile dbProfile = userService.findByEmail(profile.getEmail());

        message.setRead(false);
        message.setSentDateTime(LocalDateTime.now());

        message.getProfiles().add(dbProfile);
        dbProfile.getMessages().add(message);

        message.getProfiles().add(randomAdminProfile);
        randomAdminProfile.getMessages().add(message);

        messageRepository.save(message);
    }

    public void sendAdminMessage(Profile profile, Message message){
        // TODO
        // when sending the reply message set hasReply to true and read to false
    }
}



















