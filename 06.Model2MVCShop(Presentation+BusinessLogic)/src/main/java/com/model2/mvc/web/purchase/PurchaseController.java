package com.model2.mvc.web.purchase;

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
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;

@Controller
public class PurchaseController {


	@Autowired
	@Qualifier("PurchaseServiceImpl")
	private PurchaseService PurchaseService;
	
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
		
		return "redirect:/Purchase/addPurchaseView.jsp";
	}
	
	@RequestMapping("/addPurchase.do")
	public String addPurchase( @ModelAttribute("Purchase") Purchase Purchase ) throws Exception {

		System.out.println("/addPurchase.do");
		//Business Logic
		
		PurchaseService.addPurchase(Purchase);
		
		return "redirect:/Purchase/addPurchase.jsp";
	}
	
	@RequestMapping("/getPurchase.do")
	public String getPurchase( @RequestParam("menu") String menu, @RequestParam("prodNo") int prodNo , Model model, HttpSession session ) throws Exception {
		
		System.out.println(" @@@@@@ "+menu);
		
		System.out.println("/getPurchase.do");
		//Business Logic
		Purchase Purchase = PurchaseService.getPurchase(prodNo);
		// Model 과 View 연결
		session.setAttribute("Purchase", Purchase);
				
		return "forward:/Purchase/getPurchase.jsp";
	}
	
	@RequestMapping("/updatePurchaseView.do")
	public String updateUserView( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{

		System.out.println("/updateUserView.do");

		Purchase Purchase = PurchaseService.getPurchase(prodNo);
		// Model 과 View 연결
		model.addAttribute("Purchase", Purchase);
		
		return "forward:/Purchase/updatePurchaseView.jsp";
	}
	
	@RequestMapping("/updatePurchase.do")
	public String updatePurchaseTranCode(@RequestParam("prodNo") int prodNo, @ModelAttribute("Purchase") Purchase Purchase , Model model , HttpSession session) throws Exception{

		System.out.println("/updatePurchase.do");
		//Business Logic
		PurchaseService.updateTranCode(prodNo);
	
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
		Map<String , Object> map=PurchaseService.getPurchaseList(search,"user01");
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		System.out.println(resultPage + " :: ");
		// Model 과 View 연결
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/Purchase/listPurchase.jsp";
	}
}
