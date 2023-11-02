package com.ilyes.ClientsRegions.repositories;

import com.ilyes.ClientsRegions.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

  @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidTokenByUser(Integer id);

  @Query(value = """
      select t from Token t where t.token = :token and t.revoked= false and t.expired= false
      """)
  Optional<Token> findByToken(String token);

  void save(String token);
}
