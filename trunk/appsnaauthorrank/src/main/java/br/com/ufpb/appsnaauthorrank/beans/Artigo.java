package br.com.ufpb.appsnaauthorrank.beans;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

public class Artigo extends EntityMaster {
	private String titulo;
	private String pubYear;
	private String ondePub;
	private String linkDownload;
	private String linkDetalhe;
	private String origem;
	private String issn;

	@ManyToMany(targetEntity = Autor.class, mappedBy = "artigos", cascade = CascadeType.ALL)
	@JoinTable(name = "Artigo_Autores", joinColumns = @JoinColumn(name = "artigo_id"), inverseJoinColumns = @JoinColumn(name = "autor_id"))
	private Set<Autor> autores;
	
	@ManyToMany(targetEntity = Pesquisa.class, mappedBy = "artigos", cascade = CascadeType.ALL)
	private Set<Pesquisa> pesquisas;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getPubYear() {
		return pubYear;
	}

	public void setPubYear(String pubYear) {
		this.pubYear = pubYear;
	}

	public String getOndePub() {
		return ondePub;
	}

	public void setOndePub(String ondePub) {
		this.ondePub = ondePub;
	}

	public String getLinkDownload() {
		return linkDownload;
	}

	public void setLinkDownload(String linkDownload) {
		this.linkDownload = linkDownload;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

}
