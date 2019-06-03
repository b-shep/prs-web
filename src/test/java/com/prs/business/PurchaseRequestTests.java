package com.prs.business;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.prs.db.PurchaseRequestRepository;
import com.prs.db.UserRepository;
import com.prs.web.PurchaseRequestController;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class PurchaseRequestTests {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	PurchaseRequestRepository prRepo;

	@Test
	public void testPurchaseRequestGetAll() {
		User u = userRepo.findById(4).get();
		PurchaseRequest pr = new PurchaseRequest(0, u, "testDescrip", "justificationtest", LocalDate.now() , "car", "NEW", 100, LocalDateTime.now(), "null");
		prRepo.save(pr);
		Iterable <PurchaseRequest> prs = prRepo.findAll();
		assertNotNull(prs);
	}
	
//	@Test
//	public void testPurchaseRequestUpdate() {
//		User u = userRepo.findById(4).get();
//		PurchaseRequest pr = new PurchaseRequest(0, u, "testDescrip", "justificationtest", LocalDate.now() , "car", "NEW", 100, LocalDateTime.now(), "null");
//		prRepo.save(pr);
//		PurchaseRequestController prcon = new PurchaseRequestController();
//		PurchaseRequest updatePr = new PurchaseRequest(0, u, "TESTupdate", "justificationtest", LocalDate.now() , "car", "NEW", 100, LocalDateTime.now(), "null");
//		prcon.update(updatePr);
//		Iterable<PurchaseRequest> prs = prRepo.findAll();
//		System.out.println(prs);
//		PurchaseRequest check = prRepo.findById(2).get();
//		
//		String description = check.getDescriptiton();
//		assertEquals("TESTupdate", description);
//	}
	
	@Before
	public void testPurchaseRequestAddAndDelete() {
		User u = userRepo.findById(4).get();
		PurchaseRequest pr = new PurchaseRequest(0, u, "testDescrip", "justificationtest", LocalDate.now() , "car", "NEW", 100, LocalDateTime.now(), "null");
		assertNotNull(prRepo.save(pr));
	}
	
	

}
