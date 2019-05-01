package com.model2.mvc.web.Purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;

@Controller
public class PurchaseController {


	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	public PurchaseController() {
		System.out.println("PurchaseController Defualt Constructor");
	}
	
	@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping("/addPurchaseView.do")
	public String addPurchaseView() throws Exception {

		System.out.println("/PurchaseView.do");
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
	@RequestMapping("/addPurchase.do")
	public String addPurchase(@ModelAttribute("purchase") Purchase purchase, @ModelAttribute("product") Product product , @ModelAttribute("user") User user, HttpSession session, Model model) throws Exception {

		System.out.println("/addPurchase.do");
		
		user = (User)session.getAttribute("user");
		System.out.println(" :: " + user);
		
		product = productService.getProduct(product.getProdNo());
		System.out.println(" :: "+product);
		
		System.out.println(" :: " + purchase);
		
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		
		System.out.println(" =============== ");
		
		System.out.println(" :: " + purchase);
		
		purchaseService.addPurchase(purchase);
		
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/addPurchase.jsp";
	}
	
	/*
	@RequestMapping("/getPurchase.do")
	public String getPurchase( @RequestParam("menu") String menu, @RequestParam("prodNo") int prodNo , Model model, HttpSession session ) throws Exception {
		
		System.out.println(" @@@@@@ "+menu);
		
		System.out.println("/getPurchase.do");
		//Business Logic
		Purchase Purchase = PurchaseService.getPurchase(prodNo);
		// Model 과 View 연결
		session.setAttribute("Purchase", Purchase);
		
		if(menu.equals("manage")) {
			return "forward:/Purchase/updatePurchase.jsp";
		}
		
		return "forward:/Purchase/getPurchase.jsp";
	}
	
	@RequestMapping("/updatePurchaseView.do")
	public String updateUserView(@RequestParam("product") Product product , Model model ) throws Exception{

		System.out.println("/updateUserView.do");

		Purchase Purchase = PurchaseService.getPurchase(prodNo);
		// Model 과 View 연결
		model.addAttribute("Purchase", Purchase);
		
		return "forward:/Purchase/updatePurchaseView.jsp";
	}
	
	@RequestMapping("/updatePurchase.do")
	public String updatePurchase(@RequestParam("prodNo") int prodNo, @ModelAttribute("Purchase") Purchase Purchase , Model model , HttpSession session) throws Exception{

		System.out.println("/updatePurchase.do");
		//Business Logic
		PurchaseService.updatePurchase(Purchase);
	
		session.setAttribute("Purchase", Purchase);
		
		return "redirect:/updatePurchaseView.do?prodNo="+prodNo;
	}
	
	@RequestMapping("/listPurchase.do")
	public String listPurchase( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/listPurchase.do");
		
		if(search.getCurrentPage() == 0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		System.out.println(search + " :: ");
		// Business logic 수행
		System.out.println(search.getStartRowNum()+" "+search.getEndRowNum());
		Map<String , Object> map=PurchaseService.getPurchaseList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		System.out.println(resultPage + " :: ");
		// Model 과 View 연결
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/Purchase/listPurchase.jsp";
	}
	
	*/
}
