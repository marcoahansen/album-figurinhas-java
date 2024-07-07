package main.services;

import main.entities.Album;
import main.repositories.AlbumRepository;

import java.util.List;

public class AlbumService {
    private final AlbumRepository albumRepository;

    public AlbumService() {
        this.albumRepository = new AlbumRepository();
    }

    public List<Album> getAllAlbums() {
        return albumRepository.getAllAlbums();
    }

    public void addAlbum(Album album) {
        albumRepository.addAlbum(album);
    }

    public void updateAlbum(Album album) {
        albumRepository.updateAlbum(album);
    }

    public void deleteAlbum(int id) {
        albumRepository.deleteAlbum(id);
    }
}
