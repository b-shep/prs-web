package com.prs.business;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.prs.db.VendorRepository;

public class VendorTests {
	@Autowired
	private VendorRepository vendorRepo;
	
	@Test
	public void testVendorGetAll() {
		Iterable<Vendor> vendors = vendorRepo.findAll();
		assertNotNull(vendors);
	}
	
	@Before
	public void testVendorAddAndDelete() {
		Vendor u = new Vendor();
		//save a user
		assertNotNull(vendorRepo.save(u));
	}
	
}
