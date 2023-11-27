package com.example.project.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.project.model.Petition;
import com.example.project.model.Signature;
import com.example.project.service.PetitionService;

/**
 * The type Petition controller.
 */
@Controller
@RequestMapping("/app")
public class PetitionController {
    /**
     * The PetitionService instance used by this controller.
     *
     * This instance is used to interact with the data layer of the application,
     * performing operations related to Petitions such as saving a new Petition,
     * retrieving all Petitions, retrieving a Petition by its ID, and searching for Petitions.
     */
    private final PetitionService petitionService;

    /**
     * Instantiates a new Petition controller.
     *
     * @param petitionService the petition service
     */
    public PetitionController(PetitionService petitionService) {
        this.petitionService = petitionService;
    }

    /**
     * Display the index page to create a new petition.
     *
     * @return the name of the index template.
     */
    @GetMapping
    public String showIndexPage() {
        return "index";
    }

    /**
     * Handles the creation of a new petition. Validates the input fields for title and description.
     * If validation fails, it returns appropriate error messages. On successful creation of a
     * petition, it adds a success message to the model.
     *
     * @param title The title of the petition, must not be null or empty.
     * @param description The description of the petition, must not be null or empty.
     * @param model The Spring MVC Model used to pass attributes to the view.
     * @return The name of the index template. Redirects back to the index page after creation,
     *         displaying either error messages or a success message.
     */
    @PostMapping("/create")
    public String createPetition(@RequestParam String title, @RequestParam String description,
                                 Model model) {
        if (title == null || title.trim().isEmpty()) {
            model.addAttribute("titleError", "Title cannot be empty");
            return "index";
        }
        if (description == null || description.trim().isEmpty()) {
            model.addAttribute("descError", "Description cannot be empty");
            return "index";
        }
        Petition newPetition = new Petition(title, description);
        petitionService.savePetition(newPetition);
        model.addAttribute("successMessage", "The petition '" + title
                + "' has been successfully created. You can view it on the 'View All Petitions' page.");
        return "index";
    }

    /**
     * Displays the page with all the created petitions.
     *
     * @param model The Spring MVC Model used to pass attributes to the view.
     * @return The name of the view template.
     */
    @GetMapping("/viewAll")
    public String viewAllPetitions(Model model) {
        List<Petition> allPetitions = petitionService.getAllPetitions();
        model.addAttribute("petitions", allPetitions);
        return "view";
    }

    /**
     * Displays the details of a specific petition.
     *
     * @param id The ID of the petition.
     * @param model The Spring MVC Model used to pass attributes to the view.
     * @return The name of the petition detail template.
     */
    @GetMapping("/petition/{id}")
    public String viewPetitionDetail(@PathVariable Long id, Model model) {
        Optional<Petition> petitionOptional = petitionService.getPetitionById(id);
        petitionOptional.ifPresent(petition -> model.addAttribute("petition", petition));
        return petitionOptional.isPresent() ? "petitionDetail" : "redirect:/app/viewAll";
    }

    /**
     * Handles the submission of a signature to sign a petition.
     *
     * @param id The ID of the petition to be signed.
     * @param name The name of the signer.
     * @param email The email of the signer.
     * @param model The Spring MVC Model used to pass attributes to the view.
     * @return Redirects to the petition detail page.
     */
    @PostMapping("/petition/{id}/sign")
    public String signPetition(@PathVariable Long id, @RequestParam String name,
                               @RequestParam String email, Model model) {
        Optional<Petition> petitionOptional = petitionService.getPetitionById(id);
        if (petitionOptional.isPresent()) {
            Petition petition = petitionOptional.get();
            Signature signature = new Signature(name, email);
            signature.addPetition(petition);
            petition.addSignature(signature);
            petitionService.savePetition(petition);
            return "redirect:/app/petition/" + id;
        } else {
            return "redirect:/app/viewAll";
        }
    }

    /**
     * Displays the search page.
     *
     * @return The name of the search page template.
     */
    @GetMapping("/search")
    public String showSearchPage() {
        return "search";
    }

    /**
     * Handles the search result based on the provided query and search criteria.
     *
     * @param query The search query.
     * @param searchTitle Indicates if the search should include titles.
     * @param searchDescription Indicates if the search should include descriptions.
     * @param model The Spring MVC Model used to pass attributes to the view.
     * @return The name of the search result template.
     */
    @PostMapping("/searchResult")
    public String showSearchResult(@RequestParam String query,
                                   @RequestParam(required = false) Boolean searchTitle,
                                   @RequestParam(required = false) Boolean searchDescription, Model model) {
        List<Petition> results = petitionService.searchPetitions(query, searchTitle, searchDescription);
        model.addAttribute("searchPetitions", results);
        return "searchResult";
    }
}
