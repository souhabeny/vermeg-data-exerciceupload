package com.sip.ams.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sip.ams.entities.Provider;
import com.sip.ams.repositories.ProviderRepository;
import java.util.List;
import javax.validation.Valid;
@Controller
@RequestMapping("/provider/")
public class ProviderController {
	
	private final ProviderRepository providerRepository;
	@Autowired
	public ProviderController(ProviderRepository providerRepository) {
	this.providerRepository = providerRepository;
	}
	
	@GetMapping("list")
	//@ResponseBody
	public String listProviders(Model model) {
	model.addAttribute("providers", providerRepository.findAll());
	System.out.println(providerRepository.findAll());
	return "provider/listProviders";
	//List<Provider> lp = (List<Provider>)providerRepository.findAll();
	//System.out.println(lp);
	//return "Nombre de fournisseur = " + lp.size();
	}
}
