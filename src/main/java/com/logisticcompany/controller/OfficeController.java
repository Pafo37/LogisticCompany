package com.logisticcompany.controller;

import com.logisticcompany.data.entity.Office;
import com.logisticcompany.service.office.OfficeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/offices")
@AllArgsConstructor
public class OfficeController {

    private OfficeService officeService;

    @GetMapping
    public String getAllOffices(Model model) {
        model.addAttribute("offices", officeService.getAllOffices());
        return "offices";
    }

    @GetMapping("/add")
    public String showAddOfficeForm(Model model) {
        model.addAttribute("office", new Office());
        return "add_office";
    }

    @PostMapping("/add")
    public String addOffice(@ModelAttribute Office office) {
        officeService.saveOffice(office);
        return "redirect:/offices";
    }

    @GetMapping("/edit/{id}")
    public String showEditOfficeForm(@PathVariable Long id, Model model) {
        model.addAttribute("office", officeService.getOfficeById(id));
        return "edit_office";
    }

    @PostMapping("/edit/{id}")
    public String editOffice(@PathVariable Long id, @ModelAttribute Office office) {
        office.setId(id);
        officeService.saveOffice(office);
        return "redirect:/offices";
    }

    @GetMapping("/delete/{id}")
    public String deleteOffice(@PathVariable Long id) {
        officeService.deleteOffice(id);
        return "redirect:/offices";
    }

}