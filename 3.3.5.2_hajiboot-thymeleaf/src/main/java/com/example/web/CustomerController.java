package com.example.web;

import com.example.domain.Customer;
import com.example.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("customers")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    
    @RequestMapping(value="/customers")
    public String bookingsRedirect(){
        return "redirect:/customers/page/1";
    }
    
    //新規顧客情報
    @ModelAttribute
    CustomerForm setUpForm() {
        return new CustomerForm();
    }

//    @RequestMapping(method = RequestMethod.GET)
//    String list(Model model) {
//        List<Customer> customers = customerService.findAll();
//        model.addAttribute("customers", customers);
//        return "customers/list";
//    }

//    @RequestMapping(method = RequestMethod.GET)
//    String list(Model model) {
//        // ページング処理
//        Pageable pageable = new PageRequest(0, 5); //(1)
//        Page<Customer> page = customerService.findAll(pageable); //(2)
//        List<Customer> customers = customerService.findAll();
//        model.addAttribute("page", page);
//	    model.addAttribute("customers", customers);
//	    
//	    return "customers/list";
//    }
    
    
//    @RequestMapping(method = RequestMethod.GET)
//	String list(Model model){
//	Pageable pageable = new PageRequest(0, 5); 
    
    
//    @RequestMapping(value = "/customers/page/{pageNumber}" , method = RequestMethod.GET)
    @RequestMapping(method = RequestMethod.GET)
	String list(Model model){




        PageRequest pageable = new PageRequest(0, 5);

        Page<Customer> currentResults = customerService.findAll(pageable);
        
        model.addAttribute("currentResults", currentResults);
        
        //Pagination variables
        int current = currentResults.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, currentResults.getTotalPages()); // how many pages to display in the pagination bar
        
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        
        return "customers/list";
	}
    
    
    
    	
      


    @RequestMapping(value = "create", method = RequestMethod.POST)
    String create(@Validated CustomerForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return list(model);
//            return list(null, model);
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(form, customer);
        customerService.create(customer);
        return "redirect:/customers";
    }

    @RequestMapping(value = "edit", params = "form", method = RequestMethod.GET)
    String editForm(@RequestParam Integer id, CustomerForm form) {
        Customer customer = customerService.findOne(id);
        BeanUtils.copyProperties(customer, form);
        return "customers/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    String edit(@RequestParam Integer id, @Validated CustomerForm form, BindingResult result) {
        if (result.hasErrors()) {
            return editForm(id, form);
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(form, customer);
        customer.setId(id);
        customerService.update(customer);
        return "redirect:/customers";
    }

    @RequestMapping(value = "edit", params = "goToTop")
    String goToTop() {
        return "redirect:/customers";
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    String delete(@RequestParam Integer id) {
        customerService.delete(id);
        return "redirect:/customers";
    }
}
