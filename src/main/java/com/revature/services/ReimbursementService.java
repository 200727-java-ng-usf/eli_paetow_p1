package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementType;
import com.revature.repos.ReimbursementRepository;

import java.util.Optional;
import java.util.Set;

public class ReimbursementService {


    private ReimbursementRepository reimbursementRepository = new ReimbursementRepository();


    public void register(Reimbursement newReimbursement) {

        if (!isReimbursementValid(newReimbursement)) {
            throw new InvalidRequestException("bad values provided");

        }
        Optional<Reimbursement> reimbursement = reimbursementRepository.findReimbursementById(newReimbursement.getId());

        reimbursementRepository.save(newReimbursement);
        System.out.println(newReimbursement);

    }

    public boolean isReimbursementValid(Reimbursement reimbursement) {
        if (reimbursement == null) return false;
        if (reimbursement.getAmount() == null) return false;
        //come back cast to integer
//        if (reimbursement.getAuthorId() == null) return false;
        return true;

    }




    public Set<Reimbursement> getAllReimbursements() {
        Set<Reimbursement> reimbursements = reimbursementRepository.findAllReimbursements();
        if (reimbursements.isEmpty()) {
            throw new ResourceNotFoundException();

        }
        return reimbursements;

    }

    public Reimbursement getReimbursementById(int id){

        if(id<=0){
            throw new InvalidRequestException("id cant be less than 0");

        }
        return reimbursementRepository.findReimbursementById(id).orElseThrow(ResourceNotFoundException::new);

    }

    public Set<Reimbursement> getAllByType(ReimbursementType reimbursementType) {

        Set<Reimbursement> reimbs = reimbursementRepository.findAllReimbsByType(reimbursementType);

        if (reimbs.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return reimbs;

    }

}
