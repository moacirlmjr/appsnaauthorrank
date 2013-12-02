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
    @JoinTable(name = "Artigo_Autores", joinColumns =
    @JoinColumn(name = "artigo_id"), inverseJoinColumns =
    @JoinColumn(name = "autor_id"))
    private Set<Autor> autores;
    @ManyToMany(targetEntity = Pesquisa.class, mappedBy = "artigos", cascade = CascadeType.ALL)
    private Set<Pesquisa> pesquisas;
    @ManyToMany(targetEntity = Artigo.class, mappedBy = "artigos", cascade = CascadeType.ALL)
    private Set<Artigo> referencia;

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

    public Set<Pesquisa> getPesquisas() {
        return pesquisas;
    }

    public void setPesquisas(Set<Pesquisa> pesquisas) {
        this.pesquisas = pesquisas;
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
}
