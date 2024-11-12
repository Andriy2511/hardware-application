package com.example.hardware.controller;

import com.example.hardware.component.CatalogPaginationData;
import com.example.hardware.component.ListPaginationData;
import com.example.hardware.model.Component;
import com.example.hardware.model.Developer;
import com.example.hardware.model.User;
import com.example.hardware.service.IDeveloperService;
import com.example.hardware.service.IComponentService;
import com.example.hardware.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/catalog")
@Slf4j
public class CatalogController {

    private final IDeveloperService developerService;
    private final IComponentService componentService;
    private final IRoleService roleService;
    private final CatalogPaginationData paginationData;
    private final ListPaginationData listPaginationData;

    @Autowired
    public CatalogController(IDeveloperService developerService, IComponentService componentService, IRoleService roleService, CatalogPaginationData paginationData, ListPaginationData listPaginationData) {
        this.developerService = developerService;
        this.componentService = componentService;
        this.roleService = roleService;
        this.paginationData = paginationData;
        this.listPaginationData = listPaginationData;
    }

    @GetMapping("/developerInfo/{id}")
    public String showDeveloperInfo(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        Developer developer = developerService.findDeveloperById(id);
        model.addAttribute("developer", developer);
        listPaginationData.setTotalRecords((long) componentService.getComponentsCountByDeveloper(developer).size());
        model.addAttribute("components", componentService.findComponentsByDeveloperWithPaginationAndSorting(listPaginationData.getPage(), listPaginationData.getPageSize(), developer));
        boolean isAdmin = roleService.isUserContainRole(user, "ADMIN");
        model.addAttribute("isAdmin", isAdmin);
        return "catalog/developer-info";
    }

    @GetMapping("/componentInfo/{id}")
    public String showComponentInfo(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        Component component = componentService.findComponentById(id);
        model.addAttribute("component", component);
        boolean isAdmin = roleService.isUserContainRole(user, "ADMIN");
        model.addAttribute("isAdmin", isAdmin);
        return "catalog/component-info";
    }

    @GetMapping("/showComponentsCatalog")
    public String getComponentsWithPaginationAndSorting(Model model, @AuthenticationPrincipal User user,
                                                        @RequestParam(name = "sortingField", required = false) String sortingField,
                                                        @RequestParam(name = "page",  required = false) Integer page,
                                                        @RequestParam(name = "pageSize", required = false) Integer recordPerPage) {
        boolean isAdmin = roleService.isUserContainRole(user, "ADMIN");

        configurePaginationAndSorting(sortingField, page, recordPerPage);



        List<Component> componentList = componentService.
                findComponentsWithPaginationAndSorting(paginationData.getPage(), paginationData.getPageSize(), paginationData.getSortingField())
                .get().toList();

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("components", componentList);
        model.addAttribute("paginationData", paginationData);

        return "catalog/component-catalog";
    }

    private void configurePaginationAndSorting(String sortingField, Integer page, Integer recordPerPage) {
        if (sortingField != null) {
            paginationData.setSortingField(sortingField);
        }
        if (page != null && page >= 0) {
            paginationData.setPage(page);
        }
        if (recordPerPage != null && recordPerPage > 0) {
            paginationData.setPageSize(recordPerPage);
            paginationData.setTotalPages(countTotalPages(recordPerPage));
        }
    }

    private Integer countTotalPages(Integer recordPerPage){
        Long totalRecords = componentService.selectCountOfComponents();
        return (int) Math.ceil((double) totalRecords/recordPerPage);
    }
}
