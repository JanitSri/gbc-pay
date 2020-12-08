package com.COMP3095.gbc_pay.validation;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CustomValidationService {

    public List<String> removeErrors(BindingResult userResult, BindingResult profileResult, BindingResult addressResult){
        List<ObjectError> filteredProfileErrors = new ArrayList<>();
        if(profileResult.getErrorCount() > 0){
            for (ObjectError o: profileResult.getAllErrors()){
                if( !Objects.equals(o.getDefaultMessage(), "Confirm Password cannot be empty")
                        && !Objects.equals(o.getDefaultMessage(), "Must agree to terms of service")
                        && !Objects.equals(o.getDefaultMessage(), "Password fields must match")){
                    filteredProfileErrors.add(o);
                }
            }
        }

        List<String> profileErrorMessages = filteredProfileErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        List<String> userErrorMessages = userResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        List<String> addressErrorMessages = addressResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return Stream.of(profileErrorMessages, userErrorMessages, addressErrorMessages)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
