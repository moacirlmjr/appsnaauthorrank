package br.com.ufpb.appsnaauthorrank.beans;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Pesquisa extends EntityMaster {

	private String nome;
	private String query;

	@ManyToMany(targetEntity = Artigo.class, mappedBy = "pesquisas", cascade = CascadeType.ALL)
	@JoinTable(name = "Pesquisa_Artigos", joinColumns = @JoinColumn(name = "pesquisa_id"), inverseJoinColumns = @JoinColumn(name = "artigo_id"))
	private Set<Artigo> artigos;

}
