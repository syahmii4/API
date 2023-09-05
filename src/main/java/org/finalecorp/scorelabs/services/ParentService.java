package org.finalecorp.scorelabs.services;

import org.finalecorp.scorelabs.models.Parent;
import org.finalecorp.scorelabs.models.Teacher;
import org.finalecorp.scorelabs.repositories.ParentRepository;
import org.finalecorp.scorelabs.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParentService {
    private final ParentRepository parentRepository;
    @Autowired
    public ParentService(ParentRepository parentRepository){
        this.parentRepository=parentRepository;
    }

    public Parent createParent(int userId){
        try {
            Parent parent = new Parent();
            parent.setUserId(userId);
            parentRepository.save(parent);
            return parent;
        }
        catch (Exception e){
            return null;
        }
    }
}
