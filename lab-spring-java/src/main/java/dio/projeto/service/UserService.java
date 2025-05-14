package dio.projeto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dio.projeto.model.User;
import dio.projeto.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> listarUsuarios(){
        return userRepository.findAll();
    }

    public User salvarUsuario(User user){
        return userRepository.save(user);
    }

    public Optional<User> buscarUsuarioPorId(Long id) {
        return userRepository.findById(id);
    }


    public void deletarUsuarioPorId(Long id) {
        userRepository.deleteById(id);
    }
    

    
}
