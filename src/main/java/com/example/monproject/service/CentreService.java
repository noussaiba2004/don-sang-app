package com.example.monproject.service;

import com.example.monproject.model.Centre;
import com.example.monproject.model.User;
import com.example.monproject.repository.CentreRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CentreService {

    private final CentreRepository centreRepository;
    private final GeocodingService geocodingService;

    public CentreService(CentreRepository centreRepository, GeocodingService geocodingService) {
        this.centreRepository = centreRepository;
        this.geocodingService = geocodingService;
    }
    public List<Centre> findTop3CentresProches(String locationQuery) {
        Optional<double[]> userCoords = geocodingService.getCoordinates(locationQuery);

        if (userCoords.isEmpty()) {
            System.out.println("Aucune coordonnée trouvée pour : " + locationQuery);
            return Collections.emptyList();
        }
        double userLat = userCoords.get()[0];
        double userLon = userCoords.get()[1];

        return centreRepository.findAll().stream()
                .filter(c -> c.getLatitude() != null && c.getLongitude() != null)
                .sorted(Comparator.comparingDouble(c ->
                        distance(userLat, userLon, c.getLatitude(), c.getLongitude())))
                .limit(3)
                .collect(Collectors.toList());
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }
    public List<Centre> getAllCentres() {
        return centreRepository.findAll();
    }

    public Optional<Centre> findByUser(User user) {
        return centreRepository.findByUser(user);
    }


}
