package com.example.project.service;

import org.springframework.stereotype.Service;
import com.example.project.model.Petition;
import com.example.project.repository.PetitionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing petitions.
 */
@Service
public class PetitionService {

    private final PetitionRepository petitionRepository;

    /**
     * Constructor for the PetitionService class.
     *
     * This constructor initializes the PetitionService with a given PetitionRepository.
     * The PetitionRepository is used to perform database operations related to petitions.
     *
     * @param petitionRepository The PetitionRepository to be used by this PetitionService.
     */
    public PetitionService(PetitionRepository petitionRepository) {
        this.petitionRepository = petitionRepository;
    }

    /**
     * Retrieves all petitions.
     *
     * @return List of all petitions.
     */
    public List<Petition> getAllPetitions() {
        return petitionRepository.findAll();
    }

    /**
     * Retrieves a petition by its ID.
     *
     * @param id The ID of the petition.
     * @return An Optional containing the petition if found.
     */
    public Optional<Petition> getPetitionById(Long id) {
        return petitionRepository.findById(id);
    }

    /**
     * Searches petitions based on a query and specified search criteria.
     *
     * @param query The search query string.
     * @param searchTitle Boolean indicating whether to search in titles.
     * @param searchDescription Boolean indicating whether to search in descriptions.
     * @return A list of petitions matching the search criteria.
     */
    public List<Petition> searchPetitions(String query, boolean searchTitle,
                                          boolean searchDescription) {
        if (searchTitle && searchDescription) {
            return petitionRepository.findByTitleContainingOrDescriptionContaining(query, query);
        } else if (searchTitle) {
            return petitionRepository.findByTitleContaining(query);
        } else if (searchDescription) {
            return petitionRepository.findByDescriptionContaining(query);
        } else {
            // Return empty list if no criteria are selected
            return new ArrayList<>();
        }
    }

    /**
     * Saves a new petition to the database.
     *
     * @param petition The petition to be saved.
     */
    public void savePetition(Petition petition) {
        petitionRepository.save(petition);
    }

    /**
     * Updates an existing petition.
     *
     * @param petition The petition with updated details.
     */
    public void updatePetition(Petition petition) {
        petitionRepository.save(petition);
    }

    /**
     * Deletes a petition by its ID.
     *
     * @param id The ID of the petition to be deleted.
     */
    public void deletePetition(Long id) {
        petitionRepository.deleteById(id);
    }
}
