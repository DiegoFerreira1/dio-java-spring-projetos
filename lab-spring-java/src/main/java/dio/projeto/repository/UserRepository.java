package dio.projeto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dio.projeto.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    //Inserção de métodos personalizados
    
}
