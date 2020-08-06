package com.example.demoRedis.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demoRedis.domain.Product;
import com.example.demoRedis.domain.Segment;
import com.example.demoRedis.domain.StockNumberInfo;
import com.example.demoRedis.service.ProductService;

@RestController
@RequestMapping(value="/demo")
public class ProductController 
{
   
   @Autowired
   private ProductService prodService;
   
  
   @RequestMapping(method=RequestMethod.GET, value="/getdata/{cache}/{key}")
   public String getData(@PathVariable("cache") String cache, @PathVariable("key") String key) throws Exception
   {
	   String product = prodService.getData(cache, key);
	   
   	
   	return product;
   }
   
   @RequestMapping(method=RequestMethod.GET, value="/getStock/{key}")
   public String getData(@PathVariable("key") String key) throws Exception
   {
	   StockNumberInfo stockInfo = prodService.getStockInfo(key);
	   
	   System.out.println("stocknumber " + stockInfo.getStockNumber() + " volume " + stockInfo.getVolume());
	   stockInfo.getSegments().forEach(s -> System.out.println(" segment " + s.getSegmentNumber() + " path " +
	   s.getPath() + "status " +  s.getStatus()));
   	
   	return "finished";
   }
   
   @RequestMapping(method=RequestMethod.GET, value="/delete/{cache}/{key}")
   public void deleteData(@PathVariable("cache") String cache, @PathVariable("key") String key) throws Exception
   {
	   prodService.deleteData(cache, key);
	   
   }
   
   
   @RequestMapping(method=RequestMethod.GET, value="/getAllProduct")
    public Iterable<Product> getProducts () throws DataAccessException
    {
	   Iterable<Product> products  = null;
	   try
	   {
	      System.out.println("retrieve all products");
	      //prodService.findAllProducts();
	     
	   }
	   catch(Exception ex)
	   {
		   System.out.println("Error retriving products from database");
	   }
    	
    	
    	return products;
    }  
   
   @RequestMapping(method=RequestMethod.GET, value="/getProduct/{name}/{des}")
   @Cacheable(value="DPP",keyGenerator="customKey")
   public String getProduct(@PathVariable("name") String name, @PathVariable("des") String des) throws Exception
   {
	   System.out.println("retrieve product for " + name);
	   String product = prodService.findProductByName(name);
	   
   	
   	return product;
   }
   
   @RequestMapping(method=RequestMethod.GET, value="/getABC/{name}")
   @Cacheable(value="ABC",key="#name")
   public String getABC(@PathVariable("name") String name) throws Exception
   {
	   System.out.println("retrieve product for " + name);
	   String product = prodService.findProductByName(name);
	   
   	
   	return product;
   }  
   
   
   /*@RequestMapping(method=RequestMethod.POST, value="/requestDpp")
   public void requestDpp(@RequestBody DppRequest p) throws DataAccessException
   {
	  List<StockNumberInfo>  dppList = prodService.queryDpp();
	  
	  dppList.forEach(dpp -> {
		  prodService.requestDpp(dpp);
	  });
	    
   }*/
   
   
   
   @RequestMapping(method=RequestMethod.GET, value="/requestDpp")
   public String requestStock() 
   {
	  List<StockNumberInfo>  dppList = prodService.queryDpp();
	  
	  dppList.forEach(dpp -> {
		  try{ 
		      prodService.requestDpp(dpp);
		  }
		  catch(Exception ex){
			  System.out.println(" catch eception " + ex.getMessage());
		  }
	  });
	  
	  return "finished";
	    
   }
   
  
	   
}
