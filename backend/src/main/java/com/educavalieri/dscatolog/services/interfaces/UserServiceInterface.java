package com.educavalieri.dscatolog.services.interfaces;

import com.educavalieri.dscatolog.dto.UserDTO;
import com.educavalieri.dscatolog.dto.UserInsertDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserServiceInterface {

    List<UserDTO> findAll();

    UserDTO findById(Long id);

    UserDTO insert(UserInsertDTO dto);

    UserDTO update(Long id, UserDTO dto);

    void delete(Long id);

    Page<UserDTO> findAllPaged(Pageable pageable);
}
