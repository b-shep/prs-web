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

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class PurchaseRequestTests {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	PurchaseRequestRepository prRepo;

	@Test
	public void testPurchaseRequestsGetAll() {
		User u = userRepo.findById(4).get();
		System.out.println(u);
		PurchaseRequest pr = new PurchaseRequest(0, u, "testDescrip", "justificationtest", LocalDate.now() , "car", "NEW", 100, LocalDateTime.now(), "null");
		prRepo.save(pr);
		Iterable <PurchaseRequest> prs = prRepo.findAll();
		assertNotNull(prs);
	}
	
	@Before
	public void testPurchaseRequestAddAndDelete() {
		User u = userRepo.findById(4).get();
		System.out.println(u);
		PurchaseRequest pr = new PurchaseRequest(0, u, "testDescrip", "justificationtest", LocalDate.now() , "car", "NEW", 100, LocalDateTime.now(), "null");
		assertNotNull(prRepo.save(pr));
	}
	
	 
//	User u = userRepo.findById(1).get();
//	PurchaseRequest pr = new PurchaseRequest(0, u, "testDescrip", "justificationtest", LocalDate.now() , "car", "NEW", 100, LocalDateTime.now(), "null");
//	assertNotNull(prRepo.save(pr));
	

}
