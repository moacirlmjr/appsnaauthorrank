package br.com.ufpb.appsnaauthorrank.beans;

import java.util.Set;

public class Autor implements Comparable<Autor> {

	private String nome;
	private String abreviacao;
	private Integer hindex;
	private String instituicao;

	private Set<Artigo> artigos;

	public String getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}

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

	public Set<Artigo> getArtigos() {
		return artigos;
	}

	public void setArtigos(Set<Artigo> artigos) {
		this.artigos = artigos;
	}

	@Override
	public int hashCode() {
		return this.getNome().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		Autor autor = (Autor) o;

		if (this.getNome().equals(autor.getNome()))
			return true;
		else
			return false;
	}

	@Override
	public int compareTo(Autor t) {
		return this.getNome().compareTo(t.getNome());
	}

	@Override
	public String toString() {
		return nome;
	}
	
	

}
