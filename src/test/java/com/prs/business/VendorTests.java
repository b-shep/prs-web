package com.prs.business;

import static org.junit.Assert.*;

import org.springframework.transaction.annotation.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.prs.business.Vendor;
import com.prs.db.VendorRepository;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class VendorTests {
	
	@Autowired
	private VendorRepository vendorRepo;

	@Test
	public void testVendorGetAll() {
		Iterable<Vendor> vendors = vendorRepo.findAll();
		System.out.println(vendors);
		assertNotNull(vendors);
	}
	
	@Before
	public void testVendorAddAndDelete() {
		Vendor v = new Vendor(0, "testCode", "testName", "testAddress", "testCity", "00", "00", "test", "test", true);
		assertNotNull(vendorRepo.save(v));
	}


}
