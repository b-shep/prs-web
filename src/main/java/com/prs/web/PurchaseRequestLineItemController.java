package com.prs.web;

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
import com.prs.business.Product;
import com.prs.business.PurchaseRequest;
import com.prs.business.PurchaseRequestLineItem;
import com.prs.db.PurchaseRequestLineItemRepository;
import com.prs.db.PurchaseRequestRepository;


@RestController
@RequestMapping("/purchase-request-line-items")
public class PurchaseRequestLineItemController {
	
	@Autowired
	private PurchaseRequestLineItemRepository prliRepo;
	
	@Autowired
	private PurchaseRequestRepository prRepo;
	
	@GetMapping("")
	public JsonResponse getAll(){
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(prliRepo.findAll());
	
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
			Optional<PurchaseRequestLineItem> prli = prliRepo.findById(id);
			if(prli.isPresent()) {
				jr = JsonResponse.getInstance(prli);
			} else {
				jr = JsonResponse.getInstance("No PurchaseRequestLineItem found for id: " + id);
			}
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@PostMapping("")
	public JsonResponse add(@RequestBody PurchaseRequestLineItem prli) {
		JsonResponse jr = null;
		//NOTE: may need to enhance exception handling in future for more exceptions
		try {
			prli.getPurchaseRequest(); //debug statement to prevent prli from saving without updating pr total
			jr = JsonResponse.getInstance(prliRepo.save(prli));
			updateTotal(prli);
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@PutMapping("")
	public JsonResponse update(@RequestBody PurchaseRequestLineItem prli) {
		JsonResponse jr = null;
		//NOTE: may need to enhance exception handling in future for more exceptions
		try {
			if(prliRepo.existsById(prli.getId())) {
				jr = JsonResponse.getInstance(prliRepo.save(prli));
				updateTotal(prli);
		} else {
			jr = JsonResponse.getInstance("PurchaseRequestLineItem id: " + prli.getId() + "does not exist");
		}
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@DeleteMapping("")
	public JsonResponse delete(@RequestBody PurchaseRequestLineItem prli) {
		JsonResponse jr = null;
		//NOTE: may need to enhance exception handling in future for more exceptions
		try {
			if(prliRepo.existsById(prli.getId())) {
				prliRepo.delete(prli);
				jr = JsonResponse.getInstance("PurchaseRequestLineItem Deleted.");
				updateTotal(prli);
			} else {
				jr = JsonResponse.getInstance("PurchaseRequestLineItem id: " + prli.getId() + "does not exist and you are attempting to delete it");
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	public void updateTotal(PurchaseRequestLineItem prli) {
		double total = 0;
		PurchaseRequest pr = prli.getPurchaseRequest();
		Iterable<PurchaseRequestLineItem> prlis = prliRepo.findAll();
		
		for (PurchaseRequestLineItem lineItem: prlis) {
			if(lineItem.getPurchaseRequest().getId() == pr.getId()) {
				total += lineItem.getProduct().getPrice() * lineItem.getQuantity();
			}
		}
		
		pr.setTotal(total);
		prRepo.save(pr);
	}
	
}
