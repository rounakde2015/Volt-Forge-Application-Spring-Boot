package com.voltforge.app.controller;

import com.voltforge.app.model.User;
import com.voltforge.app.payload.AddressDTO;
import com.voltforge.app.service.AddressService;
import com.voltforge.app.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AddressController {
    @Autowired
    AuthUtil authUtil;

    @Autowired
    AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
        User loggedInUser = authUtil.loggedInUser();

        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, loggedInUser);

        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        List<AddressDTO> addressDTO = addressService.getAddresses();

        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressesById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressById(addressId);

        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/user/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressesByLoggedInUser() {
        User loggedInUser = authUtil.loggedInUser();

        List<AddressDTO> savedUserAddressDTO = addressService.getLoggedInUserAddresses(loggedInUser);

        return new ResponseEntity<>(savedUserAddressDTO, HttpStatus.OK);
    }

    @PutMapping("/user/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateUserAddress(@RequestBody AddressDTO addressDTO, @PathVariable Long addressId) {
        AddressDTO updatedAddress = addressService.updateUserAddress(addressDTO, addressId);

        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/user/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        String deletedUserAddressStatus = addressService.deleteUserAddress(addressId);

        return new ResponseEntity<>(deletedUserAddressStatus, HttpStatus.OK);
    }


}
