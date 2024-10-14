package com.kike.suscription.service.impl;

import com.kike.suscription.dto.ResponseDto;
import com.kike.suscription.dto.SuscriptionDto;
import com.kike.suscription.dto.clients.UserTypeDto;
import com.kike.suscription.entity.Suscription;
import com.kike.suscription.exception.IncorrectTypeOfUserException;
import com.kike.suscription.exception.SuscriptionAlreadyExistsException;
import com.kike.suscription.mapper.SuscriptionMapper;
import com.kike.suscription.repository.SuscriptionRepository;
import com.kike.suscription.service.ISuscriptionService;
import com.kike.suscription.service.client.UserFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SuscriptionServiceImpl implements ISuscriptionService {

    private SuscriptionRepository suscriptionRepository;
    private UserFeignClient userFeignClient;

    @Override
    public void createSuscription(SuscriptionDto suscriptionDto) {

        String clientId = suscriptionDto.getClientId();
        String vendorId = suscriptionDto.getVendorId();

        checkIfsuscriptionDoesntExist(clientId, vendorId);
        ResponseEntity<UserTypeDto> clientTypeResponse = userFeignClient.getType(clientId);
        ResponseEntity<UserTypeDto> vendorTypeResponse = userFeignClient.getType(vendorId);

        String clientType = clientTypeResponse.getBody().getType();
        String vendorType = vendorTypeResponse.getBody().getType();

        if (!clientType.equals("client")) {
            throw new IncorrectTypeOfUserException(clientId, "client", clientType);
        }

        if (!vendorType.equals("vendor")) {
            throw new IncorrectTypeOfUserException(vendorId, "vendor", clientType);
        }

        Suscription suscription = SuscriptionMapper.mapToSuscription(suscriptionDto, new Suscription());

        suscriptionRepository.save(suscription);

    }

    @Override
    public List<String> fetchVendorIdsByClientId(String clientId) {

        return suscriptionRepository.findByClientId(clientId).stream()
                .map(Suscription::getVendorId)
                .toList();
    }

    private void checkIfsuscriptionDoesntExist(String userId, String vendorId) {
        Optional<Suscription> suscription = suscriptionRepository.findByClientIdAndVendorId(userId, vendorId);

        if (suscription.isPresent())
            throw new SuscriptionAlreadyExistsException(userId, vendorId);
    }
}
