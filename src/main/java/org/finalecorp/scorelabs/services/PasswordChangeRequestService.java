package org.finalecorp.scorelabs.services;

import org.finalecorp.scorelabs.models.PasswordChangeRequest;
import org.finalecorp.scorelabs.repositories.PasswordChangeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordChangeRequestService {
    public PasswordChangeRequestRepository passwordChangeRequestRepository;

    @Autowired
    public PasswordChangeRequestService(PasswordChangeRequestRepository passwordChangeRequestRepository){
        this.passwordChangeRequestRepository = passwordChangeRequestRepository;
    }

    public String generateRequestId(int userId){
        String requestId = "";

        do {
            requestId = UUID.randomUUID().toString().replace("-", "");
        } while(passwordChangeRequestRepository.findPasswordChangeRequestByPasscode(requestId) != null);

        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setUserId(userId);
        request.setPasscode(requestId);
        passwordChangeRequestRepository.save(request);
        return requestId;
    }

    public Boolean validatePasscode(String passcode){
        PasswordChangeRequest request = passwordChangeRequestRepository.findPasswordChangeRequestByPasscode(passcode);
        if (request == null) {
            return false;
        }

        boolean passIsUsed = request.isUsed();

        return !passIsUsed;
    }

    public int getUserId(String passcode){
        PasswordChangeRequest request = passwordChangeRequestRepository.findPasswordChangeRequestByPasscode(passcode);

        if(request == null){
            return -1;
        }
        else {
            return request.getUserId();
        }
    }

    public void invalidatePasscode(String reqId) {
        PasswordChangeRequest request = passwordChangeRequestRepository.findPasswordChangeRequestByPasscode(reqId);
        request.setUsed(true);
        passwordChangeRequestRepository.save(request);
    }
}
