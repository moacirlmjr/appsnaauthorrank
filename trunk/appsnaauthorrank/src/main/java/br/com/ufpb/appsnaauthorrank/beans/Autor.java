package br.com.ufpb.appsnaauthorrank.beans;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
public class Autor extends EntityMaster {

	private String nome;
	private String abreviacao;
	private Integer hindex;

	@ManyToMany(targetEntity = Artigo.class, mappedBy = "autores", cascade = CascadeType.ALL)
	private Set<Artigo> artigos;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAbreviacao() {
		return abreviacao;
	}

	public void setAbreviacao(String abreviacao) {
		this.abreviacao = abreviacao;
	}

	public Integer getHindex() {
		return hindex;
	}

	public void setHindex(Integer hindex) {
		this.hindex = hindex;
	}

}
