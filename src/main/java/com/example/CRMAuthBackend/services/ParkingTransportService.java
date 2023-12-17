package com.example.CRMAuthBackend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.CRMAuthBackend.dto.auth.RegistrationDto;
import com.example.CRMAuthBackend.dto.auth.TokenPayloadDto;
import com.example.CRMAuthBackend.dto.busies.BusyDto;
import com.example.CRMAuthBackend.dto.entities.Codes2FADto;
import com.example.CRMAuthBackend.dto.entities.ParkingTransport;
import com.example.CRMAuthBackend.dto.exceptions.IncorrectTokenException;
import com.example.CRMAuthBackend.dto.parks.PlaceDto;
import com.example.CRMAuthBackend.repositories.Codes2FARepository;
import com.example.CRMAuthBackend.repositories.ParkRepository;
import com.example.CRMAuthBackend.repositories.ParkingTransportRepository;
import com.example.CRMAuthBackend.utils.RandomCodeGenerator;
import com.google.gson.Gson;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class ParkingTransportService {

    @Autowired
    private ParkingTransportRepository parkingTransportRepository;

    @Autowired
    private ParkRepository parkRepository;

    @Autowired
    private Codes2FARepository codesDto;

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @Value("${public.key.api.zvonok}")
    private String publicKeyApiZvonok;

    @Value("${company.id.api.zvonok}")
    private String companyIdApiZvonok;


    @Autowired
    private Gson gson;



    @Async
    public void createParkingTransport(ParkingTransport parkingTransport) {
        ParkingTransport parkingTransportNow = parkingTransportRepository.findByCarNumber(parkingTransport.getCarNumber());
        if (parkingTransportNow == null) {
            parkingTransportRepository.save(parkingTransport);
        } else {
            parkingTransportNow.setPark(parkingTransport.getPark());
            parkingTransportNow.setBusy(false);
            parkingTransportNow.setCarNumber(parkingTransport.getCarNumber());
            parkingTransportNow.setInvalid(parkingTransport.isInvalid());
            parkingTransportNow.setTariff(parkingTransport.getTariff());
            parkingTransportNow.setTimeEntry(parkingTransport.getTimeEntry());

            parkingTransportRepository.save(parkingTransportNow);
        }
    }

    @Async
    public void deleteParkingTransport(int id) {
        parkingTransportRepository.deleteById(id);
    }

    @Async
    public CompletableFuture<ParkingTransport> findByCarNumber(String carNumber) {
        return CompletableFuture.completedFuture(parkingTransportRepository.findByCarNumber(carNumber));
    }

    @Async
    public CompletableFuture<Integer> calculatingCost(int id, LocalDateTime timeExit) {
        ParkingTransport parkingTransport = parkingTransportRepository.findById(id).orElse(null);

        if (parkingTransport != null) {
            return CompletableFuture.completedFuture(calculateHoursDifference(parkingTransport.getTimeEntry(), timeExit) * parkingTransport.getTariff().getCost());
        }

        return CompletableFuture.completedFuture(0);
    }

    @Async
    public CompletableFuture<PlaceDto> getBusyPlace(int id) {
        int normalCount = parkingTransportRepository.countByParkIdAndIsInvalid(id, false);
        int invalidCount = parkingTransportRepository.countByParkIdAndIsInvalid(id, true);

        return CompletableFuture.completedFuture(new PlaceDto(invalidCount, normalCount));
    }

    @Async
    public CompletableFuture<Boolean> validate2FACode(HttpServletRequest request, String code) throws IncorrectTokenException {
        String jwtToken;
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
        } else throw new IncorrectTokenException("Incorrect refresh token");

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm)
                .build();

        DecodedJWT decoded = verifier.verify(jwtToken);

        byte[] decodedBytes = Base64.getDecoder().decode(decoded.getPayload());
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

        BusyDto busyDto = gson.fromJson(decodedString, BusyDto.class);

        Codes2FADto codes2FADto = codesDto.findByPhoneNumberAndCode(busyDto.getPhoneNumber(), code);

        if (codes2FADto == null) return CompletableFuture.completedFuture(false);
        codesDto.delete(codes2FADto);

        parkingTransportRepository.save(new ParkingTransport(busyDto.getNumberCar(), false,
                parkRepository.findById(busyDto.getParkId()).get()));

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();

        Runnable task = () -> {

            ParkingTransport parkingTransport = parkingTransportRepository.findByCarNumber(busyDto.getNumberCar());
            if (parkingTransport.isBusy()) {
                parkingTransportRepository.deleteById(parkingTransport.getId());
            }

            scheduler.destroy();
        };

        scheduler.schedule(task, new Date(System.currentTimeMillis() + (60000 * 30))); // выполнить через 30 минут


        return CompletableFuture.completedFuture(true);
    }

    public CompletableFuture<String> sendCodeToPhone(String phone, int parkId,String numberCar) throws MessagingException, IOException {
        Date now = new Date();
        Date validityTemporary = new Date(now.getTime() + 300000); // 5 minutes (temporary token)

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        String temporaryToken = JWT.create()
                .withIssuer("Smart Parking")
                .withSubject(phone)
                .withIssuedAt(now)
                .withExpiresAt(validityTemporary)
                .withPayload(gson.toJson(new BusyDto(numberCar, phone, parkId)))
                .sign(algorithm);

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://zvonok.com/manager/cabapi_external/api/v1/phones/tellcode/";

        String code = RandomCodeGenerator.generateRandomCode(4);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("public_key", publicKeyApiZvonok);
        map.add("phone", phone);
        map.add("campaign_id", companyIdApiZvonok);
        map.add("pincode", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        String response = restTemplate.postForObject(url, request, String.class);

        codesDto.save(new Codes2FADto(phone, code));

        return CompletableFuture.completedFuture(temporaryToken);
    }






    private int calculateHoursDifference(LocalDateTime timeEntry, LocalDateTime timeExit) {
        long minutesDifference = ChronoUnit.MINUTES.between(timeEntry, timeExit);

        // Рассчитываем часы с округлением вверх
        int hoursDifference = (int) Math.ceil(minutesDifference / 60.0);

        return hoursDifference;
    }
}
