package com.devsuperior.dscatalog.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

//6 - Anotação JPA: Usada para manipular ORM (Mapeamento de Objeto Relacional) no banco de dados (Nesse caso o H2)
//6.1 - Essa anotação informa ao banco que está classe é uma entidade de negócio
//7 - Table: Gera uma script SQL CREATE TABLE (passamos o nome como parâmetro)
@Entity
@Table(name = "tb_category")
public class Category implements Serializable{

	//2- Padrão Java para que o objeto possa ser convertido em bytes e ser passado para arquivos e redes. É uma boa práticae
	private static final long serialVersionUID = 1L;
	
	//8 - Anotação JPA de ID para o Banco de Dados
	//9 - GeneratedValue: Anotação JPA para que o ID seja auto incrementado pelo próprio banco de dados de 1 em 1;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant createdAt;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant updatedAt;
	
	public Category() {	
	}

	public Category(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}
	
	//Dados de auditoria criados, toda vez que o create ou update forem chamados, será salvo o tempo
	@PrePersist
	public void prePersist() {
		createdAt = Instant.now();
	}
	
	@PreUpdate
	public void preUpdated() {
		updatedAt = Instant.now();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(id, other.id);
	}
}
