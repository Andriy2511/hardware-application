package com.example.hardware.controller;

import com.example.hardware.DTO.ComponentDTO;
import com.example.hardware.component.ListPaginationData;
import com.example.hardware.model.*;
import com.example.hardware.service.*;
import com.example.hardware.service.implementation.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final IUserService userService;
    private final IDeveloperService developerService;
    private final IComponentService componentService;
    private final IOrderService orderService;
    private final IRoleService roleService;
    private final ICategoryService categoryService;
    private final ListPaginationData listPaginationData;

    @Autowired
    public AdminController(UserService userService, IDeveloperService developerService, IComponentService componentService,
                           IOrderService orderService, IRoleService roleService, ICategoryService categoryService, ListPaginationData listPaginationData) {
        this.userService = userService;
        this.developerService = developerService;
        this.componentService = componentService;
        this.orderService = orderService;
        this.roleService = roleService;
        this.categoryService = categoryService;
        this.listPaginationData = listPaginationData;
    }

    @GetMapping("/showAddDeveloperForm")
    public String showAddDeveloperForm(Model model) {
        model.addAttribute("developer", new Developer());
        return "admin/add-developer";
    }

    @PostMapping("/addDeveloper")
    public String addDeveloper(@ModelAttribute("developer") @Valid Developer developer, BindingResult result, Model model) {
        if(result.hasErrors() || developer.getName() == null || developer.getName().isEmpty() || developer.getName().isBlank()) {
            return "admin/add-developer";
        } else if (developerService.isDeveloperExist(developer)){
            model.addAttribute("developerExistsError", "Developer with this name already exists");
            return "admin/add-developer";
        }

        developerService.addNewDeveloper(developer);
        return "redirect:/admin/showAddDeveloperForm";
    }

    @GetMapping("/showDevelopersList")
    public String showDeveloperList(Model model) {
        listPaginationData.setTotalRecords(developerService.getDevelopersCount());
        model.addAttribute("developers", developerService.findAllDevelopersWithPagination(listPaginationData.getPage(), listPaginationData.getPageSize()));
        return "admin/developers-list";
    }

    @GetMapping("/showChangeDeveloperForm/{id}")
    public String showChangeDeveloperForm(@PathVariable Long id, Model model) {
        Developer developer = developerService.findDeveloperById(id);
        model.addAttribute("developer", developer);
        model.addAttribute("id", id);
        return "admin/change-developer";
    }

    @PostMapping("/changeDeveloper")
    public String changeDeveloper(@ModelAttribute("developer") @Valid Developer developer, @RequestParam("id") Long id, BindingResult result, Model model) {
        Developer changedDeveloper = new Developer();
        changedDeveloper.setId(id);
        changedDeveloper.setName(developer.getName());
        changedDeveloper.setDescription(developer.getDescription());
        boolean isDeveloperWithCurrentNameExists = developerService.findDeveloperByName(changedDeveloper.getName()) != null
                && !developerService.findDeveloperByName(changedDeveloper.getName()).getId().equals(id);
        if(result.hasErrors()) {
            return "redirect:admin/showChangeDeveloperForm/" + id;
        } else if (isDeveloperWithCurrentNameExists || changedDeveloper.getName() == null ||
                changedDeveloper.getName().isEmpty() || changedDeveloper.getName().isBlank()){
            model.addAttribute("developerExistsError", "Developer with this name already exists");
            model.addAttribute("id", id);
            model.addAttribute("developer", developer);
            return "admin/change-developer";
        }

        developerService.changeDeveloper(developer);
        return "redirect:/admin/showDevelopersList";
    }

    @GetMapping("/showComponentsList")
    public String showComponentList(Model model) {
        listPaginationData.setTotalRecords(componentService.getComponentsCount());
        model.addAttribute("components", componentService.findAllComponentsWithPagination(listPaginationData.getPage(), listPaginationData.getPageSize()));
        return "admin/component-list";
    }

    @GetMapping("/showAddComponentForm")
    public String showAddComponentForm(Model model) {
        model.addAttribute("componentDTO", new ComponentDTO());
        return "admin/add-component";
    }

    @PostMapping("/addComponent")
    public String addComponent(@ModelAttribute("componentDTO") @Valid ComponentDTO componentDTO,
                               Model model) {

        System.out.println(componentDTO);
        Map<String, String> errorMap = addErrorToModel(componentDTO, componentDTO.getDeveloper(), componentDTO.getCategory());
        if(!errorMap.isEmpty() || componentService.findComponentByName(componentDTO.getName()) != null){
            if (componentDTO.getName() != null) {
                if(componentService.findComponentByName(componentDTO.getName()) != null) {
                    errorMap.put("errorComponentName", "Component with this name has already exist");
                }
            }
            for (Map.Entry<String, String> entry : errorMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                model.addAttribute(key, value);
            }

            return "admin/add-component";
        }
        addPhotoToFolder(componentDTO.getPhoto());
        Component component = mapToComponent(componentDTO);
        componentService.addComponent(component);

        return "redirect:/admin/showAddComponentForm";
    }

    private Component mapToComponent(ComponentDTO componentDTO){
        Component component = new Component();
        component.setName(componentDTO.getName());
        component.setDescription(componentDTO.getDescription());
        component.setPhoto(componentDTO.getPhoto() != null && !componentDTO.getPhoto().isEmpty() ? componentDTO.getPhoto().getOriginalFilename() : "without photo");
        component.setPrice(componentDTO.getPrice());
        Developer developer;
        if(developerService.findDeveloperByName(componentDTO.getDeveloper()) != null) {
            developer = developerService.findDeveloperByName(componentDTO.getDeveloper());
        } else {
            developer = developerService.addNewDeveloper(new Developer(componentDTO.getDeveloper()));
        }
        component.setDeveloper(developer);

        Category category;
        if(categoryService.findCategoryByName(componentDTO.getCategory()) != null) {
            category = categoryService.findCategoryByName(componentDTO.getCategory());
        } else {
            category = categoryService.addCategory(new Category(componentDTO.getCategory()));
        }

        component.setCategory(category);
        return component;
    }

    private boolean isStringNullOrEmpty(String string) { return string == null || string.isEmpty() || string.isBlank(); }

    private Map <String, String> addErrorToModel(ComponentDTO componentDTO, String developerName, String categoryName){
        Map<String, String> map = new HashMap<>();
        if (isStringNullOrEmpty(componentDTO.getName())) {
            map.put("errorComponentName", "Enter name for this component");
        }

        if (componentDTO.getPrice() == null || componentDTO.getPrice() < 0) {
            map.put("errorPriceName", "Enter valid price for this component");
        }

        if (isStringNullOrEmpty(developerName)) {
            map.put("errorDeveloperName", "Enter the developer for this component");
        }

        if (isStringNullOrEmpty(categoryName)) {
            map.put("errorCategoryName", "Enter the category for this component");
        }

        return map;
    }

    @GetMapping("/showChangeComponentForm/{id}")
    public String showChangeComponentForm(@PathVariable Long id, Model model) {
        Component component = componentService.findComponentById(id);
        model.addAttribute("component", component);
        model.addAttribute("componentDTO", new ComponentDTO());
        model.addAttribute("id", id);
        return "admin/change-component";
    }

    @PostMapping("/changeComponent")
    public String changeComponent(@ModelAttribute("componentDTO") @Valid ComponentDTO componentDTO,
                                  @RequestParam(value = "id") Long id,
                               Model model) {

        Map<String, String> errorMap = addErrorToModel(componentDTO, componentDTO.getDeveloper(), componentDTO.getCategory());

        if (componentService.findComponentByName(componentDTO.getName()) != null) {
            if(!Objects.equals(componentService.findComponentByName(componentDTO.getName()).getId(), componentDTO.getId())) {
                errorMap.put("errorComponentName", "Component with this name has already exist");
            }
        }

        if(!errorMap.isEmpty()){
            for (Map.Entry<String, String> entry : errorMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                model.addAttribute(key, value);
            }

            model.addAttribute("componentDTO", new ComponentDTO());
            model.addAttribute("component", componentService.findComponentById(id));
            model.addAttribute("id", id);
            return "admin/change-component";
        }

        Component component = mapToComponent(componentDTO);

        if(!component.getPhoto().equals("without photo") && !componentDTO.getPhoto().getName().equals(componentService.findComponentById(id).getPhoto())){
            addPhotoToFolder(componentDTO.getPhoto());
            System.out.println(component);
        } else {
            component.setPhoto(componentService.findComponentById(id).getPhoto());
        }

        component.setId(componentDTO.getId());
        componentService.addComponent(component);

        return "redirect:/admin/showComponentsList";
    }


    @GetMapping("/showOrdersList")
    public String showOrderList(Model model) {
        listPaginationData.setTotalRecords(orderService.getOrdersCount());
        model.addAttribute("orders", orderService.showAllOrdersWithPagination(listPaginationData.getPage(), listPaginationData.getPageSize()));
        return "admin/order-list";
    }

    @GetMapping("/showUsersList")
    public String showUserList(Model model) {
        List<Role> roleList = roleService.findAllRoleByName("USER");
        listPaginationData.setTotalRecords(userService.getCountUsersByRole(roleList.get(0)));
        model.addAttribute("users", userService.findAllUsersByRolesWithPagination(roleList,
                listPaginationData.getPage(), listPaginationData.getPageSize()));
        return "admin/user-list";
    }

    @GetMapping("/userInfo/{id}")
    public String showUserInfo(@PathVariable Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        listPaginationData.setTotalRecords(orderService.getOrdersCountByUser(user));
        model.addAttribute("orders", orderService.findOrdersByUserWithPagination(user, listPaginationData.getPage(), listPaginationData.getPageSize()));
        return "admin/user-info";
    }

    private void addPhotoToFolder(MultipartFile photoFile){
        if (photoFile != null && !photoFile.isEmpty()) {
            try {
                String fileName = photoFile.getOriginalFilename();
                Path filePath = Paths.get("src/main/resources/static/componentphoto/" + fileName);
                photoFile.transferTo(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}