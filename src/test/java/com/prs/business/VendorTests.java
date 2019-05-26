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
		Vendor v = new Vendor(0, "junkRus", "junk", "123 test", "test place", "OH", "39404", "23923414", "ersd@re.com", false);
		System.out.println(v);
		//save a vendor
		assertNotNull(vendorRepo.save(v));
	}
	
}
