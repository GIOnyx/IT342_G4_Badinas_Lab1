package com.example.backend.service;

import com.example.backend.model.Item;
import com.example.backend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public Item create(Item item) {
        item.setId(null);
        return itemRepository.save(item);
    }

    public Item update(Long id, Item incoming) {
        return itemRepository.findById(id).map(existing -> {
            existing.setTitle(incoming.getTitle());
            existing.setSubtitle(incoming.getSubtitle());
            existing.setContent(incoming.getContent());
            return itemRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public void delete(Long id) {
        itemRepository.deleteById(id);
    }
}
