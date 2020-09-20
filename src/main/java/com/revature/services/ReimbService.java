package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.ErsReimbursement;
import com.revature.repos.ReimbRepository;


import java.util.Set;

public class ReimbService {

    private ReimbRepository reimbRepo = new ReimbRepository();


    public void register(ErsReimbursement newReimbursement) {

        if (!isReimbValid(newReimbursement)) {
            throw new InvalidRequestException("Invalid reimbursement field values provided during registration!");
        }

        reimbRepo.save(newReimbursement);
        System.out.println(newReimbursement);

    }


    public Set<ErsReimbursement> getAllReimbs() {

        Set<ErsReimbursement> reimbs = reimbRepo.findAllReimbs();

        if (reimbs.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return reimbs;
    }


    public Set<ErsReimbursement> getAllByAuthorId(Integer authorId) {

        Set<ErsReimbursement> reimbs = reimbRepo.findAllReimbsByAuthorId(authorId);

        if (reimbs.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return reimbs;

    }


    public ErsReimbursement getReimbById(int id) {

        if (id <= 0) {
            throw new InvalidRequestException("The provided id cannot be less than or equal to zero.");
        }

        return reimbRepo.findReimbById(id)
                .orElseThrow(ResourceNotFoundException::new);

    }


    public boolean resolve(ErsReimbursement updatedReimb) {

        if (updatedReimb == null) {
            throw new InvalidRequestException("reimb to resolve not found");
        }

        reimbRepo.resolve(updatedReimb);
        return true;
    }


    public boolean update(ErsReimbursement updatedReimb) {

        if (updatedReimb == null) {
            throw new InvalidRequestException("reimb to update not found");
        }

        reimbRepo.update(updatedReimb);
        return true;

    }


    public void delete(ErsReimbursement reimb) {

        if (reimb == null) {
            throw new InvalidRequestException("reimb to delete not found");
        }

        reimbRepo.delete(reimb);

    }


    public boolean isReimbValid(ErsReimbursement reimb) {
        if (reimb == null) return false;
        if (reimb.getAmount() == null) return false;
        if (reimb.getAuthorId() == null) return false;
        return true;
    }

}



