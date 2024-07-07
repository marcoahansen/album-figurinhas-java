package main.services;

import main.entities.Figurinha;
import main.repositories.FigurinhaRepository;

import java.util.List;

public class FigurinhaService {
    private FigurinhaRepository figurinhaRepository;

    public FigurinhaService() {
        this.figurinhaRepository = new FigurinhaRepository();
    }

    public List<Figurinha> getAllFigurinhas() {
        return figurinhaRepository.getAllFigurinhas();
    }

    public void addFigurinha(Figurinha figurinha) {
        figurinhaRepository.addFigurinha(figurinha);
    }

    public void updateFigurinha(Figurinha figurinha) {
        figurinhaRepository.updateFigurinha(figurinha);
    }

    public void deleteFigurinha(int id) {
        figurinhaRepository.deleteFigurinha(id);
    }

    public Figurinha getFigurinhaById(int id) {
        return figurinhaRepository.getFigurinhaById(id);
    }

    public List<Figurinha> filtrarFigurinhas(String nome) {
        return figurinhaRepository.filtrarFigurinhas(nome);
    }
}
