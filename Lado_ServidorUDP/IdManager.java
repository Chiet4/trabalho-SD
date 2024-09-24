package com.anchietaalbano.trabalho;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IdManager {
    private final List<String> availableIds;
    private final Random random;

    public IdManager() {
        this.availableIds = new ArrayList<>();
        this.random = new Random();
        GenerateIds();
    }

    private void GenerateIds() {
        // Inicializa os IDs disponíveis (exemplo com 10000 IDs)
        for (int i = 1; i <= 100; i++) {
            availableIds.add(String.format("%04d", i));
        }
    }

    public String gerarId() {
        if (availableIds.isEmpty()) {
            throw new RuntimeException("No available IDs left");
        }
        int index = random.nextInt(availableIds.size());

        return availableIds.remove(index);
    }

    public void liberarId(String id) {
        availableIds.add(id);
    }
}
