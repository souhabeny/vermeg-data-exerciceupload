package com.sip.ams.controllers;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
import com.sip.ams.entities.Article;
import com.sip.ams.entities.Provider;
import com.sip.ams.repositories.ArticleRepository;
import com.sip.ams.repositories.ProviderRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import org.springframework.web.multipart.MultipartFile;
@Controller
@RequestMapping("/article/")
public class ArticleController {
	private final ArticleRepository articleRepository;
	private final ProviderRepository providerRepository;
	
	public static String uploadDirectory =
			System.getProperty("user.dir")+"/src/main/resources/static/uploads";

	@Autowired
	public ArticleController(ArticleRepository articleRepository, ProviderRepository providerRepository) {
		this.articleRepository = articleRepository;
		this.providerRepository = providerRepository;
	}

	@GetMapping("list")
	public String listArticles(Model model) {
		//model.addAttribute("articles", null);
		
		List<Article> la = (List<Article>) articleRepository.findAll();
		if (la.size() == 0)
			la = null;
		
		model.addAttribute("articles", la);
		return "article/listArticles";
	}

	@GetMapping("add")
	public String showAddArticleForm(Article article, Model model) {
		model.addAttribute("providers", providerRepository.findAll());
		model.addAttribute("article", new Article());
		return "article/addArticle";
	}

	@PostMapping("add")
	// @ResponseBody
	public String addArticle(@Valid Article article, BindingResult result,
			@RequestParam(name = "providerId", required = false) Long p,
			@RequestParam("files") MultipartFile[] files) {
		
		Provider provider = providerRepository.findById(p)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + p));
		article.setProvider(provider);
		
		
		/// part upload
		StringBuilder fileName = new StringBuilder();
		MultipartFile file = files[0];
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyy");
        Date date = new Date();
		String filedate=formatter.format(date).toString()+file.getOriginalFilename();
		Path fileNameAndPath = Paths.get(uploadDirectory,filedate);
		fileName.append(filedate);
		try {
		Files.write(fileNameAndPath, file.getBytes()); //upload
		} catch (IOException e) {
		e.printStackTrace();
		}
		article.setPicture(fileName.toString());
		
		articleRepository.save(article);
		return "redirect:list";
		// return article.getLabel() + " " +article.getPrice() + " " + p.toString();
	}

	@GetMapping("delete/{id}")
	public String deleteProvider(@PathVariable("id") long id, Model model) {
		Article artice = articleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
		try { 
            File file = new File(uploadDirectory+"/"+artice.getPicture());
            if(file.delete()) { 
               System.out.println(file.getName() + " is deleted!");
            } else {
               System.out.println("Delete operation is failed.");
               }
         }
           catch(Exception e)
           {
               System.out.println("Failed to Delete image !!");
           }
		articleRepository.delete(artice);
		return "redirect:../list";
		//model.addAttribute("articles", articleRepository.findAll());
		//return "/article/listArticles";
	}

	@GetMapping("edit/{id}")
	public String showArticleFormToUpdate(@PathVariable("id") long id, Model model) {
		Article article = articleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
		model.addAttribute("article", article);
		//System.out.println("ddd"+article.getPicture());
		model.addAttribute("providers", providerRepository.findAll());
		model.addAttribute("idProvider", article.getProvider().getId());
		
		return "article/updateArticle";
	}

	@PostMapping("edit")
	public String updateArticle(@Valid Article article, BindingResult result, Model model,
			@RequestParam(name = "providerId", required = false) Long p,@RequestParam(name = "pictureA", required = false) String pic,@RequestParam("files") MultipartFile[] files) {
		if (result.hasErrors()) {
			
			return "article/updateArticle";
		}
		
		Provider provider = providerRepository.findById(p)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + p));
		article.setProvider(provider);
		
		StringBuilder fileName = new StringBuilder();
		MultipartFile file = files[0];
		
		if(file.getOriginalFilename().isEmpty()==false)
		{SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyy");
        Date date = new Date();
		String filedate=formatter.format(date).toString()+file.getOriginalFilename();
		Path fileNameAndPath = Paths.get(uploadDirectory,filedate);
		fileName.append(filedate);
		System.out.println("ddddd"+filedate);
		 File filedelete = new File(uploadDirectory+"/"+pic);
		 filedelete.delete();
		try {
		Files.write(fileNameAndPath, file.getBytes()); //upload
		} catch (IOException e) {
		e.printStackTrace();
		}
		article.setPicture(fileName.toString());
		
		
		}
		if(file.getOriginalFilename().isEmpty()==true)
		{Path fileNameAndPath = Paths.get(uploadDirectory,pic);
		fileName.append(pic);
		System.out.println("bbbbb"+fileName);
		
		
		article.setPicture(fileName.toString());
		
		}
		
		articleRepository.save(article);
		return "redirect:../list";
		//model.addAttribute("articles", articleRepository.findAll());
		//return "article/listArticles";
	}
	
	@GetMapping("show/{id}")
	public String showArticleDetails(@PathVariable("id") long id, Model model)
	{
	Article article = articleRepository.findById(id)
	.orElseThrow(()->new IllegalArgumentException("Invalid provider Id:" + id));
	model.addAttribute("article", article);
	return "article/showArticle";
	}

}