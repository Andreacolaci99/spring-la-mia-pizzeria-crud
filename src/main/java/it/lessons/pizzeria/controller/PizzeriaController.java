package it.lessons.pizzeria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.lessons.pizzeria.model.Pizza;
import it.lessons.pizzeria.repository.PizzaRepository;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/")
public class PizzeriaController {

    @Autowired
    private PizzaRepository pizzaRepository;

    @GetMapping("/pizzeria")
    public String startMenu(Model model) {
        return "/pizzeria/menu";
    }

    @GetMapping("/pizzeria/pizze")
    public String listaPizze(Model model, @RequestParam(name = "keyword", required = false) String name) {
        List<Pizza> listaPizze;
        if (name != null && !name.isBlank()) {
            listaPizze = pizzaRepository.findByNameContainingIgnoreCase(name);
        } else {
            listaPizze = pizzaRepository.findAll();
        }
        model.addAttribute("pizze", listaPizze);
        return "/pizzeria/pizze";
    }

    @GetMapping("/pizzeria/pizze/{id}")
    public String dettaglioPizza(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("pizza", pizzaRepository.findById(id).get());
        return "pizzeria/dettaglioPizze";
    }

    @GetMapping("/pizzeria/pizze/create")
    public String createNewPizza(Model model) {

        model.addAttribute("pizza", new Pizza());

        return "pizzeria/create";
    }

    @PostMapping("pizzeria/pizze/create")
    public String postCreate(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasFieldErrors()) {
            return "/pizzeria/create";
        }

        pizzaRepository.save(formPizza);
        return "redirect:/pizzeria/pizze";
    }

    @GetMapping("/pizze/edit/{id}")
    public String editPizza(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("pizza", pizzaRepository.findById(id).get());

        return "/pizzeria/edit";
    }

    @PostMapping("/pizze/edit/{id}")
    public String updatePizza(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model) {
        
        if(bindingResult.hasErrors()){
            return "/pizzeria/edit";
        }
        pizzaRepository.save(formPizza);

        return "redirect:/pizzeria/pizze";
    }

    @PostMapping("/delete/{id}")
    public String deletePizza(@PathVariable("id") Integer id) {
        
        pizzaRepository.deleteById(id);
        return "redirect:/pizzeria/pizze";
    }
    
    
}
