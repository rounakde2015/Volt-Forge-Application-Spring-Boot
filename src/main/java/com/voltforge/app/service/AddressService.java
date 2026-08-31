package com.voltforge.app.service;

import com.voltforge.app.model.User;
import com.voltforge.app.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User loggedInUser);


    List<AddressDTO> getAddresses();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getLoggedInUserAddresses(User loggedInUser);

    AddressDTO updateUserAddress(AddressDTO addressDTO, Long addressId);

    String deleteUserAddress(Long addressId);
}
