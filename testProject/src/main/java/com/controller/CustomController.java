package com.controller;



import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;



@Controller
@RequestMapping(value="/hello",produces="application/json")
public class CustomController {
	
	static org.apache.commons.logging.Log log = LogFactory.getLog(CustomController.class.getName());

@RequestMapping(value="/fetchPrimeNumbers",method=RequestMethod.GET)	
@ResponseBody public Object displayAllPrimeNumbers(@RequestParam(value="number")int num) {
	log.info("inside fetchPrimeNumbers");
	
	if(num<=1) {
		return new ResponseDTO("failure","Number must be greater than 1");
	}
	List<Integer> primeNumbersList=new ArrayList<>();
	for(int i=2;i<=num;i++) {
		if(checkNumberIsPrime(i)) {
			primeNumbersList.add(i);
		}
	}
	return new ResponseDTO("success",primeNumbersList);
	}
	
public boolean checkNumberIsPrime(int num) {
	
	  int m=num/2;      
	   
	   for(int i=2;i<=m;i++){      
	    if(num%i==0){      
	     return false;      
	    }      
	   }      

	return true;
}

@RequestMapping(value="/insertUser",method=RequestMethod.POST)	
@ResponseBody public Object insertUser(@RequestBody UserDTO dto) {
	log.info("inside insertUser");
 
   if((dto.getName()==null || dto.getName().isEmpty()) || (dto.getEmail()==null || dto.getEmail().isEmpty())
		   || (dto.getPassword()==null || dto.getPassword().isEmpty()) || (dto.getDob()==null || dto.getDob().isEmpty())){
		return new ResponseDTO("failure","Mandatory Fields cannot be left empty ."); 
   }
	
   String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
           "[a-zA-Z0-9_+&*-]+)*@" + 
           "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
           "A-Z]{2,7}$"; 
   
   Pattern pat = Pattern.compile(emailRegex); 
    if(!pat.matcher(dto.getEmail()).matches()) {
    	return new ResponseDTO("failure","Invalid email id format"); 
    } 
   //10 digit phone number
   pat=Pattern.compile("[0-9]{10}");
   if(!pat.matcher( dto.getPhone().toString()).matches()) {
   	return new ResponseDTO("failure","Invalid phone number"); 
   } 

	MongoCollection<Document> collection = MongoConnection.mongoClient.getDatabase("myNewDatabase").getCollection("users");
    
    Document d= new Document("name",dto.getName())
            .append("email",dto.getEmail())
            .append("password",dto.getPassword())
            .append("phone", dto.getPhone())
            .append("dob", dto.getDob());
    
    try {
collection.insertOne(d);
    }
    catch(com.mongodb.MongoWriteException ex) {
    	return new ResponseDTO("failure","duplicate email and phone Number combination .");
    }
    
	return new ResponseDTO("success","User inserted successfully");
}

}