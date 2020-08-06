package com.example.demoRedis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demoRedis.domain.Segment;
import com.example.demoRedis.domain.StockNumberInfo;




@Service
public class ProductService 
{
	@Autowired
	RedisTemplate<String, String> redisTemplate;
	@Autowired
	RedisCacheManager  rcm;
	
	
	
	public String getData(String cache, String key) {
		System.out.println("get data from cache " + cache);
		String value =(String) rcm.getCache(cache).get(key).get();
		
		return value;
		
	}
	
	public StockNumberInfo getStockInfo(String key) {
		System.out.println("get stocknumber for key " + key);
		StockNumberInfo value =(StockNumberInfo) rcm.getCache("DPP").get(key).get();
		
		return value;
		
	}
	
	public void deleteData(String cache, String key) {
		System.out.println("delete data from cache " + cache);
		rcm.getCache(cache).evict(key);;
		
		
	}
	
	public List<StockNumberInfo>  queryDpp() {
		
		List<StockNumberInfo>  stockNumberList = new ArrayList<StockNumberInfo>();
		
		StockNumberInfo info1 = new StockNumberInfo();
		info1.setStockNumber("DP1");
		info1.setVolume(2);
		Segment segment1 = new Segment("s1","COMPLETED","/user/home/DP1-1/s1");
		Segment segment2 = new Segment("s2","COMPLETED","/user/home/DP1-2/s2");
		Segment segment5 = new Segment("s2","COMPLETED","/user/home/DP1-2/s5");
		List<Segment> segmentList1 = new ArrayList<Segment>();
		//segmentList1.add(segment1);
		segmentList1.add(segment5);
		
		info1.setSegments(segmentList1);
		
		StockNumberInfo info2 = new StockNumberInfo();
		info2.setStockNumber("DP2");
		info2.setVolume(2);
		Segment segment3 = new Segment("s3","COMPLETED","/user/home/DP2-1/s3");
		Segment segment4 = new Segment("s4","COMPLETED","/user/home/DP2-2/s4");
		List<Segment> segmentList2 = new ArrayList<Segment>();
		segmentList2.add(segment3);
		segmentList2.add(segment4);
		
		info2.setSegments(segmentList2);
		
		stockNumberList.add(info1);
		//stockNumberList.add(info2);
		
		
		return stockNumberList;
		
	}
	
	@CachePut(value="DPP",keyGenerator="stockNumberCustomKey")	
	public StockNumberInfo downloadFile(Segment segment,StockNumberInfo stockInfo ) throws Exception{
		System.out.println("download file for segment " + segment.getSegmentNumber());
		//Thread.sleep(5000);
		if(segment.getSegmentNumber().equals("s1") || segment.getSegmentNumber().equals("s4")) {
			segment.setStatus("FAILED");
		}
		if(segment.getSegmentNumber().equals("s2") || segment.getSegmentNumber().equals("s3")) {
			segment.setStatus("COMPLETED");
		}
		
		synchronized(stockInfo) {
		     rcm.getCache("DPP").put("DPP",stockInfo.getStockNumber());
		}
		
		return stockInfo;
		
	};
	
	
	public void startRunnable(Segment segment,StockNumberInfo stockInfo) {
        Runnable r = () -> {
            try {
            	downloadFile(segment,stockInfo);
                
            } catch (Exception ex) {
            	System.out.println("Exception thrown in startRunable");
            }
        };
        new Thread(r).start();
    }
	

	public void processSegment(StockNumberInfo stockInfo) throws Exception{
		if(rcm.getCache("DPP").get(stockInfo.getStockNumber()) == null) {
			stockInfo.getSegments().forEach(se -> {
				System.out.println("startRunnable for segment " + se.getSegmentNumber());
				startRunnable(se, stockInfo); 
			});
		  }
		 else {
				StockNumberInfo stockFromCache = (StockNumberInfo) rcm.getCache("DPP").get(stockInfo.getStockNumber()).get();
			    if (stockFromCache != null ) {
			    	stockInfo.getSegments().forEach(se -> {
			    		for(Segment seCache:stockFromCache.getSegments()) {
                           if((seCache.getSegmentNumber().equals(se.getSegmentNumber()))) {
			    				if(!seCache.getStatus().equals("COMPLETED")) {
			    					System.out.println("status not completed download again file for segment " + 
			    							seCache.getSegmentNumber());
			    					startRunnable(se, stockInfo);
			    					break;
			    				}
			    			}
			    		}	
			    	});
			    }
		  }
	}
	
 @CachePut(value="DPP",keyGenerator="stockNumberCustomKey")	
   public StockNumberInfo  requestDpp(StockNumberInfo dpp) throws Exception {
	   System.out.println("requestDpp ");  
	   processSegment(dpp);
			   
	return dpp;
	}
   
   
   
   
   
   /*@Autowired
   public ProductService(ProductRepository prodRepo)
   {
	   this.prodRepo = prodRepo;
	   
   }
   
   
   public void setLocation()
   {
	   URI location = ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("thu").build().toUri();
	   
   }
   public void persistProducts(List<Product> products)
   {
	   System.out.println("persistProducts ");
	   
	      for (Product prod:products)
	      {
		     prodRepo.save(prod);
	      }
	  
   }
   
 
   
   
   public List<Product> findProductsWithSort()
   {
	   
	   Sort sort = new Sort(Sort.Direction.ASC, "price");
	   //List<Product> products = prodRepo.findByDescription("for sale", sort);
	   List<Product> products = prodRepo.findByDescription("for sale");
	   
	   products.forEach(p-> System.out.println(p.getDescription() + p.getName() + p.getPrice()));	 
	   
	   return products;
   }
   
   
   
   public Iterable<Product>  findAllProducts()
   {
	   Iterable<Product> prods  = prodRepo.findAll();
	   
	   for(Product prod:prods)
	   {
		   System.out.println("Prod name " + prod.getName() + " description: " + prod.getDescription() + " price: " + prod.getPrice());
	   }
	   
	   return prods;
   }
   
   
   public Iterable<Product>  findSpecificProducts()
   {
	   Iterable<Product> prods  = prodRepo.getProductMoreExpensiveThan();
	   
	   for(Product prod:prods)
	   {
		   System.out.println("Prod name " + prod.getName() + " description: " + prod.getDescription() + " price: " + prod.getPrice());
	   }
	   
	   return prods;
   }*/
   
   public String findProductByName(String name) throws Exception
   {
	  // Product prod = prodRepo.findByName(name);
	   //Product prod = new Product("product1", "for sale", 12);
	   
	  /* if (prod != null)
	   {
	     System.out.println("Prod name " + prod.getName() + " description: " + prod.getDescription() + 
	    		 " price: " + prod.getPrice());
	   }*/
	   Thread.sleep(5000);
	   if (name.equals("bad")) {
		   throw new Exception("bad request");
	   }
	  
	   return name;
   }
   
  
   
   
   
   
   
  
  
  
}
