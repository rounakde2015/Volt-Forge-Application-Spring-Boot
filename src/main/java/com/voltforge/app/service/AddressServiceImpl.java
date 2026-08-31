package com.voltforge.app.service;

import com.voltforge.app.exception.ResourceNotFoundException;
import com.voltforge.app.model.Address;
import com.voltforge.app.model.User;
import com.voltforge.app.payload.AddressDTO;
import com.voltforge.app.repository.AddressRepository;
import com.voltforge.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User loggedInUser) {
        Address address = modelMapper.map(addressDTO, Address.class);

        List<Address> addressList = loggedInUser.getAddress();
        addressList.add(address);
        loggedInUser.setAddress(addressList);

        address.setUser(loggedInUser);
        Address savedAddress = addressRepository.save(address);

        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();

        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address addresses = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        return modelMapper.map(addresses, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getLoggedInUserAddresses(User loggedInUser) {
        List<Address>  userAddresses = loggedInUser.getAddress();

        return userAddresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO updateUserAddress(AddressDTO addressDTO, Long addressId) {
        Address savedUserAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        savedUserAddress.setBuildingName(addressDTO.getBuildingName());
        savedUserAddress.setCity(addressDTO.getCity());
        savedUserAddress.setCountry(addressDTO.getCountry());
        savedUserAddress.setStreetName(addressDTO.getStreetName());
        savedUserAddress.setPinCode(addressDTO.getPinCode());
        savedUserAddress.setState(addressDTO.getState());

        Address updatedUserAddress = addressRepository.save(savedUserAddress);

        User user = savedUserAddress.getUser();
        user.getAddress().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddress().add(updatedUserAddress);
        userRepository.save(user);

        return modelMapper.map(updatedUserAddress, AddressDTO.class);
    }

    @Override
    public String deleteUserAddress(Long addressId) {
        Address savedUserAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        User user = savedUserAddress.getUser();
        user.getAddress().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(savedUserAddress);

        return "Address has been deleted";
    }
}
