package com.example.monproject.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate;
    private final Map<String, double[]> cache = new ConcurrentHashMap<>();

    public GeocodingService() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("User-Agent", "monproject-v1 (contact@example.com)");
            return execution.execute(request, body);
        });
    }

    public Optional<double[]> getCoordinates(String query) {
        String cleanedQuery = query.trim().toLowerCase();

        // Vérifier le cache
        if (cache.containsKey(cleanedQuery)) {
            return Optional.of(cache.get(cleanedQuery));
        }

        try {
            URI uri = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
                    .queryParam("q", cleanedQuery)
                    .queryParam("format", "json")
                    .queryParam("limit", "1")
                    .build()
                    .toUri();

            String response = restTemplate.getForObject(uri, String.class);
            JSONArray results = new JSONArray(response);

            if (results.isEmpty()) return Optional.empty();

            JSONObject location = results.getJSONObject(0);
            double lat = Double.parseDouble(location.getString("lat"));
            double lon = Double.parseDouble(location.getString("lon"));

            double[] coords = new double[]{lat, lon};

            // Sauvegarder dans le cache
            cache.put(cleanedQuery, coords);

            return Optional.of(coords);

        } catch (Exception e) {
            System.err.println("Erreur lors du géocodage : " + e.getMessage());
            return Optional.empty();
        }
    }
}


