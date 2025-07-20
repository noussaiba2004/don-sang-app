package com.example.monproject;

import com.example.monproject.model.Centre;
import com.example.monproject.repository.CentreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableScheduling
@SpringBootApplication
public class MonprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonprojectApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(CentreRepository centreRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (centreRepository.count() == 0) {
				// Centre 1 - Casablanca (Sang total + Plasma)
				Centre centre1 = new Centre();
				centre1.setNom("Centre régional de transfusion sanguine de Casablanca");
				centre1.setAdresse("19 Rue Mustapha El Maani, Casablanca");
				centre1.setVille("Casablanca");
				centre1.setCodePostal("20000");
				centre1.setLatitude(33.5731);
				centre1.setLongitude(-7.5898);
				centre1.setEtat("ouvert");
				centre1.setTypeCollecte("Sang total, Plasma");
				centre1.setHoraire("Lun-Ven: 8h-20h, Sam: 9h-17h");
				centre1.setEmail("CRTS.CASA@example.com");
				centre1.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre1);

				// Centre 2 - Rabat (Sang total + Plaquettes)
				Centre centre2 = new Centre();
				centre2.setNom("Centre régional de transfusion sanguine de Rabat");
				centre2.setAdresse("Avenue Mohammed Belhassan El Ouazzani, Rabat");
				centre2.setVille("Rabat");
				centre2.setCodePostal("10000");
				centre2.setLatitude(34.0209);
				centre2.setLongitude(-6.8416);
				centre2.setEtat("urgence");
				centre2.setTypeCollecte("Sang total, Plaquettes");
				centre2.setHoraire("Lun-Sam: 8h-19h");
				centre2.setEmail("CRTS.RABAT@example.com");
				centre2.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre2);

				// Centre 3 - Marrakech (Sang total seulement)
				Centre centre3 = new Centre();
				centre3.setNom("Centre régional de transfusion sanguine de Marrakech");
				centre3.setAdresse("Rue Ibn Sina, Quartier de l'Hôpital, Marrakech");
				centre3.setVille("Marrakech");
				centre3.setCodePostal("40000");
				centre3.setLatitude(31.6295);
				centre3.setLongitude(-8.0087);
				centre3.setEtat("ouvert");
				centre3.setTypeCollecte("Sang total");
				centre3.setHoraire("Lun-Ven: 8h-18h");
				centre3.setEmail("CRTS.MARRAKECH@example.com");
				centre3.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre3);

				// Centre 4 - Fès (Plasma seulement)
				Centre centre4 = new Centre();
				centre4.setNom("Centre régional de transfusion sanguine de Fès");
				centre4.setAdresse("Avenue des FAR, Fès");
				centre4.setVille("Fès");
				centre4.setCodePostal("30000");
				centre4.setLatitude(34.0331);
				centre4.setLongitude(-5.0003);
				centre4.setEtat("ferme");
				centre4.setTypeCollecte("Plasma");
				centre4.setHoraire("Mar-Sam: 9h-16h");
				centre4.setEmail("CRTS.FES@example.com");
				centre4.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre4);

				// Centre 5 - Tanger (Tous types)
				Centre centre5 = new Centre();
				centre5.setNom("Centre régional de transfusion sanguine de Tanger");
				centre5.setAdresse("Boulevard Mohammed VI, Tanger");
				centre5.setVille("Tanger");
				centre5.setCodePostal("90000");
				centre5.setLatitude(35.7595);
				centre5.setLongitude(-5.8330);
				centre5.setEtat("ouvert");
				centre5.setTypeCollecte("Sang total, Plasma, Plaquettes");
				centre5.setHoraire("Lun-Dim: 7h-22h");
				centre5.setEmail("CRTS.TANGER@example.com");
				centre5.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre5);

				// Centre 6 - Tétouan
				Centre centre6 = new Centre();
				centre6.setNom("Centre régional de transfusion sanguine de Tétouan");
				centre6.setAdresse("Quartier administratif, Tétouan");
				centre6.setVille("Tétouan");
				centre6.setCodePostal("93000");
				centre6.setLatitude(35.5714);
				centre6.setLongitude(-5.3684);
				centre6.setEtat("ouvert");
				centre6.setTypeCollecte("Sang total, Plasma");
				centre6.setHoraire("Lun-Ven: 8h-16h");
				centre6.setEmail("CRTS.TETOUAN@example.com");
				centre6.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre6);

				// Centre 7 - Oujda
				Centre centre7 = new Centre();
				centre7.setNom("Centre régional de transfusion sanguine de Oujda");
				centre7.setAdresse("Av. Zerktouni, Oujda");
				centre7.setVille("Oujda");
				centre7.setCodePostal("60000");
				centre7.setLatitude(34.6835);
				centre7.setLongitude(-1.9095);
				centre7.setEtat("ouvert");
				centre7.setTypeCollecte("Sang total");
				centre7.setHoraire("Lun-Ven: 8h-14h");
				centre7.setEmail("CRTS.OUJDA@example.com");
				centre7.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre7);

				// Centre 8 - Agadir
				Centre centre8 = new Centre();
				centre8.setNom("Centre régional de transfusion sanguine d’Agadir");
				centre8.setAdresse("Av. Hassan II, Agadir");
				centre8.setVille("Agadir");
				centre8.setCodePostal("80000");
				centre8.setLatitude(30.4278);
				centre8.setLongitude(-9.5981);
				centre8.setEtat("ouvert");
				centre8.setTypeCollecte("Sang total, Plaquettes");
				centre8.setHoraire("Lun-Sam: 8h-18h");
				centre8.setEmail("CRTS.AGADIR@example.com");
				centre8.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre8);

				// Centre 9 - Meknès
				Centre centre9 = new Centre();
				centre9.setNom("Centre régional de transfusion sanguine de Meknès");
				centre9.setAdresse("Av. Allal Ben Abdellah, Meknès");
				centre9.setVille("Meknès");
				centre9.setCodePostal("50000");
				centre9.setLatitude(33.8935);
				centre9.setLongitude(-5.5473);
				centre9.setEtat("ouvert");
				centre9.setTypeCollecte("Sang total");
				centre9.setHoraire("Lun-Ven: 8h-16h");
				centre9.setEmail("CRTS.MEKNES@example.com");
				centre9.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre9);

				// Centre 10 - Safi
				Centre centre10 = new Centre();
				centre10.setNom("Centre régional de transfusion sanguine de Safi");
				centre10.setAdresse("Bd. Hassan II, Safi");
				centre10.setVille("Safi");
				centre10.setCodePostal("46000");
				centre10.setLatitude(32.2994);
				centre10.setLongitude(-9.2372);
				centre10.setEtat("ouvert");
				centre10.setTypeCollecte("Sang total");
				centre10.setHoraire("Lun-Ven: 9h-15h");
				centre10.setEmail("CRTS.SAFI@example.com");
				centre10.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre10);

				// Centre 11 - Nador
				Centre centre11 = new Centre();
				centre11.setNom("Centre régional de transfusion sanguine de Nador");
				centre11.setAdresse("Av. Ibn Khaldoun, Nador");
				centre11.setVille("Nador");
				centre11.setCodePostal("62000");
				centre11.setLatitude(35.1717);
				centre11.setLongitude(-2.9283);
				centre11.setEtat("ouvert");
				centre11.setTypeCollecte("Sang total");
				centre11.setHoraire("Lun-Ven: 8h-14h");
				centre11.setEmail("CRTS.NADOR@example.com");
				centre11.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre11);

				// Centre 12 - El Jadida
				Centre centre12 = new Centre();
				centre12.setNom("Centre régional de transfusion sanguine d’El Jadida");
				centre12.setAdresse("Av. Mohammed V, El Jadida");
				centre12.setVille("El Jadida");
				centre12.setCodePostal("24000");
				centre12.setLatitude(33.2348);
				centre12.setLongitude(-8.5000);
				centre12.setEtat("ouvert");
				centre12.setTypeCollecte("Sang total, Plasma");
				centre12.setHoraire("Lun-Ven: 9h-16h");
				centre12.setEmail("CRTS.ELJADIDA@example.com");
				centre12.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre12);

				// Centre 13 - Al Hoceima
				Centre centre13 = new Centre();
				centre13.setNom("Centre régional de transfusion sanguine d’Al Hoceima");
				centre13.setAdresse("Av. Hassan II, Al Hoceima");
				centre13.setVille("Al Hoceima");
				centre13.setCodePostal("32000");
				centre13.setLatitude(35.2486);
				centre13.setLongitude(-3.9315);
				centre13.setEtat("ouvert");
				centre13.setTypeCollecte("Sang total");
				centre13.setHoraire("Lun-Ven: 9h-14h");
				centre13.setEmail("CRTS.ALHOCEIMA@example.com");
				centre13.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre13);

				// Centre 14 - Centre national – Rabat (non réservé aux dons, mais à ajouter)
				Centre centre14 = new Centre();
				centre14.setNom("Centre national de transfusion sanguine – Rabat");
				centre14.setAdresse("Av. Ibn Sina, Rabat");
				centre14.setVille("Rabat");
				centre14.setCodePostal("10000");
				centre14.setLatitude(34.0209);
				centre14.setLongitude(-6.8416);
				centre14.setEtat("ouvert");
				centre14.setTypeCollecte("Plasma, Plaquettes");
				centre14.setHoraire("Lun-Ven: 9h-17h");
				centre14.setEmail("CNTSH.RABAT@example.com");
				centre14.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre14);

				// Centre 15 CRTS - Laâyoune
				Centre centre15 = new Centre();
				centre15.setNom("Centre Régional de Transfusion Sanguine (CRTS) – Laâyoune");
				centre15.setAdresse("Av. Hassan II, Laâyoune");
				centre15.setVille("Laâyoune");
				centre15.setCodePostal("70000");
				centre15.setLatitude(27.1535);  // Coordonnées approximatives
				centre15.setLongitude(-13.2033);
				centre15.setEtat("ouvert");
				centre15.setTypeCollecte("Sang total, Plasma");
				centre15.setHoraire("Lun-Sam: 8h30-16h30");
				centre15.setEmail("CRTS.LAAYOUNE@example.com");
				centre15.setPassword(passwordEncoder.encode("centre123"));

				centreRepository.save(centre15);

				System.out.println("15 centres de don de sang avec types de collecte ont été créés !");
			}
		};
	}
}
