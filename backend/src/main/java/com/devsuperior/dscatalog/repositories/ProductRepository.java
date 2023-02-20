package com.devsuperior.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Product;

/*10 - Repository: É a camada de acesso a dados, implementando uma JpaRepository, criando uma interface para a classe Category (os métodos valem
 * para qualquer BD Relacional)
 * */
//14 - Repository: Anotação JPA para que a interface possa ser "injetada" posteriormente
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}