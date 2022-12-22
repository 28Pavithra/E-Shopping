package com.eshopping.springboot.servicelmpl;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eshopping.springboot.exception.ResourceNotFoundException;
import com.eshopping.springboot.model.Customer;
import com.eshopping.springboot.model.Order;
import com.eshopping.springboot.model.Payment;
import com.eshopping.springboot.model.Product;
import com.eshopping.springboot.repository.OrderRepository;
import com.eshopping.springboot.repository.PaymentRepository;
import com.eshopping.springboot.service.CartService;
import com.eshopping.springboot.service.CustomerService;
import com.eshopping.springboot.service.OrderService;
import com.eshopping.springboot.service.PaymentService;
import com.eshopping.springboot.service.ProductService;




@Service
public class PaymentServiceImpl implements PaymentService{
	
	@Autowired
private PaymentRepository paymentRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OrderService orderService;


        public PaymentServiceImpl(PaymentRepository paymentRepository, ProductService productService,
			CustomerService customerService,OrderService orderService) {
		super();
		this.paymentRepository = paymentRepository;
		
		this.customerService = customerService;
		this.orderService = orderService;

	}
        @Override
        public Payment addPayment(Payment payment,long orderId,long customerId) {
    		
        	// TODO Auto-generated method stub
        	Order order = orderRepository.findById(orderId)
					.orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));
			payment.setOrderId(orderId);
			payment.setTotalPrice(order.getTotalPrice());
			payment.setPaidDate(LocalDate.now());
			payment.setPaidAmount(order.getTotalPrice());
			if (payment.getTotalPrice() == payment.getPaidAmount()) {
				order.setPaymentStatus("PAID");
				order.setOrderStatus("Delivered");
			} else {

				order.setPaymentStatus("NOT-PAID");
				order.setOrderStatus("payment pending");
			}
				  Customer customer=customerService.getCustomerById(customerId);
			    	
			    	payment.setCustomer(customer);
			    	
			    	
			    	     //return paymentRepository.save(payment);
			    	
			
			return paymentRepository.save(payment);
			
        }
              // order = orderService.getOrderById(orderId);
        	//payment.setOrderId(order.getOrderId());
        	//payment.setTotalPrice(payment.getTotalPrice());
        	//payment.setPaidDate(payment.getPaidDate());
  



	@Override
	public List<Payment> getAllPayments() {
		return paymentRepository.findAll();
	}
	@Override
	public List<Payment> getAllPaymentsByCustomerId(long customerId) {
		return paymentRepository.findByOrderId(customerId);
	}


	@Override
	public Payment getPaymentById(long paymentId) {
		
		return paymentRepository.findById(paymentId).orElseThrow(()->new ResourceNotFoundException("Payement","Id",paymentId));
	}


    @Override
	public void deletePayment(long paymentId) {
		paymentRepository.findById(paymentId).orElseThrow(()->new ResourceNotFoundException("Payement","Id",paymentId));
		paymentRepository.deleteById(paymentId);
		
	}
	
}