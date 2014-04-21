package br.com.ufpb.appsnaauthorrank.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	private static FileWriter fw;

	public static void criaArquivo(String local, Boolean isConstant) {
		try {		
			File f = new File(Constantes.DIR_APPSNA_ELEMENTOS);
			if(!f.exists()){
				f.mkdir();
			}
			File f2 = new File(Constantes.DIR_APPSNA_RELACIONAMENTOS);
			if(!f2.exists()){
				f2.mkdir();
			}
			fw = new FileWriter(local, isConstant);
			AppSNALog.info("Arquivo gerado com sucesso");
		} catch (IOException e) {
			AppSNALog.error(e.toString());
		}
	}
	
	public static void refresh() {
		try {
			fw.flush();
		} catch (IOException e) {
			AppSNALog.error(e.toString());
		}
	}

	public static void escreveNoArquivo(String conteudo) {
		try {
			fw.write(conteudo);
		} catch (IOException e) {
			AppSNALog.error(e.toString());
		}
	}

	public static void criarCabecalho(String[] conteudo) {
		for (String n : conteudo) {
			FileUtil.escreveNoArquivo(n);
			FileUtil.quebra();
		}
		FileUtil.quebraLinha(1);
	}

	public static void quebraLinha(int num) {
		try {
			for (int x = 0; x < num; x++) {
				fw.write("\n");
			}
		} catch (IOException e) {
			AppSNALog.error(e.toString());
		}
	}

	public static void inserirTabulacao(int num) {
		try {
			for (int x = 0; x < num; x++) {
				fw.write("\r");
			}
		} catch (IOException e) {
			AppSNALog.error(e.toString());
		}
	}

	public static void quebra() {
		try {
			fw.write(";");
		} catch (IOException e) {
			AppSNALog.error(e.toString());
		}
	}

	public static void salvarArquivo() {
		try {
			fw.close();
			AppSNALog.info("Arquivo gravado com sucesso");
		}
		// em caso de erro apresenta mensagem abaixo
		catch (Exception e) {
			AppSNALog.error(e.toString());
		}
	}
	
	public static List<File> listarArquivosDir(String dir){
		
		File diretorio = new File(dir);
		List<File> arquivosCsv = new ArrayList<File>();
		if(diretorio.isDirectory()){
			File arquivos[] = diretorio.listFiles();
			for(File arq : arquivos){
				if(arq.getName().contains(".csv")){
					arquivosCsv.add(arq);
				}
			}
		}
		
		return arquivosCsv;
	}
	
	public static List<File> listarArquivosLog(String dir){
		
		File diretorio = new File(dir);
		List<File> arquivosCsv = new ArrayList<File>();
		if(diretorio.isDirectory()){
			File arquivos[] = diretorio.listFiles();
			for(File arq : arquivos){
				if(arq.getName().contains(".log")){
					arquivosCsv.add(arq);
				}
			}
		}
		
		return arquivosCsv;
	}

}
