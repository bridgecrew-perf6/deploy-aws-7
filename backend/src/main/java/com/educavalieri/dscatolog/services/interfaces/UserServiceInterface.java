package com.educavalieri.dscatolog.services.interfaces;

import com.educavalieri.dscatolog.dto.ProductDTO;
import com.educavalieri.dscatolog.dto.UserDTO;
import com.educavalieri.dscatolog.dto.UserInsertDto;
import com.educavalieri.dscatolog.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserServiceInterface {

    List<UserDTO> findAll();

    UserDTO findById(Long id);

    UserDTO insert(UserInsertDto dto);

    UserDTO update(Long id, UserDTO dto);

    void delete(Long id);

    Page<UserDTO> findAllPaged(Pageable pageable);
}
