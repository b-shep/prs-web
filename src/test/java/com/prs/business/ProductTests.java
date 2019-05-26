package com.prs.business;

import static org.junit.Assert.*;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.prs.business.User;
import com.prs.db.ProductRepository;
import com.prs.db.UserRepository;
import com.prs.db.VendorRepository;
import com.prs.web.VendorController;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductTests {
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private VendorRepository vendorRepo;
	
	@Test
	public void testUserGetAll() {
		Iterable<Product> products = productRepo.findAll();
		assertNotNull(products);
	}
	
	@Before
	public void testUserAddAndDelete() {
		Vendor v = vendorRepo.findById(1).get();
		Product p = new Product(0, v, "part1", "test product", 12.34, "unittest", "photopath");
		//save a user
		assertNotNull(productRepo.save(p));
		
	}
	
}
