package com.prs.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prs.business.JsonResponse;
import com.prs.business.PurchaseRequest;
import com.prs.business.PurchaseRequestLineItem;
import com.prs.business.User;
import com.prs.db.PurchaseRequestLineItemRepository;
import com.prs.db.PurchaseRequestRepository;
import com.prs.db.UserRepository;


@RestController
@RequestMapping("/purchase-requests")
public class PurchaseRequestController {
	
	@Autowired
	private PurchaseRequestRepository purchaseRequestRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PurchaseRequestLineItemRepository prliRepo;
	
	
	@GetMapping("/")
	public JsonResponse getAll(){
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestRepo.findAll());

	
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	
		
	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			Optional<PurchaseRequest> pr = purchaseRequestRepo.findById(id);
			if(pr.isPresent()) {
				jr = JsonResponse.getInstance(pr);
			} else {
				jr = JsonResponse.getInstance("No PurchaseRequest found for id: " + id);
			}
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@PostMapping("/submit-new")
	public JsonResponse add(@RequestBody PurchaseRequest pr) {
		JsonResponse jr = null;
		//NOTE: may need to enhance exception handling in future for more exceptions
		try {
			setStatus(pr, StatusType.NEW);
			pr.setSubmittedDate(LocalDateTime.now());
			jr = JsonResponse.getInstance(purchaseRequestRepo.save(pr));
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@PutMapping("")
	public JsonResponse update(@RequestBody PurchaseRequest pr) {
		JsonResponse jr = null;
		//NOTE: may need to enhance exception handling in future for more exceptions
		try {
			if(purchaseRequestRepo.existsById(pr.getId())) {
				jr = JsonResponse.getInstance(purchaseRequestRepo.save(pr));
		} else {
			jr = JsonResponse.getInstance("PurchaseRequest id: " + pr.getId() + "does not exist");
		}
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@DeleteMapping("")
	public JsonResponse delete(@RequestBody PurchaseRequest pr) {
		JsonResponse jr = null;
		//NOTE: may need to enhance exception handling in future for more exceptions
		try {
			if(purchaseRequestRepo.existsById(pr.getId())) {
				purchaseRequestRepo.delete(pr);
				jr = JsonResponse.getInstance("PurchaseRequest Deleted.");
		} else {
			jr = JsonResponse.getInstance("PurchaseRequest id: " + pr.getId() + "does not exist and you are attempting to delete it");
		}
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@PutMapping("/submit-review")
	public JsonResponse submitForReview(@RequestBody PurchaseRequest pr) {
		JsonResponse jr = null;
		try {
			if(purchaseRequestRepo.existsById(pr.getId())) {
				if(pr.getTotal() <= 50) {
					setStatus(pr, StatusType.APPOVED);
				} else {
					setStatus(pr, StatusType.REVIEW);
				}
				pr.setSubmittedDate(LocalDateTime.now());
				jr = JsonResponse.getInstance(purchaseRequestRepo.save(pr));
			} else {
				jr = JsonResponse.getInstance("PurchaseRequest with id: " + pr.getId() + " does not exist that you are attempting to submit for review");
			}
		} catch (Exception e){
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@PutMapping("/approve")
	public JsonResponse approve(@RequestBody PurchaseRequest pr) {
		JsonResponse jr = null;
		try {
			if(purchaseRequestRepo.existsById(pr.getId())) {
				setStatus(pr, StatusType.APPOVED);
				jr = JsonResponse.getInstance(purchaseRequestRepo.save(pr));
			} else {
				jr = JsonResponse.getInstance("PurchaseRequest with id: " + pr.getId() + " does not exist and you are attempting to set its Status");
			}
		} catch(Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@PutMapping("/reject")
	public JsonResponse reject(@RequestBody PurchaseRequest pr) {
		JsonResponse jr = null;
		try {
			if(purchaseRequestRepo.existsById(pr.getId())) {
				setStatus(pr, StatusType.REJECTED);
				jr = JsonResponse.getInstance(purchaseRequestRepo.save(pr));
			} else {
				jr = JsonResponse.getInstance("PurchaseRequest with id: " + pr.getId() + " does not exist and you are attempting to set its Status");
			}
		} catch(Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@GetMapping("/list-review/{id}")
	public JsonResponse listReview(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			if (userRepo.existsById(id) && userRepo.findById(id).get().isReviewer() == true){
				Iterable<PurchaseRequest> prs = purchaseRequestRepo.findAll();
				List<PurchaseRequest> nonUserPrs = new ArrayList<>();
				for (PurchaseRequest pr: prs){
					if (pr.getUser().getId() != id && pr.getStatus().equals("REVIEW")) {
						nonUserPrs.add(pr);
					}
				}
//				list of prlis never interacts with database, does it need to return an iterable?
//				Iterable<PurchaseRequest> returnList = nonUserPrs;
				jr = JsonResponse.getInstance(nonUserPrs);
			} else {
				jr = JsonResponse.getInstance("User with Id: " + id + " either does not have necessary permissions or does not exist");
			}
			
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		
		return jr;
	}
	
	@GetMapping("/lines-for-pr/{id}")
	public JsonResponse listPrlis(PurchaseRequest pr) {
		JsonResponse js = null;
		try {
			if (purchaseRequestRepo.existsById(pr.getId())) {
				Iterable<PurchaseRequestLineItem> prlis = prliRepo.findAll();
				List<PurchaseRequestLineItem> prlisPr = new ArrayList<>();
				for(PurchaseRequestLineItem lineItem: prlis) {
					if (lineItem.getPurchaseRequest().getId() == pr.getId()) {
						prlisPr.add(lineItem);
					}
				}
//				Iterable<PurchaseRequestLineItem> returnList = prlisPr;
//				list of prlis never interacts with database so don't need to return as iterable?
				js = JsonResponse.getInstance(prlisPr);
			} else {
				js = JsonResponse.getInstance("Error, you are trying to retrieve line items for purchase request that does not exist");
			}
		} catch (Exception e) {
			js = JsonResponse.getInstance(e);
	}
		
		
		
		return js;
	}
	
	public enum StatusType {
		NEW,
		REVIEW,
		APPOVED,
		REJECTED
	}

	public void setStatus(PurchaseRequest pr, StatusType st) {
		if(st == StatusType.NEW) {
			pr.setStatus("NEW");
		} else if(st == StatusType.REVIEW) {
			pr.setStatus("REVIEW");
		} else if(st == StatusType.APPOVED) {
			pr.setStatus("APPROVED");
		} else if(st == StatusType.REJECTED) {
			pr.setStatus("REJECTED");
		}
	}
}
