package br.com.ufpb.appsnaauthorrank.beans;

import java.util.Set;

public class Artigo {

	private Long id;
	private String titulo;
	private String pubYear;
	private String ondePub;
	private String linkDownload;
	private String linkDetalhe;
	private String origem;
	private String issn;
	private Set<Autor> autores;
	private String keywords;
	private Set<Artigo> referencia;
	private Set<String> termos;

	public Set<String> getTermos() {
		return termos;
	}

	public void setTermos(Set<String> termos) {
		this.termos = termos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getLinkDetalhe() {
		return linkDetalhe;
	}

	public void setLinkDetalhe(String linkDetalhe) {
		this.linkDetalhe = linkDetalhe;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	public Set<Autor> getAutores() {
		return autores;
	}

	public void setAutores(Set<Autor> autores) {
		this.autores = autores;
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

	public Set<Artigo> getReferencia() {
		return referencia;
	}

	public void setReferencia(Set<Artigo> referencia) {
		this.referencia = referencia;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
}
