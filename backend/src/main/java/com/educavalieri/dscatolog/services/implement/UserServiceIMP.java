package com.educavalieri.dscatolog.services.implement;

import com.educavalieri.dscatolog.dto.RoleDTO;
import com.educavalieri.dscatolog.dto.UserDTO;
import com.educavalieri.dscatolog.dto.UserInsertDto;
import com.educavalieri.dscatolog.entities.Category;
import com.educavalieri.dscatolog.entities.Role;
import com.educavalieri.dscatolog.entities.User;
import com.educavalieri.dscatolog.repositories.RoleRepository;
import com.educavalieri.dscatolog.repositories.UserRepository;
import com.educavalieri.dscatolog.services.exceptions.DataBaseException;
import com.educavalieri.dscatolog.services.exceptions.ResourceNotFoundException;
import com.educavalieri.dscatolog.services.interfaces.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceIMP implements UserServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public List<UserDTO> findAll() {
        List<User> list = userRepository.findAll();
        return list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());

    }

    @Transactional
    @Override
    public UserDTO findById(Long id) {
        Optional<User> obj = userRepository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("ID not found"));
        return new UserDTO(entity);
    }

    @Override
    @Transactional
    public UserDTO insert(UserInsertDto dto) {
        User entity = new User();
        constructEntity(entity, dto);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(entity);
        return new UserDTO(entity);
    }

    @Override
    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        try {
            User entity = userRepository.findById(id).get();
            constructEntity(entity, dto);
            userRepository.save(entity);
            return new UserDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("ID not found" +id);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found" +id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }
    }

    @Override
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> list = userRepository.findAll(pageable);
        return list.map(x -> new UserDTO(x));

    }
    public void constructEntity(User entity, UserDTO dto){

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();
        for (RoleDTO roleDto : dto.getRoles()){
            Role role = roleRepository.getOne(roleDto.getId());
            entity.getRoles().add(role);
        }

    }



}
