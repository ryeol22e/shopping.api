package com.project.shopping.common.repository;

import org.springframework.data.repository.CrudRepository;
import com.project.shopping.common.dto.TokenInfo;

public interface TokenRepository extends CrudRepository<TokenInfo, String> {

}
