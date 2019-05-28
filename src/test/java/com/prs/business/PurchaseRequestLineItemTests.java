package com.prs.business;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.prs.db.ProductRepository;
import com.prs.db.PurchaseRequestLineItemRepository;
import com.prs.db.PurchaseRequestRepository;
import com.prs.db.UserRepository;
import com.prs.db.VendorRepository;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)

public class PurchaseRequestLineItemTests {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	VendorRepository vendorRepo;
	
	@Autowired
	ProductRepository productRepo;
	
	@Autowired
	PurchaseRequestRepository prRepo;
	
	@Autowired
	PurchaseRequestLineItemRepository prliRepo;
	



	@Test
	public void testPurchaseRequestLineItemGetAll() {
		User u = userRepo.findById(4).get();
		PurchaseRequest pr = new PurchaseRequest(0, u, "testDescrip", "justificationtest", LocalDate.now() , "car", "NEW", 100, LocalDateTime.now(), "null");
		prRepo.save(pr);
		Product p = productRepo.findById(4).get();
		
		PurchaseRequestLineItem prli = new PurchaseRequestLineItem(0, pr, p, 3);
		prliRepo.save(prli);
		Iterable<PurchaseRequestLineItem> prlis = prliRepo.findAll();
		assertNotNull(prlis);
		
	}

	@Before
 	public void testPurchaseRequestLineItemAddAndDelete() {
		User u = userRepo.findById(4).get();
		PurchaseRequest pr = new PurchaseRequest(0, u, "testDescrip", "justificationtest", LocalDate.now() , "car", "NEW", 100, LocalDateTime.now(), "null");
		prRepo.save(pr);
		Product p = productRepo.findById(4).get();
		
		PurchaseRequestLineItem prli = new PurchaseRequestLineItem(0, pr, p, 3);
		assertNotNull(prliRepo.save(prli));
	}
 	
 	
 	
	}
