package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

//Pageable追加
import org.springframework.data.domain.Pageable;
import com.example.repository.CustomerRepository;
import com.example.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

@EnableAutoConfiguration
@ComponentScan
public class App  implements CommandLineRunner {
	
	 @Autowired
	 CustomerRepository customerRepository;
	
    @Override
    public void run(String... strings) throws Exception {
        // データ追加
        Customer created = customerRepository.save(new Customer(null, "Sugio", "Deki"));
        System.out.println(created + " is created!");
        // ページング処理
        Pageable pageable = new PageRequest(0,  5); //(1)
        Page<Customer> page = customerRepository.findAll(pageable); //(2)

        //(3)
        System.out.println("1ページのデータ数=" + page.getSize());//5
        System.out.println("現在のページ=" + page.getNumber());//0
        System.out.println("全ページ数=" + page.getTotalPages());//4
        System.out.println("全データ数=" + page.getTotalElements());//19
        page.getContent().forEach(System.out::println);
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}