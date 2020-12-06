/* *********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 3
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: December 05, 2020
 * Description: Service class for the handling the messaging feature. CRUD operations for messages.
 ******************************************************************************** */

package com.COMP3095.gbc_pay.services.dashboard;

import com.COMP3095.gbc_pay.models.Message;
import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.repositories.MessageRepository;
import com.COMP3095.gbc_pay.services.user.RoleService;
import com.COMP3095.gbc_pay.services.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
                add(m.getSentDateTime().format(DateTimeFormatter.ofPattern("EE, LLL dd,yyyy h:mma")));
                add(m.isRead());
                add(m.isHasReply());
                add(getSender(profile, Math.toIntExact(m.getId())));
            }};

            formattedMessages.add(temp);
        }

        Collections.reverse(formattedMessages);
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
        List<Profile> adminProfiles = new ArrayList<>(roleService.getRole("ADMIN").getProfiles());
        Random r = new Random();
        Profile randomAdminProfile = adminProfiles.get(r.nextInt(adminProfiles.size()));

        Profile dbProfile = userService.findByEmail(profile.getEmail());

        message.setRead(true);
        message.setSentDateTime(LocalDateTime.now());

        message.getProfiles().add(dbProfile);
        dbProfile.getMessages().add(message);

        message.getProfiles().add(randomAdminProfile);
        randomAdminProfile.getMessages().add(message);

        messageRepository.save(message);
    }

    public Profile getSender(Profile profile, Integer messageId){
        Message message = getMessageById(profile, messageId);
        return message.getProfiles()
                .stream()
                .filter(profile1 -> !profile1.getId().equals(profile.getId()))
                .findFirst()
                .orElse(null);
    }

    public void sendAdminMessage(Message message){
        Message dbMessage = messageRepository.findById(message.getId()).orElse(null);

        if(dbMessage != null && dbMessage.getReplyMessageBody() == null){
            dbMessage.setHasReply(true);
            dbMessage.setRead(false);
            dbMessage.setReplyMessageBody(message.getReplyMessageBody());
            messageRepository.save(dbMessage);
        }
    }
}



















